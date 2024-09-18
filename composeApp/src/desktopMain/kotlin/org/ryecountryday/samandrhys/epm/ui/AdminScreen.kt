package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory

/**
 * Main screen for viewing all employees and their status.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AdminScreen(employees: MutableSet<Employee>) {
    Column(modifier = Modifier.fillMaxSize()) {
        var tab by remember { mutableStateOf(0) }
        val tabs = listOf("All Employees", "Clocked In Employees")
        SecondaryTabRow(selectedTabIndex = tab, containerColor = MaterialTheme.colors.background) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = MaterialTheme.colors.onBackground) },
                    selected = tab == index,
                    onClick = { tab = index }
                )
            }
        }

        Box { // use Box to reset the alignments in the Column
            when (tab) {
                0 -> EmployeeList(employees)
                1 -> EmployeeList(employees.filter { WorkHistory.isClockedIn(it.id) }.toMutableSet(), false)
            }
        }
    }
}