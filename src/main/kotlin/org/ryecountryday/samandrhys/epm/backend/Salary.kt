package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import java.util.*

abstract class Salary {
    abstract fun calculateSalary(history: WorkHistory): Double

    class Hourly(private val hourlyRate: Double) : Salary() {
        override fun calculateSalary(history: WorkHistory): Double {
            var moneyMade = 0.0
            var totalWorkedHours = 0.0
            val totalWorkedDays = mutableSetOf<Date>()
            var consecutiveDays = 0
            var lastWorkedDate: Date? = null

            for (entry in history.getEntries()) {
                val entryDurationInHours = (entry.duration / 3600000.0) // Convert milliseconds to hours

                var hoursWorkedInEntry = 0.0
                var currentStart = entry.start

                // Check if entry is split by thresholds
                while (hoursWorkedInEntry < entryDurationInHours) {
                    val remainingHoursInEntry = entryDurationInHours - hoursWorkedInEntry
                    val hoursTillOvertime = maxOf(0.0, 40.0 - totalWorkedHours)
                    val hoursTill9Days = if (consecutiveDays >= 9) remainingHoursInEntry else hoursInDay(currentStart)

                    // Determine how many hours to process at normal rate or with a multiplier
                    val hoursToProcess = minOf(remainingHoursInEntry, hoursTillOvertime, hoursTill9Days)

                    // Check for consecutive workdays
                    if (lastWorkedDate != null && isNextDay(lastWorkedDate, currentStart)) {
                        consecutiveDays++
                    } else {
                        consecutiveDays = 1
                    }
                    lastWorkedDate = Date(currentStart.time + (hoursToProcess * 3600000).toLong())

                    var multiplier = 1.0
                    if (totalWorkedHours + hoursToProcess > 40) {
                        multiplier *= 1.5
                    }
                    if (consecutiveDays >= 9) {
                        multiplier *= 1.5
                    }

                    // Calculate pay for the hours to process in this iteration
                    moneyMade += hoursToProcess * hourlyRate * multiplier
                    totalWorkedHours += hoursToProcess
                    hoursWorkedInEntry += hoursToProcess
                    currentStart = Date(currentStart.time + (hoursToProcess * 3600000).toLong())

                    // Track all the unique worked days
                    totalWorkedDays.addAll(entry.dates)
                }
            }

            return moneyMade
        }

        private fun isNextDay(previous: Date, current: Date): Boolean {
            val prevCal = Calendar.getInstance().apply { time = previous }
            val currCal = Calendar.getInstance().apply { time = current }
            prevCal.add(Calendar.DATE, 1)
            return prevCal.get(Calendar.YEAR) == currCal.get(Calendar.YEAR) &&
                    prevCal.get(Calendar.DAY_OF_YEAR) == currCal.get(Calendar.DAY_OF_YEAR)
        }

        private fun hoursInDay(date: Date): Double {
            val cal = Calendar.getInstance().apply { time = date }
            val endOfDay = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }
            return (endOfDay.timeInMillis - cal.timeInMillis) / 3600000.0
        }
    }

    class Salaried(private val annualSalary: Double) : Salary() {
        override fun calculateSalary(history: WorkHistory): Double {
            return 0.0 // Salaried employees typically have a fixed pay, can be adjusted if needed
        }
    }
}