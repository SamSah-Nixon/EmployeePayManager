/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend.timing

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.ryecountryday.samandrhys.epm.backend.PayPeriod
import org.ryecountryday.samandrhys.epm.util.json
import java.nio.file.Path
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
    fun addPayPeriod(startDate: LocalDate, endDate: LocalDate): Boolean {
        if(endDate.isEqual(payPeriods[0].payPeriodEnd)) return false
        payPeriods.addFirst(PayPeriod(startDate, endDate, currentPeriod))
        currentPeriod = mutableSetOf()
        return true
    }

    fun clockIn(id: String) {
        clockedIn.add(WorkEntry(Instant.now(), id))
    }

    fun isClockedIn(id: String) : Boolean {
        return getEntry(id).let { it != null && it.end == null }
    }

    fun getEntry(id: String) : WorkEntry? {
        return clockedIn.firstOrNull { it.id == id }
    }

    fun clockOut(id: String) {
        getEntry(id)?.let {
            it.end = Instant.now()
            //If a work entry spans multiple days split it into two
            if(it.start.atZone(ZoneId.systemDefault()).toLocalDate().isBefore(it.end!!.atZone(ZoneId.systemDefault()).toLocalDate())) {
                val startOfNewDay: Instant = it.end!!.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
                val entry1: WorkEntry = WorkEntry(it.start,startOfNewDay, id)
                val entry2: WorkEntry = WorkEntry(startOfNewDay,it.end, id)
                //If a work entry spreads across multiple pay periods add the second day into the new pay period
                if(it.start.atZone(ZoneId.systemDefault()).toLocalDate().isEqual(payPeriods[0].payPeriodStart)) {
                    payPeriods[0].workEntries.add(entry2)
                }
                else {
                    currentPeriod.add(entry2)
                }
                currentPeriod.add(entry1)
            }
            else{
                currentPeriod.add(it)
            }
            clockedIn.remove(it)
        }
    }

    fun load(path: Path) {
        val element = json.parseToJsonElement(path.readText())
        currentPeriod = json.decodeFromJsonElement(element.jsonObject["currentPeriod"]!!)
        clockedIn.addAll(json.decodeFromJsonElement(element.jsonObject["clockedIn"].let {
            it ?: element.jsonObject["entries"]!!
        }))
        payPeriods.addAll(json.decodeFromJsonElement(element.jsonObject["payPeriods"]!!))
    }

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

