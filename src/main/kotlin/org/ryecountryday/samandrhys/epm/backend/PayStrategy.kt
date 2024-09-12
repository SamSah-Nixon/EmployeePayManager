package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.PayStrategySerializer
import java.time.Instant
import kotlin.math.round

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE") // it's fine
@Serializable(with = PayStrategySerializer::class)
sealed class PayStrategy {
    abstract fun calculateSalary(payPeriod : Int): Double

    abstract val type: String
    abstract val rate: Double
    abstract override fun toString(): String

    @Serializable(with = PayStrategySerializer::class)
    class Hourly(hourlyRate: Double) : PayStrategy() {
        override val type = "Hourly"
        val hourlyRate: Double = hourlyRate.roundToTwoDecimalPlaces()

        override val rate: Double
            get() = hourlyRate

        constructor(hourlyRate: Int) : this(hourlyRate.toDouble())

        override fun calculateSalary(payPeriod: Int): Double {
            val payTime : Instant = WorkHistory.payPeriod.get(payPeriod)

            //TODO (calc salary) return (hourlyRate * ).roundToTwoDecimalPlaces()
            return 0.0
        }

        override fun toString(): String {
            return "Hourly ($${hourlyRate.toMoneyString()}/hour)"
        }
    }

    @Serializable(with = PayStrategySerializer::class)
    class Salaried(val annualSalary: Double) : PayStrategy() {
        override val type = "Salaried"
        val dailySalary: Double = (annualSalary / 365.0).roundToTwoDecimalPlaces()

        override val rate: Double
            get() = annualSalary

        constructor(annualSalary: Int) : this(annualSalary.toDouble())

        override fun calculateSalary(payPeriod: Int): Double {
            val payTime : Instant = WorkHistory.payPeriod.get(payPeriod)
            //TODO (calc salary) val daysWorked = history.flatMap { it.datesWorked }.distinct().count()
            //return (dailySalary * daysWorked).roundToTwoDecimalPlaces()
            return 0.0
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