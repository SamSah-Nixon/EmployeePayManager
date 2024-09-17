package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.PayStrategySerializer
import org.ryecountryday.samandrhys.epm.util.roundToTwoDecimalPlaces
import org.ryecountryday.samandrhys.epm.util.toMoneyString
import java.time.Instant

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
    abstract fun calculateSalary(payPeriod: Instant, id: String): Double

    /** A name for this pay strategy, for use when serializing. */
    abstract val type: String

    /** Some constant number that is a part of the calculation for how much money is made during a pay period. */
    abstract val rate: Double
    abstract override fun toString(): String

    @Serializable(with = PayStrategySerializer::class)
    class Hourly(hourlyRate: Double) : PayStrategy() {
        override val type = TYPE

        override val rate: Double = hourlyRate.roundToTwoDecimalPlaces()

        constructor(hourlyRate: Int) : this(hourlyRate.toDouble())

        override fun calculateSalary(payPeriod: Instant, id: String): Double {
            val payTime = WorkHistory.entries[payPeriod]
            var pay = 0.0
            for (entry in WorkHistory.entries[payPeriod]!!) {
                if (entry.id == id) pay += entry.durationHours
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

    @Serializable(with = PayStrategySerializer::class)
    class Salaried(annualSalary: Double) : PayStrategy() {
        override val type = TYPE
        override val rate: Double = annualSalary.roundToTwoDecimalPlaces()

        val dailySalary: Double = (annualSalary / 365.0).roundToTwoDecimalPlaces()

        constructor(annualSalary: Int) : this(annualSalary.toDouble())

        override fun calculateSalary(payPeriod: Instant, id: String): Double {

            //TODO (calc salary) val daysWorked = history.flatMap { it.datesWorked }.distinct().count()
            //return (dailySalary * daysWorked).roundToTwoDecimalPlaces()
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