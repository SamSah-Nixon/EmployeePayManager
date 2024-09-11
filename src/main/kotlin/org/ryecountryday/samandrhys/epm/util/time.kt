package org.ryecountryday.samandrhys.epm.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

fun Instant.plusDays(days: Long): Instant {
    return this.plusSeconds(days * 86400)
}

fun Instant.plusHours(hours: Double): Instant {
    return this.plusSeconds((hours * 3600).toLong())
}

fun Instant.startOfDay(): Instant {
    return this.truncatedTo(ChronoUnit.DAYS)
}

/**
 * parse a date string in the format "MM/dd/yyyy"
 */
fun parseDate(str: String, separator: Char = '/'): Date {
    return SimpleDateFormat("MM${separator}dd${separator}yyyy").parse(str)
}

fun dateToString(date: Date, separator: Char = '/'): String {
    return SimpleDateFormat("MM${separator}dd${separator}yyyy").format(date)
}