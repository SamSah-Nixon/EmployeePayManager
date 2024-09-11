package org.ryecountryday.samandrhys.epm.util

import java.time.Instant
import java.time.temporal.ChronoUnit

fun Instant.plusDays(days: Long): Instant {
    return this.plusSeconds(days * 86400)
}

fun Instant.plusHours(hours: Double): Instant {
    return this.plusSeconds((hours * 3600).toLong())
}

fun Instant.startOfDay(): Instant {
    return this.truncatedTo(ChronoUnit.DAYS)
}