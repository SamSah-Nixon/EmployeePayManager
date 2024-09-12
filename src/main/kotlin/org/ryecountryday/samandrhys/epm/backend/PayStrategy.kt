package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import kotlin.math.round

abstract class PayStrategy {
    abstract fun calculateSalary(history: WorkHistory): Double

    class Hourly(hourlyRate: Double) : PayStrategy() {
        private val hourlyRate: Double = hourlyRate.roundToTwoDecimalPlaces()

        constructor(hourlyRate: Int) : this(hourlyRate.toDouble())

        override fun calculateSalary(history: WorkHistory): Double {
            //val hoursWorked = history.sumOf { it.durationHours }
            return (hourlyRate * hoursWorked).roundToTwoDecimalPlaces()
        }

        override fun toString(): String {
            return "Hourly ($${hourlyRate.toMoneyString()}/hour)"
        }
    }

    class Salaried(val annualSalary: Double) : PayStrategy() {
        val dailySalary: Double = (annualSalary / 365.0).roundToTwoDecimalPlaces()

        constructor(annualSalary: Int) : this(annualSalary.toDouble())

        override fun calculateSalary(history: WorkHistory): Double {
            val daysWorked = history.flatMap { it.datesWorked }.distinct().count()
            return (dailySalary * daysWorked).roundToTwoDecimalPlaces()
        }

        override fun toString(): String {
            return "Salaried ($${annualSalary.toMoneyString()}/year)"
        }
    }

    protected fun Double.roundToTwoDecimalPlaces(): Double {
        return round(this * 100) / 100
    }

    protected fun Double.toMoneyString(): String {
        var result = this.roundToTwoDecimalPlaces().toString()
        if(result.substringAfter('.').length == 1) {
            result += "0"
        }
        return result
    }
}