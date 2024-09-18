/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
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
                "type": "hourly",
                "rate": 10.0
            }
        """.trimIndent()

        val expectedSalaried = """
            {
                "type": "salaried",
                "rate": 100000.0
            }
        """.trimIndent()

        assertEquals(expectedHourly, serializedHourly)
        assertEquals(expectedSalaried, serializedSalaried)
    }

    @Test
    fun employees() {
        val employee = Employee(
            name = "Sam Sah-Nixon",
            id = "id",
            pay = PayStrategy.Hourly(10.0),
            dateOfBirth = parseDate("1/1/2000"),
            address = Address("123 Main St", "Anytown", "USA", "12345")
        )

        val json = Json {
            prettyPrint = true
        }

        val serialized = json.encodeToString(employee)

        val expected = """
            {
                "lastName": "Sah-Nixon",
                "firstName": "Sam",
                "id": "id",
                "pay": {
                    "type": "hourly",
                    "rate": 10.0
                },
                "dateOfBirth": "1/1/2000",
                "address": {
                    "street": "123 Main St",
                    "city": "Anytown",
                    "state": "USA",
                    "zip": "12345"
                },
                "active": true
            }
        """.trimIndent()

        assertEquals(expected, serialized)
    }

    @Test
    fun `employee container`() {
        val c = EmployeeContainer(
            Employee(
                name = "Mr. Cruté",
                id = "000100",
                pay = PayStrategy.Hourly(0),
                dateOfBirth = parseDate("01/01/2000"),
                address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
            ),
            Employee(
                name = "Jaymin Ding",
                id = "4",
                pay = PayStrategy.Hourly(13),
                dateOfBirth = parseDate("9/23/2007"),
                address = Address("3 Cedar Street", "Rye", "New York", "10580"),
                active = false
            )
        )

        val json = Json {
            prettyPrint = true
        }

        val serialized = json.encodeToString(c)

        val expected = """
            {
                "employees": [
                    {
                        "lastName": "Cruté",
                        "firstName": "Mr.",
                        "id": "000100",
                        "pay": {
                            "type": "hourly",
                            "rate": 0.0
                        },
                        "dateOfBirth": "1/1/2000",
                        "address": {
                            "street": "3 Five Cedar",
                            "city": "Rye Land",
                            "state": "New York",
                            "zip": "11122-1111"
                        },
                        "active": true
                    },
                    {
                        "lastName": "Ding",
                        "firstName": "Jaymin",
                        "id": "4",
                        "pay": {
                            "type": "hourly",
                            "rate": 13.0
                        },
                        "dateOfBirth": "9/23/2007",
                        "address": {
                            "street": "3 Cedar Street",
                            "city": "Rye",
                            "state": "New York",
                            "zip": "10580"
                        },
                        "active": false
                    }
                ]
            }
        """.trimIndent()

        assertEquals(expected, serialized)
    }
}