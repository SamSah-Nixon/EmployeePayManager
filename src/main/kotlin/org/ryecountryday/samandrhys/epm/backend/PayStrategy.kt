/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.PayStrategySerializer
import org.ryecountryday.samandrhys.epm.util.roundToTwoDecimalPlaces
import org.ryecountryday.samandrhys.epm.util.toMoneyString
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
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
            var pay: Double = 0.0
            var daysInRow = 0
            var hoursPerWeek = payPeriod.hoursWorkedByWeek(id)
            var hoursPerDay = payPeriod.hoursWorkedbyDay(id)

            for(day in hoursPerDay){

                if(day > 0.0) daysInRow++
                else daysInRow = 0

                //Overtime 9 days in a row
                if(daysInRow > 9) pay += day*1.5
                pay += day
            }

            //Overtime 40+ hours a week
            for (hours in hoursPerWeek){
                if(hours > 40) pay += (hours-40)*0.5
            }

            return pay
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
            var pay: Double = rate * payPeriod.daysInPeriod / 365.0
            var hoursPerWeek = payPeriod.hoursWorkedByWeek(id)

            //Deduction for under 40h a week
            for (hours in hoursPerWeek){
                if(hours < 40) {
                    pay -= (((40-hours).toInt()/8)+1)*dailySalary
                }
            }
            return pay
        }

        override fun toString(): String {
            return "$type ($${rate.toMoneyString()}/year)"
        }

        companion object {
            const val TYPE = "Salaried"
        }
    }
}