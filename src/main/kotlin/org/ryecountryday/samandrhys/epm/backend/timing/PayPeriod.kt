package org.ryecountryday.samandrhys.epm.backend.timing

import java.time.Instant
import java.util.TreeSet

data class PayPeriod(val start: Instant, val end: Instant) {
    val entries : MutableSet<WorkEntry>
        get() = TreeSet<WorkEntry>()
}