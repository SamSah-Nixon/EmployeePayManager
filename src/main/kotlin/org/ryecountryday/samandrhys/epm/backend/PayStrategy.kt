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

    //Hourly pay strategy
    @Serializable(with = PayStrategySerializer::class)
    class Hourly(hourlyRate: Double) : PayStrategy() {
        override val type = TYPE

        override val rate: Double = hourlyRate.roundToTwoDecimalPlaces()

        constructor(hourlyRate: Int) : this(hourlyRate.toDouble())
        override fun calculateSalary(payPeriod: PayPeriod, id: String): Double {
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
            //Overtime 40+ hours
            if(hours > 40.0) hours =  (40.0 * rate) + ((hours - 40.0) * rate * 1.5)
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

    //Salaried pay strategy
    @Serializable(with = PayStrategySerializer::class)
    class Salaried(annualSalary: Double) : PayStrategy() {
        override val type = TYPE
        override val rate: Double = annualSalary.roundToTwoDecimalPlaces()

        val dailySalary: Double = (annualSalary / 365.0).roundToTwoDecimalPlaces()

        constructor(annualSalary: Int) : this(annualSalary.toDouble())

        override fun calculateSalary(payPeriod: PayPeriod, id: String): Double {

            return 0.0
        }

        override fun toString(): String {
            return "$type ($${rate.toMoneyString()}/year)"
        }

        companion object {
            const val TYPE = "Salaried"
        }
    }
}