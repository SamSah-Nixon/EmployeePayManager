package org.ryecountryday.samandrhys.epm.backend.timing

import java.util.TreeSet

data class WorkHistory{
    val periods: MutableSet<PayPeriod> = TreeSet<PayPeriod>()
    val clockedIn: MutableSet<WorkEntry> = TreeSet<WorkEntry>()
    val entries: MutableSet<WorkEntry> = TreeSet<WorkEntry>()

    fun clockIn(id: Int) {
        clockedIn.add(id)
    }

    fun clockOut(period: PayPeriod) {
        remove(period)
    }
}