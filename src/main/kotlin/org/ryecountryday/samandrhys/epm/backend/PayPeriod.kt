/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.util.PayPeriodSerializer
import java.time.LocalDate

/**
 * Represents a pay period, which contains all the work entries for a given period of time.
 */
@Serializable(with = PayPeriodSerializer::class)
class PayPeriod(var payPeriodStart: LocalDate, var payPeriodEnd: LocalDate) {
    var daysInPeriod = payPeriodStart.until(payPeriodEnd).days

    override fun toString(): String {
        return "Pay Period: $payPeriodStart to $payPeriodEnd"
    }
}