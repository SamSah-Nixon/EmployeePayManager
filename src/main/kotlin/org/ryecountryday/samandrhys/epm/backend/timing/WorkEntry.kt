package org.ryecountryday.samandrhys.epm.backend.timing

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.util.*
import java.time.Instant

/**
 * Represents a single work entry for an employee.
 *
 * @property start The time someone clocked in
 * @property end The time someone clocked out. If null, the work entry is still ongoing.
 * @property id The ID of the work entry
 */
@Serializable(with = WorkEntrySerializer::class)
class WorkEntry(val start: Instant, var end: Instant?, val id: String) : Comparable<WorkEntry> {

    constructor(start: Instant, id: String) : this(start, null, id)
    constructor(start: Instant, duration: Double, id: String) : this(start, start.plusSeconds((duration * 3600).toLong()), id)

    val durationSeconds: Long
        get() = (end?.epochSecond ?: Instant.now().epochSecond) - start.epochSecond

    val durationHours: Double
        get() = durationSeconds / 3600.0

    override fun compareTo(other: WorkEntry): Int {
        return start.compareTo(other.start)
    }

    override fun toString(): String {
        return "WorkEntry{\"$id\";start=$start;end=$end}"
    }
}