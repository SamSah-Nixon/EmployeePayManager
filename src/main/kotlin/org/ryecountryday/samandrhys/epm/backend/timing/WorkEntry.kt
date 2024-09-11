package org.ryecountryday.samandrhys.epm.backend.timing

import java.time.Instant

class WorkEntry(val start: Instant, val end: Instant) {
    constructor(start: Instant, duration: Double) : this(start, start.plusNanos((duration * 3_600_000_000_000).toLong()))
    constructor(start: Instant, duration: Int) : this(start, duration.toDouble())

    val dates: Set<Instant>
        get() {
            val dates = mutableSetOf<Instant>()
            var current = start
            while (current.isBefore(end)) {
                dates.add(current)
                current = current.plusSeconds(86400)
            }
            return dates
        }

    val durationSeconds: Long
        get() = end.epochSecond - start.epochSecond
}