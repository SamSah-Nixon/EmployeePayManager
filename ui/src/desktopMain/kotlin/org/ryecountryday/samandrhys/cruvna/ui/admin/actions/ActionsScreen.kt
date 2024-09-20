package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.ui.CoolTabRow
import org.ryecountryday.samandrhys.cruvna.ui.HistorySettings
import org.ryecountryday.samandrhys.cruvna.ui.Money
import org.ryecountryday.samandrhys.cruvna.ui.Storage

@Composable
fun ActionsScreen(employees: MutableSet<Employee>) {
    CoolTabRow(
        mapOf(
            ("Payroll" to Icons.Filled.Money) to { PayrollScreen(employees) },
            ("Edit Work History" to Icons.Filled.HistorySettings) to { EditScreen() },
            ("Files" to Icons.Filled.Storage) to { FilesScreen() },
        ),
        defaultTab = 1
    )
}