package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.min
import kotlin.math.round

abstract class PayStrategy {
    abstract fun calculateSalary(history: WorkHistory): Double

    class Hourly(private val hourlyRate: Double) : PayStrategy() {
        override fun calculateSalary(history: WorkHistory): Double {
            var moneyMade = 0.0
            var totalWorkedHours = 0.0
            val totalWorkedDays = mutableSetOf<Instant>()
            var consecutiveDays = 0
            var lastWorkedDate: Instant? = null

            for (entry in history.getEntries()) {
                val entryDurationInHours = (entry.durationSeconds / 3600.0)

                var hoursWorkedInEntry = 0.0
                var currentStart = entry.start

                // Check if entry is split by thresholds
                while (hoursWorkedInEntry < entryDurationInHours) {
                    val remainingHoursInEntry = entryDurationInHours - hoursWorkedInEntry
                    val hoursTillOvertime = maxOf(0.0, 40.0 - totalWorkedHours)
                    val hoursTill9Days = if (consecutiveDays >= 9) remainingHoursInEntry else hoursLeftInDay(currentStart)

                    // Determine how many hours to process at normal rate or with a multiplier
                    val hoursToProcess = minOf(remainingHoursInEntry, hoursTillOvertime, hoursTill9Days)

                    // Check for consecutive workdays
                    if (lastWorkedDate != null && isNextDay(lastWorkedDate, currentStart)) {
                        consecutiveDays++
                    } else {
                        consecutiveDays = 1
                    }
                    lastWorkedDate = currentStart.plusHours(hoursToProcess)

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
                    currentStart = currentStart.plusHours(hoursToProcess)

                    // Track all the unique worked days
                    totalWorkedDays.addAll(entry.dates)
                }
            }

            return round(moneyMade * 100) * 0.01
        }

        private fun isNextDay(previous: Instant, current: Instant): Boolean {
            return previous.plusSeconds(86400).isAfter(current)
        }

        private fun hoursLeftInDay(instant: Instant): Double {
            val zoneId = ZoneId.systemDefault()
            // Convert the given instant to a ZonedDateTime in the specified time zone
            val zonedDateTime = instant.atZone(zoneId)

            // Get the end of the current day (23:59:59.999)
            val endOfDay = zonedDateTime.toLocalDate().atTime(23, 59, 59, 999_999_999)
                .atZone(zoneId)

            // Calculate the difference in hours between the current time and the end of the day
            val hoursUntilEndOfDay = ChronoUnit.MILLIS.between(zonedDateTime, endOfDay) / 3600000.0

            return hoursUntilEndOfDay
        }
    }

    class Salaried(annualSalary: Double) : PayStrategy() {
        private val dailySalary: Double = annualSalary / 365.0

        override fun calculateSalary(history: WorkHistory): Double {
            var totalPay = 0.0
            var totalWorkedHours = 0.0
            var currentWeekStart: Instant? = null
            var hoursInWeek = 0.0
            var workedDaysInWeek = 0

            for (entry in history.getEntries().sortedBy { it.start }) {
                val entryDurationInHours = (entry.durationSeconds / 3600000.0) // Convert milliseconds to hours
                totalWorkedHours += entryDurationInHours

                val entryDates = entry.dates.sorted()
                for (date in entryDates) {
                    if (currentWeekStart == null) {
                        currentWeekStart = date
                    }

                    // Calculate weekly hours and apply penalty if a week is over
                    if (date.isAfter(currentWeekStart.plusDays(7))) {
                        totalPay += calculateWeeklyPay(hoursInWeek, workedDaysInWeek)
                        hoursInWeek = 0.0
                        workedDaysInWeek = 0
                        currentWeekStart = date
                    }

                    hoursInWeek += min(24.0, entryDurationInHours) // Add hours for this day (max 24h/day)
                    workedDaysInWeek++
                }
            }

            // Add final week pay
            totalPay += calculateWeeklyPay(hoursInWeek, workedDaysInWeek)

            return totalPay
        }

        private fun calculateWeeklyPay(hoursInWeek: Double, workedDays: Int): Double {
            var daysToPay = workedDays

            if (hoursInWeek < 40.0) {
                val hoursUnder = 40.0 - hoursInWeek
                daysToPay -= (hoursUnder / 8.0).toInt() // Lose 1 day of pay for every 8 hours under
            }

            return daysToPay * dailySalary
        }
    }

    protected fun Instant.plusDays(days: Long): Instant {
        return this.plusSeconds(days * 86400)
    }

    protected fun Instant.plusHours(hours: Double): Instant {
        return this.plusSeconds((hours * 3600).toLong())
    }
}