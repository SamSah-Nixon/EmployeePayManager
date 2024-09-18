/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.epm.util.PayPeriodSerializer
import org.ryecountryday.samandrhys.epm.util.toInstant
import org.ryecountryday.samandrhys.epm.util.toLocalDate
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

/**
 * Represents a pay period, which contains all the work entries for a given period of time.
 */
@Serializable(with = PayPeriodSerializer::class)
class PayPeriod(var payPeriodStart: LocalDate, var payPeriodEnd: LocalDate, var workEntries: MutableSet<WorkEntry>) {
    var daysInPeriod = payPeriodStart.until(payPeriodEnd).days

    //Find amount of hours worked for a specific employee in the pay period separated by day
    fun hoursWorkedbyDay(id: String): MutableList<Double> {
        val PPStart : Instant = payPeriodStart.toInstant()
        val PPEnd : Instant = payPeriodEnd.atTime(LocalTime.MAX).toInstant()
        //Find all workEntries that match the employee id and are within the pay period
        val workEntries = workEntries.filter {
            it.id == id && (it.end?.isAfter(PPStart) ?: true) && it.start.isBefore(PPEnd)
        }

        val hoursWorked: MutableList<Double> = mutableListOf()
        var dateIterator: LocalDate = payPeriodStart
        var hours = 0.0

        while (dateIterator.isBefore(payPeriodEnd)) {
            for (workEntry in workEntries) {
                //Find all entries on this day
                if (workEntry.start.toLocalDate() == dateIterator) {
                    hours += workEntry.durationHours
                }
            }
            hoursWorked.add(hours)
            dateIterator = dateIterator.plusDays(1)
        }

        return hoursWorked
    }

    fun hoursWorkedByWeek(id: String): List<Double> {
        val hoursWorkedByDay = hoursWorkedbyDay(id)
        val hoursWorkedByWeek = mutableListOf<Double>()
        var hours = 0.0
        var dateIterator = payPeriodStart
        var index = 0
        while (dateIterator.isBefore(payPeriodEnd)) {
            hours += hoursWorkedByDay[index]
            if (dateIterator.dayOfWeek == DayOfWeek.SUNDAY) {
                hoursWorkedByWeek.add(hours)
                hours = 0.0
            }
            dateIterator = dateIterator.plusDays(1)
            index++
        }
        return hoursWorkedByWeek
    }


    override fun toString(): String {
        return "Pay Period: $payPeriodStart to $payPeriodEnd"
    }
}