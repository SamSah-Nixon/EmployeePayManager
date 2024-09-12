import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {
    @Test
    fun `work entry serialization`() {
        val workEntry = WorkEntry(Instant.ofEpochSecond(0), 2)

        val json = Json.encodeToString(workEntry)

        val expected = """{"start":0,"end":7200}"""

        assertEquals(expected, json)
    }
}