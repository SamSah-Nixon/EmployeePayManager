/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.PayStrategySerializer
import org.ryecountryday.samandrhys.epm.util.roundToTwoDecimalPlaces
import org.ryecountryday.samandrhys.epm.util.toMoneyString
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Represents some form of calculating a monetary value based on a number of hours worked.
 */
@Suppress("SERIALIZER_TYPE_INCOMPATIBLE") // it's fine
@Serializable(with = PayStrategySerializer::class)
sealed class PayStrategy {

    /**
     * @return a [Double] representing the amount of money eared for the employee with ID [id]
     * during the last pay period.
     */
    abstract fun calculateSalary(payPeriod: PayPeriod, id: String): Double

    /** A name for this pay strategy, for use when serializing. */
    abstract val type: String

    /** Some constant number that is a part of the calculation for how much money is made during a pay period. */
    abstract val rate: Double
    abstract override fun toString(): String

    fun findHours(payPeriod: PayPeriod, id: String): Double {
        val PPStart : Instant = ZonedDateTime.of(payPeriod.payPeriodStart.atStartOfDay(),ZoneId.systemDefault()).toInstant()
        val PPEnd : Instant = ZonedDateTime.of(payPeriod.payPeriodEnd.atTime(LocalTime.MAX),ZoneId.systemDefault()).toInstant()
        //Find all workEntries that match the employee id and are within the pay period
        val workEntries = WorkHistory.entries.filter {
            it.id == id && ((it.end == null )|| it.end!!.isAfter(PPStart)) && it.start.isBefore(PPEnd)
        }
        var hours = 0.0
        for (workEntry in workEntries) {
            val WEStart: Instant = workEntry.start
            val WEEnd: Instant = workEntry.end ?: Instant.now()
            //This accounts for when working times can be outside the pay period
            hours += if(WEStart.isAfter(PPStart) && WEEnd.isBefore(PPEnd)) workEntry.durationHours
            else if(WEStart.isAfter(PPStart) && WEEnd.isAfter(PPEnd)) Duration.between(WEStart,PPEnd).toHours().toDouble()
            else if(WEStart.isBefore(PPStart) && WEEnd.isBefore(PPEnd)) Duration.between(PPStart,WEEnd).toHours().toDouble()
            else Duration.between(PPStart,PPEnd).toHours().toDouble()
        }
        return hours
    }

    /**
     * Pays the employee based on a rate per hour worked. Overtime can occur in 2 scenarios:
     * - Working more than 40 hours in a week (1.5x rate)
     * - Working more than 9 days in a row (1.5x rate)
     *
     * These both can occur at the same time, in which case the rate is 2.25x the base rate.
     *
     * @param hourlyRate the rate per hour worked
     */
    @Serializable(with = PayStrategySerializer::class)
    class Hourly(hourlyRate: Number) : PayStrategy() {
        override val type = TYPE

        override val rate: Double = hourlyRate.toDouble().roundToTwoDecimalPlaces()

        override fun calculateSalary(payPeriod: PayPeriod, id: String): Double {
            var hours = findHours(payPeriod, id)
            //Overtime 40+ hours
            if(hours > 40.0) hours = (40.0 * rate) + ((hours - 40.0) * rate * 1.5)
            else hours *= rate
            //TODO Overtime 9 days in a row


            return hours
        }

        override fun toString(): String {
            return "$type ($${rate.toMoneyString()}/hour)"
        }

        companion object {
            const val TYPE = "Hourly"
        }
    }

    /**
     * Pays the employee based on a fixed annual salary, which is divided by 365 to get a daily rate.
     * If a salaried employee does not work at least 40 hours in a 1-week period, they lose 1 day's pay
     * for every 8 hours they are short of 40.
     */
    @Serializable(with = PayStrategySerializer::class)
    class Salaried(annualSalary: Number) : PayStrategy() {
        override val type = TYPE
        override val rate: Double = annualSalary.toDouble().roundToTwoDecimalPlaces()

        val dailySalary: Double = (annualSalary.toDouble() / 365.0).roundToTwoDecimalPlaces()

        override fun calculateSalary(payPeriod: PayPeriod, id: String): Double {
            val hours = findHours(payPeriod, id)
            val days = payPeriod.daysInPeriod
            //TODO Lazy people pay deduction. Does >=40 hours in a week mean mon-sun or 7 days from the start of the pay period???
            return dailySalary * days
        }

        override fun toString(): String {
            return "$type ($${rate.toMoneyString()}/year)"
        }

        companion object {
            const val TYPE = "Salaried"
        }
    }
}