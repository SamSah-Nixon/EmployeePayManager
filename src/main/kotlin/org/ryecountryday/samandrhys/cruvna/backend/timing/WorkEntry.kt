/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.backend.timing

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.cruvna.util.*
import java.time.Instant
import java.util.*

/**
 * Represents a single work entry for an employee.
 *
 * @property start The time someone clocked in
 * @property end The time someone clocked out. If null, the work entry is still ongoing.
 * @property id The ID of the work entry
 */
@Serializable(with = WorkEntrySerializer::class)
class WorkEntry(var start: Instant = Instant.now(), var end: Instant? = null, var id: String) : Comparable<WorkEntry> {

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkEntry) return false

        return equals(
            start to other.start,
            end to other.end,
            id to other.id
        )
    }

    override fun hashCode(): Int {
        return Objects.hash(id, start, end)
    }
}