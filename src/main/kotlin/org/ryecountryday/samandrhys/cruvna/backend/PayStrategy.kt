/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.cruvna.util.PayStrategySerializer
import org.ryecountryday.samandrhys.cruvna.util.roundToTwoDecimalPlaces
import org.ryecountryday.samandrhys.cruvna.util.toMoneyString


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
            var pay = 0.0
            var daysInRow = 0
            val hoursPerWeek = payPeriod.hoursWorkedByWeek(id)
            val hoursPerDay = payPeriod.hoursWorkedbyDay(id)
            // Overtime 9 days in a row
            for(hours in hoursPerDay){
                if(hours > 0.0) daysInRow++
                else daysInRow = 0

                if(daysInRow > 9) pay += hours*1.5
                pay += hours
            }

            // Overtime 40+ hours a week
            for (hours in hoursPerWeek){
                if(hours > 40) pay += (hours-40)*0.5
            }

            return pay * rate
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

        private val dailySalary: Double = (annualSalary.toDouble() / 365.0).roundToTwoDecimalPlaces()

        override fun calculateSalary(payPeriod: PayPeriod, id: String): Double {
            var days = payPeriod.daysInPeriod
            val hoursPerWeek = payPeriod.hoursWorkedByWeek(id)
            //Deduction for under 40h a week
            for (hours in hoursPerWeek){
                println("Id $id Hours: $hours")
                if(hours < 40) {
                    val numHoursUnder = (40 - hours).toInt()
                    val deductions = (numHoursUnder / 8)
                    days -= (deductions + 1)
                }
            }
            return days * dailySalary
        }

        override fun toString(): String {
            return "$type ($${rate.toMoneyString()}/year)"
        }

        companion object {
            const val TYPE = "Salaried"
        }
    }
}