package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import kotlin.math.round

abstract class PayStrategy {
    abstract fun calculateSalary(history: WorkHistory): Double

    class Hourly(private val hourlyRate: Double) : PayStrategy() {
        override fun calculateSalary(history: WorkHistory): Double {
            val hoursWorked = history.sumOf { it.durationHours }
            return (hourlyRate * hoursWorked).roundToTwoDecimalPlaces()
        }
    }

    class Salaried(annualSalary: Double) : PayStrategy() {
        val dailySalary: Double = (annualSalary / 365.0).roundToTwoDecimalPlaces()

        override fun calculateSalary(history: WorkHistory): Double {
            val daysWorked = history.flatMap { it.datesWorked }.distinct().count()
            return (dailySalary * daysWorked).roundToTwoDecimalPlaces()
        }
    }

    protected fun Double.roundToTwoDecimalPlaces(): Double {
        return round(this * 100) / 100
    }
}