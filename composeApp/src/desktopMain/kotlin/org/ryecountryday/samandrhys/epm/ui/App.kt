@file:OptIn(ExperimentalMaterial3Api::class)
package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.util.parseDate
import org.ryecountryday.samandrhys.epm.util.toDateString
import java.util.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        val employees = remember {
            EmployeeContainer(
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
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            employees.forEach {
                EmployeeCard(it)
            }
        }

        val addDialogState: MutableState<Any> = remember { mutableStateOf(false) }
        FloatingActionButton(onClick = { addDialogState.value = true }) {
            Icon(Icons.Filled.Add, contentDescription = "Add Employee")
        }

        if(addDialogState.value != false) {
            AddEmployeeDialog(addDialogState, employees)
        }
    }
}

@Composable
fun EmployeeCard(employee: Employee) {

    var show by remember { mutableStateOf(false) }
    Button(onClick = { show = true },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        modifier = Modifier.padding(2.dp),
        border = mutedBorder()
    ) {
        Column(modifier = Modifier.padding(8.dp).width(400.dp)) {
            Text("Name: ${employee.name}", style = MaterialTheme.typography.body1)
            Text("ID: ${employee.id}", style = MaterialTheme.typography.body1)
        }
    }

    if(show) {
        Dialog(onDismissRequest = { show = false }) {
            Card(modifier = Modifier.width(500.dp)) {
                Column {
                    Text("Employee Details", modifier = Modifier.padding(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    val modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)

                    LabeledCard("First Name", border = mutedBorder(), modifier = modifier) { Text(employee.firstName) }
                    LabeledCard("Last Name", border = mutedBorder(), modifier = modifier) { Text(employee.lastName) }
                    LabeledCard("ID", border = mutedBorder(), modifier = modifier) { Text(employee.id) }
                    LabeledCard("Pay Type", border = mutedBorder(), modifier = modifier) { Text(employee.pay.toString()) }
                    LabeledCard("Birthday", border = mutedBorder(), modifier = modifier) { Text(employee.dateOfBirth.toDateString()) }
                    LabeledCard("Address", border = mutedBorder(), modifier = modifier) { Text(employee.address.toStringMultiline()) }
                }
            }
        }
    }
}

@Composable
fun AddEmployeeDialog(value: MutableState<Any>, employees: EmployeeContainer) {
    Dialog(
        onDismissRequest = { value.value = false },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(modifier = Modifier.width(500.dp)) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                var lastName by remember { mutableStateOf("") }
                var firstName by remember { mutableStateOf("") }
                var id by remember { mutableStateOf("") }
                var hourly by remember { mutableStateOf(true) }
                var rate by remember { mutableStateOf("") }

                Text("Add Employee", modifier = Modifier.padding(8.dp))

                val modifier = Modifier.width(400.dp).align(Alignment.CenterHorizontally)
                OutlinedTextField(
                    value = lastName,
                    singleLine = true,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.width(400.dp)
                )
                OutlinedTextField(
                    value = firstName,
                    singleLine = true,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.width(400.dp)
                )
                OutlinedTextField(
                    value = id,
                    singleLine = true,
                    onValueChange = { id = it },
                    label = { Text("ID") },
                    modifier = Modifier.width(400.dp)
                )

                DropdownButton(
                    items = listOf("Hourly", "Salaried"),
                    onItemSelected = { hourly = it == "Hourly" },
                    modifier = Modifier.width(400.dp)
                ) {
                    Text("Pay Type: ${if (hourly) "Hourly" else "Salaried"}")
                }

                fun String.isPositiveDouble(): Boolean {
                    return toDoubleOrNull()?.let { it >= 0 } == true
                }

                fun String.numDecimalPlaces(): Int {
                    return this.indexOf('.').let { if (it == -1) 0 else length - it - 1 }
                }

                OutlinedTextField(
                    value = rate,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        val valid = it.isEmpty() || it.isPositiveDouble()
                                && !it.endsWith("f") && !it.endsWith("F")
                                && !it.endsWith("d") && !it.endsWith("D")
                                && it.numDecimalPlaces() <= 2
                        if (valid) {
                            rate = it
                        }
                    },
                    label = { Text("Pay Rate (per ${if (hourly) "hour" else "year"})") }, modifier = modifier
                )

                val datePickerState = rememberDatePickerState()
                InlineDatePicker(datePickerState, modifier = modifier)
                val birthday by remember { derivedStateOf { datePickerState.selectedDateMillis } }

                var street by remember { mutableStateOf("") }
                var city by remember { mutableStateOf("") }
                var state by remember { mutableStateOf("") }
                var zip by remember { mutableStateOf("") }

                Spacer(modifier = Modifier.height(8.dp))

                LabeledCard("Address", modifier = modifier, border = mutedBorder()) {
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

                Row(modifier = modifier) {
                    Button(onClick = {
                        if (lastName.isBlank() && firstName.isBlank()) {
                            value.value = "Name cannot be blank"
                            return@Button
                        }

                        if (id.isBlank()) {
                            value.value = "ID cannot be blank"
                            return@Button
                        }

                        if (rate.isBlank()) {
                            value.value = "Rate cannot be blank"
                            return@Button
                        }

                        // birthday defaults to today, so it's always valid

                        if (street.isBlank()) {
                            value.value = "Street cannot be blank"
                            return@Button
                        }

                        if (city.isBlank()) {
                            value.value = "City cannot be blank"
                            return@Button
                        }

                        if (state.isBlank()) {
                            value.value = "State cannot be blank"
                            return@Button
                        }

                        if (zip.isBlank()) {
                            value.value = "Zip cannot be blank"
                            return@Button
                        }

                        val employee = Employee(
                            lastName = lastName,
                            firstName = firstName,
                            id = id,
                            pay = if (hourly) PayStrategy.Hourly(rate.toDouble()) else PayStrategy.Salaried(rate.toDouble()),
                            dateOfBirth = Date(birthday ?: System.currentTimeMillis()),
                            address = Address(street, city, state, zip)
                        )

                        println("adding employee: $employee")

                        val added = employees.add(employee)

                        if (!added) {
                            value.value = "Employee with ID $id already exists"
                        } else {
                            value.value = false
                        }
                    }) {
                        Text("Add Employee")
                    }

                    if(value.value is String) {
                        Dialog(onDismissRequest = { value.value = true }) {
                            Card {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(value.value as String, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                                    Button(onClick = { value.value = true }) {
                                        Text("Try Again")
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { value.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text("Cancel")
                    }
                }
            }

        }
    }

}

