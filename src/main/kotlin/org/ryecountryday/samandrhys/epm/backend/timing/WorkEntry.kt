package org.ryecountryday.samandrhys.epm.backend.timing

import java.util.Date

class WorkEntry(val start: Date, val end: Date) {
    val dates: Set<Date>
        get() {
            val dates = mutableSetOf<Date>()
            var current = start
            while (current.before(end)) {
                dates.add(current)
                current = Date(current.time + 86400000) // 24 hours in milliseconds
            }
            return dates
        }

    val duration: Long
        get() = end.time - start.time
}