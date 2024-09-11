package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
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
        val employees = remember {
            EmployeeContainer().apply {
                addEmployee(Employee(
                    name = "Admin",
                    id = "000100",
                    salary = PayStrategy.Hourly(50.0),
                    birthday = parseDate("01/01/2000"),
                    address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
                ))
                addEmployee(Employee(
                    name = "Sam",
                    id = "abF31",
                    salary = PayStrategy.Salaried(100000.0),
                    birthday = parseDate("12/06/2006"),
                    address = Address("123 Main St", "Rye", "New York", "10580"),
                ))

                for(i in 0..100) {
                    addEmployee(Employee(
                        name = "Employee $i",
                        id = "id$i",
                        salary = PayStrategy.Hourly(Math.random() * 100),
                        birthday = parseDate("${i % 12 + 1}/${i % 28 + 1}/${2000 + i % 20}"),
                        address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
                    ))
                }
            }
        }
        val openAddDialog = remember {mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(employees.size) { index ->
                EmployeeCard(employees.getAllEmployees()[index])
            }
        }

        Button(onClick = { openAddDialog.value = true }) {
            Text("Add Employee")
        }

        if (openAddDialog.value) {
            AddEmployeeDialog(openAddDialog, employees)
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
fun AddEmployeeDialog(value: MutableState<Boolean>?, employees: EmployeeContainer) {
    Dialog(onDismissRequest = { value?.value = false }) {
        Card {
            Column {
                var name: String by remember { mutableStateOf("") }
                var id: String by remember { mutableStateOf("") }

                TextField(value = name, singleLine = true, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = id, singleLine = true, onValueChange = { id = it }, label = { Text("ID") })
                // Dropdown item for salary - hourly or salaried
                // spinner/textfield for rate
                // date picker for birthday
            }
        }
    }
}