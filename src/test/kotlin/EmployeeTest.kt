import org.junit.jupiter.api.Assertions.*
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.epm.util.parseDate
import java.time.Instant
import kotlin.test.Test

class EmployeeTest {
    @Test
    fun `test employee creation and salary`() {
        val e = Employee(
            name = "Admin",
            id = "000100",
            pay = PayStrategy.Hourly(50.0),
            birthday = parseDate("01/01/2000"),
            address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
        )

        e.workHistory.add(WorkEntry(Instant.parse("2021-01-01T09:00:00Z"), 8.0)) // 9-5
        // 8 hours * 50 = $400
        assertEquals(400.0, e.pay.calculateSalary(e.workHistory))
    }
}