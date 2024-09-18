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
import kotlin.io.path.outputStream
import kotlin.io.path.readText

/**
 * Stores work history for all employees.
 *
 * @property currentPeriod
 * @property entries
 * @property payPeriods
 */
@Serializable
object WorkHistory {

    var currentPeriod = mutableSetOf<WorkEntry>()
    val entries = mutableListOf<WorkEntry>()
    var payPeriods = mutableListOf<PayPeriod>()

    fun addPayPeriod(startDate: LocalDate, endDate: LocalDate) {
        payPeriods.addFirst(PayPeriod(startDate, endDate))
    }

    fun clockIn(id: String) {
        entries.addFirst(WorkEntry(Instant.now(), id))
    }

    fun isClockedIn(id: String) : Boolean {
        return getEntry(id).let { it != null && it.end == null }
    }

    fun getEntry(id: String) : WorkEntry? {
        return entries.firstOrNull { it.id == id }
    }

    fun clockOut(id: String) {
        getEntry(id)?.let {
            it.end = Instant.now()
            currentPeriod.add(it)
        }
    }

    fun load(path: Path) {
        val element = json.parseToJsonElement(path.readText())
        currentPeriod = json.decodeFromJsonElement(element.jsonObject["currentPeriod"]!!)
        entries.addAll(json.decodeFromJsonElement(element.jsonObject["entries"]!!))
        payPeriods.addAll(json.decodeFromJsonElement(element.jsonObject["payPeriods"]!!))
    }

    fun save(path: Path) {
        val element = buildJsonObject {
            put("currentPeriod", json.encodeToJsonElement(currentPeriod))
            put("entries", json.encodeToJsonElement(entries))
            put("payPeriods", json.encodeToJsonElement(payPeriods))
        }

        path.outputStream().use {
            it.write(json.encodeToString(element as Map<String, JsonElement>).toByteArray())
        }
    }
}