/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

/**
 * @return The start of the day of this [Instant].
 */
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

/**
 * Turns an [Instant] into an instance of [LocalDate]. Any time of day information will be discarded.
 */
fun Instant.toLocalDate(): LocalDate {
    return this.atZone(ZoneId.systemDefault()).toLocalDate()
}

/**
 * Turns this [LocalDate] into an instance of [Instant]. The time of day will be midnight in the system default time zone.
 */
fun LocalDate.toInstant(): Instant {
    return this.atStartOfDay().toInstant()
}

/**
 * Turns this [LocalDateTime] into an instance of [Instant] using the system default time zone.
 */
fun LocalDateTime.toInstant(): Instant {
    return ZonedDateTime.of(this, ZoneId.systemDefault()).toInstant()
}

fun formatTime(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    /**
     * Adds an "s" to the end of a string if the number is not 1.
     */
    fun Long.p(): String {
        return if (this == 1L) "" else "s"
    }

    return buildList {
        if (hours > 0) add("$hours hour${hours.p()}")
        if (minutes > 0) add("$minutes minute${minutes.p()}")
        if (seconds > 0) add("$seconds second${seconds.p()}")
        if(isEmpty()) add("0 seconds")
    }.joinToString(", ")
}
