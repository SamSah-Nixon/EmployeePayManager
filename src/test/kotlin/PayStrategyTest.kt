import org.junit.jupiter.api.Assertions.*
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import java.time.Instant
import kotlin.test.Test

class PayStrategyTest {
    @Test
    fun `test hourly salary`() {
        val salary = PayStrategy.Hourly(20.0) // 20 bucks per hour
        val history = WorkHistory().apply {
            add(WorkEntry(Instant.ofEpochSecond(0), 2)) // start 0 hours, duration 2 hours
            add(WorkEntry(Instant.ofEpochSecond(3 * 3600), 2)) // start 3 hours, duration 2 hours
            add(WorkEntry(Instant.ofEpochSecond(24 * 3600), 8)) // start 10 hours, duration 8 hours
            add(WorkEntry(Instant.ofEpochSecond(50 * 3600), 4.5)) // start 20 hours, duration 4 hours 30 minutes
        }

        //total worked hours = 2 + 2 + 8 + 4.5 = 16.5
        //total worked days = 3 (first 2 entries are on the same day)

        // 2 hours * 20 = $40
        // 2 hours * 20 = $40
        // 8 hours * 20 = $160
        // 4.5 hours * 20 = $90
        // total = $330

        assertEquals(330.0, salary.calculateSalary(history))
    }

    @Test
    fun `test yearly salary`() {
        val salary = PayStrategy.Salaried(100000.0) // 100k per year
        val history = WorkHistory().apply {
            add(WorkEntry(Instant.ofEpochSecond(0), 2)) // start 0 hours, duration 2 hours
            add(WorkEntry(Instant.ofEpochSecond(3 * 3600), 2)) // start 3 hours, duration 2 hours
            add(WorkEntry(Instant.ofEpochSecond(24 * 3600), 8)) // start 10 hours, duration 8 hours
            add(WorkEntry(Instant.ofEpochSecond(50 * 3600), 4.5)) // start 20 hours, duration 4 hours 30 minutes
            add(WorkEntry(Instant.ofEpochSecond(100 * 3600), 60)) // start 100 hours, duration 60 hours
        }

        // 100k / 365 = $273.97 per day
        assertEquals(273.97, salary.dailySalary)

        // total worked days = 6
        // first 2 on same day, 3rd on the next day, 4th on the next day, 5th on the next 2 days
        assertEquals(6, history.flatMap { it.datesWorked }.distinct().count())

        // 293.97 * 6 = $1643.82
        assertEquals(1643.82, salary.calculateSalary(history))
    }
}