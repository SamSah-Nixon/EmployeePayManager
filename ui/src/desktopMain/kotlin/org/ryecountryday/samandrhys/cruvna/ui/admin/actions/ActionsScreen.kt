package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.runtime.Composable
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.ui.CoolTabRow

@Composable
fun ActionsScreen(employees: MutableSet<Employee>) {
    CoolTabRow(
        mapOf(
            "Payroll" to { PayrollScreen(employees) },
            "Edit Work History" to { EditScreen(employees) },
            "Files" to { FilesScreen() },
        ),
        defaultTab = 1
    )
}