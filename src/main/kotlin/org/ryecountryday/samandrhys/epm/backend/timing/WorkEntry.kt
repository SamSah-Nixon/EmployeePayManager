package org.ryecountryday.samandrhys.epm.backend.timing

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.util.*
import java.time.Instant

@Serializable(with = WorkEntrySerializer::class)
class WorkEntry(val start: Instant, val end: Instant?, val id: Int) : Comparable<WorkEntry> {
    val datesWorked: Set<Instant>
        get() {
            val dates = mutableSetOf<Instant>()
            var current = start
            while (current.isBefore(end)) {
                dates.add(current.startOfDay())
                current = current.plusDays(1)
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