/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.util

import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun Instant.plusDays(days: Double): Instant {
    return this.plusSeconds((days * 86400).toLong())
}

fun Instant.plusHours(hours: Double): Instant {
    return this.plusSeconds((hours * 3600).toLong())
}

fun Instant.startOfDay(): Instant {
    return this.truncatedTo(ChronoUnit.DAYS)
}

/**
 * Turns a [String] into an instance of [LocalDate]. The string must be in the format "MM/dd/yyyy", "MM-dd-yyyy", or "MM.dd.yyyy".
 * @param str The string to parse.
 */
fun parseDate(str: String): LocalDate {
    val parts = str.split('/', '-', '.')
    return LocalDate.of(parts[2].toInt(), parts[0].toInt(), parts[1].toInt())
}

/**
 * Turns a [Long] into an instance of [LocalDate]. The long must be the number of milliseconds since the Unix epoch.
 * If the long is null, the current time will be used.
 * @param ms The number of milliseconds since the Unix epoch.
 */
// masquerading as a constructor is very funny
fun LocalDate(ms: Long?): LocalDate {
    return LocalDate.ofEpochDay((ms ?: System.currentTimeMillis()) / 86400000)
}

/**
 * Turns a [LocalDate] into a [String]. The string will be in the format "MM/dd/yyyy" by default, but the separator can be changed.
 * This method does not take into account the time of day of the date.
 * @param separator The character to use as the separator between the month, day, and year.
 */
fun LocalDate.toDateString(separator: Char = '/'): String {
    return "$monthValue$separator$dayOfMonth$separator$year"
}
