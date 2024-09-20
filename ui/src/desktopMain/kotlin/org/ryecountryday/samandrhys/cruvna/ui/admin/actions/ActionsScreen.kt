package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.material.Text
import androidx.compose.runtime.*
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.ui.CoolTabRow

@Composable
fun ActionsScreen(employees: MutableSet<Employee>) {
    CoolTabRow(
        mapOf(
            "Payroll" to { PayrollScreen(employees) },
            "Edit Work History" to { EditScreen(employees) },
            "Save/Load" to { SaveLoadScreen() },
        ),
        defaultTab = 1
    )
}

@Composable
private fun PayrollScreen(employees: MutableSet<Employee>) {
    Text("TODO")
}