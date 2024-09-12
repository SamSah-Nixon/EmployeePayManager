package org.ryecountryday.samandrhys.epm.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

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
 * Turns a [String] into an instance of [Date]. The string must be in the format "MM/dd/yyyy", "MM-dd-yyyy", or "MM.dd.yyyy".
 * @param str The string to parse.
 */
fun parseDate(str: String): Date {
    val exeptions = mutableListOf<ParseException>()
    for(separator in listOf('/', '-', '.')) {
        try {
            return SimpleDateFormat("MM${separator}dd${separator}yyyy").parse(str)
        } catch (e: ParseException) {
            exeptions.add(e)
        }
    }

    throw IllegalArgumentException("Could not parse date from string \"$str\"").apply {
        exeptions.forEach { addSuppressed(it) }
    }
}

/**
 * Turns a [Date] into a [String]. The string will be in the format "MM/dd/yyyy" by default, but the separator can be changed.
 * This method does not take into account the time of day of the date.
 * @param separator The character to use as the separator between the month, day, and year.
 */
fun Date.toDateString(separator: Char = '/'): String {
    return SimpleDateFormat("MM${separator}dd${separator}yyyy").format(this)
}