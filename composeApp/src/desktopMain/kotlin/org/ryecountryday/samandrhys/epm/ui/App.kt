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
                    pay = PayStrategy.Hourly(0),
                    birthday = parseDate("01/01/2000"),
                    address = Address("3 Five Cedar", "Rye Land", "New York", "11122-1111"),
                ))
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

        val openAddDialog: MutableState<Any> = remember { mutableStateOf(false) }
        FloatingActionButton(onClick = { openAddDialog.value = true }) {
            Icon(Icons.Filled.Add, contentDescription = "Add Employee")
        }

        if(openAddDialog.value == true) {
            AddEmployeeDialog(openAddDialog, employees)
        } else if(openAddDialog.value is String) {
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
fun AddEmployeeDialog(value: MutableState<Any>, employees: EmployeeContainer) {
    Dialog(onDismissRequest = { value.value = false }) {
        Card(modifier = Modifier.height(700.dp).width(500.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(rememberScrollState())) {
                var name by remember { mutableStateOf("") }
                var id by remember { mutableStateOf("") }
                var hourly by remember { mutableStateOf(true) }
                var rate by remember { mutableStateOf("") }

                Text("Add Employee", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))

                OutlinedTextField(value = name, singleLine = true, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.width(400.dp))
                OutlinedTextField(value = id, singleLine = true, onValueChange = { id = it }, label = { Text("ID") }, modifier = Modifier.width(400.dp))

                DropdownButton(items = listOf("Hourly", "Salaried"), onItemSelected = { hourly = it == "Hourly" }, modifier = Modifier.width(400.dp)) {
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
                    label = { Text("Pay Rate (per ${if(hourly) "hour" else "year"})") }, modifier = Modifier.width(400.dp))

                val datePickerState = rememberDatePickerState()
                DatePick(datePickerState, modifier = Modifier.width(400.dp).align(Alignment.CenterHorizontally))
                val birthday by remember { derivedStateOf { datePickerState.selectedDateMillis } }

                var street by remember { mutableStateOf("") }
                var city by remember { mutableStateOf("") }
                var state by remember { mutableStateOf("") }
                var zip by remember { mutableStateOf("") }

                Spacer(modifier = Modifier.height(8.dp))

                LabeledCard("Address", modifier = Modifier.width(400.dp), border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))) {
                    Column {
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
                    }
                }

                Button(onClick = {
                    if(name.isBlank()) {
                        value.value = "Name cannot be blank"
                        return@Button
                    }

                    if(id.isBlank()) {
                        value.value = "ID cannot be blank"
                        return@Button
                    }

                    if(rate.isBlank()) {
                        value.value = "Rate cannot be blank"
                        return@Button
                    }

                    // birthday defaults to today

                    if(street.isBlank()) {
                        value.value = "Street cannot be blank"
                        return@Button
                    }

                    if(city.isBlank()) {
                        value.value = "City cannot be blank"
                        return@Button
                    }

                    if(state.isBlank()) {
                        value.value = "State cannot be blank"
                        return@Button
                    }

                    if(zip.isBlank()) {
                        value.value = "Zip cannot be blank"
                        return@Button
                    }

                    val employee = Employee(
                        name = name,
                        id = id,
                        pay = if(hourly) PayStrategy.Hourly(rate.toDouble()) else PayStrategy.Salaried(rate.toDouble()),
                        birthday = Date(birthday ?: System.currentTimeMillis()),
                        address = Address(street, city, state, zip)
                    )

                    println("adding employee: $employee")

                    val added = employees.add(employee)

                    value.value = false

                    if(!added) {
                        value.value = "Employee with ID $id already exists"
                    }
                }, modifier = Modifier.width(400.dp)) {
                    Text("Add Employee")
                }
            }
        }
    }
}

@Composable
fun LabeledCard(value: String,
                modifier: Modifier = Modifier,
                border: BorderStroke,
                content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = border
        ) {
            Column(modifier = Modifier.padding(top = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                content()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Label text that overlaps with the card's border
        Text(value,
            modifier = Modifier.padding(start = 16.dp)
                .align(Alignment.TopStart).offset(y = (-8).dp)
                .background(MaterialTheme.colors.background) // Add a background color to match the parent - gives the appearance of interrupting the border
                .padding(horizontal = 4.dp), // Padding for the text itself
            color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            style = MaterialTheme.typography.caption
        )
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
fun DatePick(state: DatePickerState, modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
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
            modifier = Modifier.fillMaxWidth().height(64.dp)
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