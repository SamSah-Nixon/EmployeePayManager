/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.cruvna.util.PayPeriodSerializer
import org.ryecountryday.samandrhys.cruvna.util.toInstant
import org.ryecountryday.samandrhys.cruvna.util.toLocalDate
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

/**
 * Represents a pay period, which contains all the work entries for a given period of time.
 */
@Serializable(with = PayPeriodSerializer::class)
class PayPeriod(var payPeriodStart: LocalDate, var payPeriodEnd: LocalDate, var workEntries: MutableSet<WorkEntry>) {
    var daysInPeriod = payPeriodEnd.toEpochDay() - payPeriodStart.toEpochDay()

    fun hoursWorked(id: String): Double {
        return workEntries.filter { it.id == id }.sumOf { it.durationHours }
    }

    /**
     * Find amount of hours worked for a specific employee in the pay period separated by day
     */
    fun hoursWorkedbyDay(id: String): List<Double> {
        val PPStart : Instant = payPeriodStart.toInstant()
        val PPEnd : Instant = payPeriodEnd.atTime(LocalTime.MAX).toInstant()
        //Find all workEntries that match the employee id and are within the pay period
        val workEntries = workEntries.filter {
            it.id == id && (it.end?.isAfter(PPStart) ?: true) && it.start.isBefore(PPEnd)
        }

        val hoursWorked: MutableList<Double> = mutableListOf()
        var dateIterator: LocalDate = payPeriodStart
        var hours: Double

        while (!dateIterator.isAfter(payPeriodEnd)) {
            hours = 0.0
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

    /**
     * Find amount of hours worked for a specific employee in the pay period separated by week
     * If the pay period is less than a week, the hours worked will be returned in a single element list
     */
    fun hoursWorkedByWeek(id: String): List<Double> {
        val hoursWorkedByDay = hoursWorkedbyDay(id)
        val hoursWorkedByWeek = mutableListOf<Double>()
        var hours = 0.0
        var dateIterator = payPeriodStart
        var index = 0
        while (hoursWorkedByDay.size > index) {
            hours += hoursWorkedByDay[index]
            if (dateIterator.dayOfWeek == DayOfWeek.SUNDAY) {
                hoursWorkedByWeek.add(hours)
                hours = 0.0
            }
            index++
            dateIterator = dateIterator.plusDays(1)
        }
        hoursWorkedByWeek.add(hours)
        return hoursWorkedByWeek
    }


    override fun toString(): String {
        return "Pay Period: $payPeriodStart to $payPeriodEnd"
    }
}