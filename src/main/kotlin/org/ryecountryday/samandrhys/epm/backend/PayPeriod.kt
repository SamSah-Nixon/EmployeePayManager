package org.ryecountryday.samandrhys.epm.backend

import java.time.LocalDate


/**
 * Represents a pay period, which contains all the work entries for a given period of time.
 */
class PayPeriod(var payPeriodStart: LocalDate, var payPeriodEnd: LocalDate) {
    var daysInPeriod = payPeriodStart.until(payPeriodEnd).days

    override fun toString(): String {
        return "Pay Period: $payPeriodStart to $payPeriodEnd"
    }
}