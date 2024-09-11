package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.util.parseDate

@Composable
@Preview
fun App() {
    MaterialTheme {
        val employees = remember { EmployeeContainer() }
        var openAddDialog by remember { mutableStateOf(false) }

        employees.addEmployee(Employee(
                name = "Admin",
                id = "000100",
                salary = PayStrategy.Hourly(50.0),
                birthday = parseDate("01/01/2000"),
                address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
        ))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (employee in employees) {
                EmployeeCard(employee)
            }
        }

        Button(onClick = { openAddDialog = !openAddDialog }) {
            Text("Add Employee")
        }

        if (openAddDialog) {
            AddEmployeeDialog()
        }
    }
}

@Composable
fun EmployeeCard(employee: Employee) {
    val border = Modifier.border(BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)).padding(8.dp)
    Column(modifier = border) {
        Text("Name: ${employee.name}")
        Text("ID: ${employee.id}")
        Text("Salary: ${employee.salary}")
        Text("DOB: ${employee.birthday}")
        Text("Address: ${employee.address}")
    }
}

@Composable
fun AddEmployeeDialog() {
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add Employee")
            Button(onClick = { }) {
                Text("Add")
            }
        }
    }
}