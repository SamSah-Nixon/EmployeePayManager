package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import java.util.*

abstract class Salary {
    abstract fun calculateSalary(history: WorkHistory): Double

    class Hourly(private val hourlyRate: Double) : Salary() {
        override fun calculateSalary(history: WorkHistory): Double {
            var moneyMade = 0.0
            var totalWorkedHours = 0
            val totalWorkedDays = mutableSetOf<Date>()

            for(entry in history.getEntries()) {
                // if worked for >40 hours, pay 1.5x
                // if worked for >=9 days in a row, pay 1.5x
                // if both, 2.25x

                val entryHours = (entry.duration / 3600000).toInt()

                val numOvertimeHours = if (totalWorkedHours + entryHours > 40) {
                    totalWorkedHours + entryHours - 40
                } else {
                    0
                }

                val numOvertimeDays = if (totalWorkedDays.size + entry.dates.size >= 9) {
                    totalWorkedDays.size + entry.dates.size - 9
                } else {
                    0
                }

                totalWorkedDays.addAll(entry.dates)
                totalWorkedHours += entryHours
            }

            return moneyMade
        }
    }

    class Salaried(private val annualSalary: Double) : Salary() {
        override fun calculateSalary(history: WorkHistory): Double {
            return 0.0
        }
    }
}