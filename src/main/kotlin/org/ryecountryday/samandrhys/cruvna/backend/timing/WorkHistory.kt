/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.backend.timing

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.ryecountryday.samandrhys.cruvna.backend.PayPeriod
import org.ryecountryday.samandrhys.cruvna.util.json
import org.ryecountryday.samandrhys.cruvna.util.startOfDay
import org.ryecountryday.samandrhys.cruvna.util.toLocalDate
import java.nio.file.Path
import java.time.Instant
import java.time.LocalDate
import kotlin.io.path.outputStream
import kotlin.io.path.readText

/**
 * Stores work history for all employees.
 *
 * @property currentPeriod All work entries in the current pay period
 * @property clockedIn All employees currently clocked in
 * @property payPeriods All pay periods
 */
@Serializable
object WorkHistory {

    var clockedIn = mutableSetOf<WorkEntry>()
    var currentPeriod = mutableSetOf<WorkEntry>()
    var payPeriods = mutableListOf<PayPeriod>()

    /**
     * Adds a pay period to the list of pay periods.
     * @param startDate the start date of the pay period
     * @param endDate the end date of the pay period
     * @return true if the pay period was added successfully,
     * false if a pay period with the same end date already exists
     */
    fun addPayPeriod(endDate: LocalDate): Boolean {
        val previousEnd = payPeriods.getOrNull(0)
        //If the previous pay period ended on the same day as the new one, don't add it
        if(previousEnd != null && endDate.isEqual(previousEnd.payPeriodEnd)) return false
        if(previousEnd == null) payPeriods.addFirst(PayPeriod(LocalDate.of(2024,1,1), endDate, mutableSetOf(*currentPeriod.toTypedArray())))
        else payPeriods.addFirst(PayPeriod(previousEnd.payPeriodEnd, endDate, mutableSetOf(*currentPeriod.toTypedArray())))
        currentPeriod.clear()
        return true
    }

    /**
     * Starts tracking the work of an employee.
     */
    fun clockIn(id: String) {
        clockedIn.add(WorkEntry(id = id))
    }

    /**
     * @return true if the employee with the given ID is currently clocked in, false otherwise
     */
    fun isClockedIn(id: String) : Boolean {
        return getClockedInEntry(id).let { it != null && it.end == null }
    }

    /**
     * @return the work entry of the employee with the given ID that is currently clocked in, or null if the employee is not clocked in
     */
    fun getClockedInEntry(id: String) : WorkEntry? {
        return clockedIn.firstOrNull { it.id == id }
    }

    /**
     * Stops tracking the work of an employee. The work entry is added to the current pay period.
     */
    fun clockOut(id: String) {
        getClockedInEntry(id)?.let {
            it.end = Instant.now()
            //If a work entry spans multiple days split it into two
            if(it.start.toLocalDate().isBefore(it.end!!.toLocalDate())) {
                val startOfNewDay: Instant = it.end!!.startOfDay()
                val entry1 = WorkEntry(it.start, startOfNewDay, id)
                val entry2 = WorkEntry(startOfNewDay, it.end, id)

                //If a work entry spreads across multiple pay periods add the second day into the new pay period
                if(payPeriods.getOrNull(0) != null && it.start.toLocalDate().isEqual(payPeriods.getOrNull(0)!!.payPeriodStart)) {
                    payPeriods[0].workEntries.add(entry2)
                } else {
                    currentPeriod.add(entry2)
                }
                currentPeriod.add(entry1)
            } else {
                currentPeriod.add(it)
            }

            clockedIn.removeIf { it.id == id }
        }
    }

    /**
     * Loads work history from a file.
     */
    fun load(path: Path) {
        val element = json.parseToJsonElement(path.readText())
        currentPeriod = json.decodeFromJsonElement(element.jsonObject["currentPeriod"]!!)
        clockedIn.addAll(json.decodeFromJsonElement(element.jsonObject["clockedIn"].let {
            it ?: element.jsonObject["entries"]!! // Backwards compatibility - old save files used "entries" instead of "clockedIn"
        }))
        payPeriods.addAll(json.decodeFromJsonElement(element.jsonObject["payPeriods"]!!))

        clockedIn.removeIf { it.end != null }
    }

    /**
     * Saves work history to a file.
     */
    fun save(path: Path) {
        val element = buildJsonObject {
            put("currentPeriod", json.encodeToJsonElement(currentPeriod))
            put("clockedIn", json.encodeToJsonElement(clockedIn))
            put("payPeriods", json.encodeToJsonElement(payPeriods))
        }

        path.outputStream().use {
            it.write(json.encodeToString(element as Map<String, JsonElement>).toByteArray())
        }
    }
}

