@file:OptIn(ExperimentalMaterial3Api::class)
package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.util.dateToString
import org.ryecountryday.samandrhys.epm.util.parseDate
import java.util.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        val employees = remember {
            EmployeeContainer().apply {
                add(Employee(
                    name = "Admin",
                    id = "000100",
                    pay = PayStrategy.Hourly(50.0),
                    birthday = parseDate("01/01/2000"),
                    address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
                ))
                add(Employee(
                    name = "Sam",
                    id = "abF31",
                    pay = PayStrategy.Salaried(100000.0),
                    birthday = parseDate("12/06/2006"),
                    address = Address("123 Main St", "Rye", "New York", "10580"),
                ))

                for(i in 0..100) {
                    add(Employee(
                        name = "Employee $i",
                        id = "id$i",
                        pay = PayStrategy.Hourly(Math.random() * 100),
                        birthday = parseDate("${i % 12 + 1}/${i % 28 + 1}/${2000 + i % 20}"),
                        address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
                    ))
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val itr = employees.iterator()
            items(employees.size) {
                EmployeeCard(itr.next())
            }
        }

        val openAddDialog: MutableState<Boolean?> = remember { mutableStateOf(false) }
        FloatingActionButton(onClick = { openAddDialog.value = true }) {
            Icon(Icons.Filled.Add, contentDescription = "Add Employee")
        }

        if(openAddDialog.value == true) {
            AddEmployeeDialog(openAddDialog, employees)
        } else if(openAddDialog.value == null) {
            Dialog(onDismissRequest = { openAddDialog.value = false }) {
                Card(modifier = Modifier.fillMaxHeight()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Employee with that ID already exists", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                        Button(onClick = { openAddDialog.value = true }) {
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeCard(employee: Employee) {
    Card(border = BorderStroke(Dp.Hairline, MaterialTheme.colors.primary)) {
        Column(modifier = Modifier.padding(8.dp).width(400.dp)) {
            Text("Name: ${employee.name}")
            Text("ID: ${employee.id}")
            Text("Pay: ${employee.pay}")
            Text("DOB: ${employee.birthday}")
            Text("Address: ${employee.address}")
        }
    }
}

@Composable
fun AddEmployeeDialog(value: MutableState<Boolean?>, employees: EmployeeContainer) {
    Dialog(onDismissRequest = { value.value = false }) {
        Card(modifier = Modifier.fillMaxHeight()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(rememberScrollState())) {
                var name by remember { mutableStateOf("") }
                var id by remember { mutableStateOf("") }
                var hourly by remember { mutableStateOf(true) }
                var rate by remember { mutableStateOf("") }

                Text("Add Employee", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))

                OutlinedTextField(value = name, singleLine = true, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = id, singleLine = true, onValueChange = { id = it }, label = { Text("ID") }, modifier = Modifier.fillMaxWidth())

                DropdownButton(items = listOf("Hourly", "Salaried"), onItemSelected = { hourly = it == "Hourly" }, modifier = Modifier.fillMaxWidth()) {
                    Text("Pay Type: ${if(hourly) "Hourly" else "Salaried"}")
                }

                OutlinedTextField(value = rate,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        val valid = it.isEmpty() || it.isPositiveDouble()
                                && !it.endsWith("f") && !it.endsWith("F")
                                && !it.endsWith("d") && !it.endsWith("D")
                                && it.numDecimalPlaces <= 2
                        if(valid) {
                            rate = it
                        }
                    },
                    label = { Text("Pay Rate (per ${if(hourly) "hour" else "year"})") }, modifier = Modifier.fillMaxWidth())

                val datePickerState = rememberDatePickerState()
                DatePick(datePickerState)
                val birthday by remember { derivedStateOf { datePickerState.selectedDateMillis } }

                var street by remember { mutableStateOf("") }
                var city by remember { mutableStateOf("") }
                var state by remember { mutableStateOf("") }
                var zip by remember { mutableStateOf("") }

                Spacer(modifier = Modifier.height(4.dp))

                Card(modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))) {
                    Column {
                        Text("Address", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                        OutlinedTextField(
                            value = street,
                            singleLine = true,
                            onValueChange = { street = it },
                            label = { Text("Street") },
                            modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)
                        )
                        OutlinedTextField(
                            value = city,
                            singleLine = true,
                            onValueChange = { city = it },
                            label = { Text("City") },
                            modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)
                        )
                        OutlinedTextField(
                            value = state,
                            singleLine = true,
                            onValueChange = { state = it },
                            label = { Text("State") },
                            modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)
                        )
                        OutlinedTextField(
                            value = zip,
                            singleLine = true,
                            onValueChange = { zip = it },
                            label = { Text("Zip") },
                            modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(onClick = {
                    val added = employees.add(Employee(
                        name = name,
                        id = id,
                        pay = if(hourly) PayStrategy.Hourly(rate.toDouble()) else PayStrategy.Salaried(rate.toDouble()),
                        birthday = Date(birthday!!),
                        address = Address(street, city, state, zip)
                    ))
                    value.value = false

                    if(!added) {
                        value.value = null
                    }
                }) {
                    Text("Add Employee")
                }
            }
        }
    }
}

@Composable
fun DropdownButton(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Button(onClick = { expanded = true },
        content = content,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        border = if(expanded)
            BorderStroke(TextFieldDefaults.FocusedBorderThickness, MaterialTheme.colors.primary)
        else
            BorderStroke(TextFieldDefaults.UnfocusedBorderThickness, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        items.forEach { item ->
            DropdownMenuItem(onClick = {
                onItemSelected(item)
                expanded = false
            }) {
                Text(item)
            }
        }
    }
}

@Composable
fun DatePick(state: DatePickerState) {
    var showDatePicker by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        val timeMs = state.selectedDateMillis?.plus(86400000) ?: System.currentTimeMillis()
        OutlinedTextField(
            value = dateToString(Date(timeMs)),
            onValueChange = { },
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .width(400.dp)
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = state,
                        showModeToggle = false,
                    )
                }
            }
        }
    }
}

fun String.isPositiveDouble(): Boolean {
    return toDoubleOrNull()?.let { it >= 0 } == true
}

val String.numDecimalPlaces: Int
    get() = this.indexOf('.').let { if(it == -1) 0 else length - it - 1 }