import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.epm.util.parseDate
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {
    @Test
    fun `work entry serialization`() {
        val workEntry = WorkEntry(Instant.ofEpochSecond(0), 2.0, "id")

        val json = Json {
            prettyPrint = true
        }

        val serialized = json.encodeToString(workEntry)

        val expected = """
            {
                "start": 0,
                "end": 7200,
                "id": "id"
            }
        """.trimIndent()

        assertEquals(expected, serialized)
    }

    @Test
    fun `work entry with no end`() {
        val workEntry = WorkEntry(Instant.ofEpochSecond(0), "id")

        val json = Json {
            prettyPrint = true
        }

        val serialized = json.encodeToString(workEntry)

        val expected = """
            {
                "start": 0,
                "id": "id"
            }
        """.trimIndent()

        assertEquals(expected, serialized)
    }

    @Test
    fun `pay strategies`() {
        val hourly = PayStrategy.Hourly(10.0)
        val salaried = PayStrategy.Salaried(100000.0)

        val json = Json {
            prettyPrint = true
        }

        val serializedHourly = json.encodeToString(hourly)
        val serializedSalaried = json.encodeToString(salaried)

        val expectedHourly = """
            {
                "type": "Hourly",
                "rate": 10.0
            }
        """.trimIndent()

        val expectedSalaried = """
            {
                "type": "Salaried",
                "rate": 100000.0
            }
        """.trimIndent()

        assertEquals(expectedHourly, serializedHourly)
        assertEquals(expectedSalaried, serializedSalaried)
    }

    @Test
    fun employees() {
        val employee = Employee("Sam Sah-Nixon", "id", PayStrategy.Hourly(10.0), parseDate("1/1/2000"), Address("123 Main St", "Anytown", "USA", "12345"))

        val json = Json {
            prettyPrint = true
        }

        val serialized = json.encodeToString(employee)

        println(employee.dateOfBirth)

        val expected = """
            {
                "lastName": "Sah-Nixon",
                "firstName": "Sam",
                "id": "id",
                "pay": {
                    "type": "Hourly",
                    "rate": 10.0
                },
                "dateOfBirth": "01/01/2000",
                "address": {
                    "street": "123 Main St",
                    "city": "Anytown",
                    "state": "USA",
                    "zip": "12345"
                }
            }
        """.trimIndent()

        assertEquals(expected, serialized)
    }
}