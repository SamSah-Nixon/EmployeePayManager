package org.ryecountryday.samandrhys.epm.backend.timing

import org.ryecountryday.samandrhys.epm.util.*
import java.time.Instant

class WorkEntry(val start: Instant, val end: Instant) : Comparable<WorkEntry> {
    constructor(start: Instant, duration: Double) : this(start, start.plusHours(duration))
    constructor(start: Instant, duration: Int) : this(start, duration.toDouble())

    val datesWorked: Set<Instant>
        get() {
            val dates = mutableSetOf<Instant>()
            var current = start
            while (current.isBefore(end)) {
                dates.add(current.startOfDay())
                current = current.plusSeconds(86400)
            }
            return dates
        }

    val durationSeconds: Long
        get() = end.epochSecond - start.epochSecond

    val durationHours: Double
        get() = durationSeconds / 3600.0

    override fun compareTo(other: WorkEntry): Int {
        return start.compareTo(other.start)
    }

    override fun toString(): String {
        return "WorkEntry{start=$start, end=$end}"
    }
}