package org.ryecountryday.samandrhys.epm.backend.timing

import org.ryecountryday.samandrhys.epm.backend.PayPeriod
import java.time.Instant
import java.time.LocalDate

/**
 * Stores work history for all employees.
 *
 * @property currentPeriod The work entries currently in the pay period.
 * @property entries The work entries for each pay period.
 * @property payPeriods The pay periods.
 */
object WorkHistory {

    var currentPeriod = mutableSetOf<WorkEntry>()
    val entries = mutableListOf<WorkEntry>()
    var payPeriods = mutableListOf<PayPeriod>()

    fun addPayPeriod(startDate : LocalDate, endDate : LocalDate) {
        payPeriods.add(0,PayPeriod(startDate, endDate))
    }

    fun clockIn(id: String) {
        entries.add(0,WorkEntry(Instant.now(), id))
    }

    fun clockOut(id: String) {
        entries.firstOrNull { it.id == id }?.let {
            it.end = Instant.now()
            currentPeriod.add(it)
        }
    }
}