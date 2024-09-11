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
        val history = WorkHistory()
        history.addEntry(WorkEntry(Instant.ofEpochSecond(0), 2)) // start 0 hours, duration 2 hours
        history.addEntry(WorkEntry(Instant.ofEpochSecond(3 * 3600), 2)) // start 3 hours, duration 2 hours
        history.addEntry(WorkEntry(Instant.ofEpochSecond(24 * 3600), 8)) // start 10 hours, duration 8 hours
        history.addEntry(WorkEntry(Instant.ofEpochSecond(50 * 3600), 4.5)) // start 20 hours, duration 4 hours 30 minutes

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
        val history = WorkHistory()
        history.addEntry(WorkEntry(Instant.ofEpochSecond(0), 2)) // start 0 hours, duration 2 hours
        history.addEntry(WorkEntry(Instant.ofEpochSecond(3 * 3600), 2)) // start 3 hours, duration 2 hours
        history.addEntry(WorkEntry(Instant.ofEpochSecond(24 * 3600), 8)) // start 10 hours, duration 8 hours
        history.addEntry(WorkEntry(Instant.ofEpochSecond(50 * 3600), 4.5)) // start 20 hours, duration 4 hours 30 minutes
        history.addEntry(WorkEntry(Instant.ofEpochSecond(100 * 3600), 60)) // start 100 hours, duration 60 hours

        //total worked hours = 2 + 2 + 8 + 4.5 + 60 = 76.5
        //total worked days = 3 (first 2 entries are on the same day)

        // 100k / 365 = $273.97 per day
        // 273.97 * 2 = $547.94
        // 273.97 * 2 = $547.94
        // 273.97 * 8 = $2191.76
        // 273.97 * 4.5 = $1232.86
        // 273.97 * 60 = $16438.20
        // total = $20858.70

        assertEquals(20858.70, salary.calculateSalary(history))
    }
}