package org.ryecountryday.samandrhys.epm.backend.timing

import org.ryecountryday.samandrhys.epm.backend.PayPeriod
import java.time.Instant
import java.time.LocalDate

/**
 * Stores work history for all employees.
 *
 * @property currentPeriod
 * @property entries
 * @property payPeriods
 */
object WorkHistory {

    var currentPeriod = mutableSetOf<WorkEntry>()
    val entries = mutableListOf<WorkEntry>()
    var payPeriods = mutableListOf<PayPeriod>()

    fun addPayPeriod(startDate : LocalDate, endDate : LocalDate) {
        payPeriods.addFirst(PayPeriod(startDate, endDate))
    }

    fun clockIn(id: String) {
        entries.addFirst(WorkEntry(Instant.now(), id))
    }

    fun isClockedIn(id: String) : Boolean {
        return getEntry(id).let { it != null && it.end == null }
    }

    fun getEntry(id: String) : WorkEntry? {
        return entries.firstOrNull { it.id == id }
    }

    fun clockOut(id: String) {
        getEntry(id)?.let {
            it.end = Instant.now()
            currentPeriod.add(it)
        }
    }
}