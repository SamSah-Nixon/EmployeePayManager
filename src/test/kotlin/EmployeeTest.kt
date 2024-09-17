/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

import org.junit.jupiter.api.Assertions.*
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.util.parseDate
import kotlin.test.Test

class EmployeeTest {
    @Test
    fun `test employee creation and name splitting`() {
        val e = Employee(
            name = "Mr. Cruté",
            id = "000100",
            pay = PayStrategy.Hourly(0),
            dateOfBirth = parseDate("01/01/2000"),
            address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
        )

        assertEquals("Mr.", e.firstName)
        assertEquals("Cruté", e.lastName)

        val e2 = Employee(
            name = "Rhys de Haan",
            id = "a",
            pay = PayStrategy.Salaried(999999999),
            dateOfBirth = parseDate("11/04/2007"),
            address = Address("a", "a", "a", "a"),
        )

        assertEquals("Rhys", e2.firstName)
        assertEquals("de Haan", e2.lastName)
    }
}