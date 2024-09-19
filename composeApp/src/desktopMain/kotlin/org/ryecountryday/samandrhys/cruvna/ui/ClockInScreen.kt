/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.ryecountryday.samandrhys.cruvna.backend.EmployeeContainer
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employees
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.util.formatTime

/**
 * A very simple screen that allows employees to clock in and out.
 * It just has a text field for the employee ID and a button to clock in, which opens a [ClockInPopup].
 */
@Composable
fun ClockInScreen(employees: EmployeeContainer, onAdminButtonClicked: () -> Unit = {}) {
    var showPopup by remember { mutableStateOf(false) }
    var employeeId by remember { mutableStateOf("") }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Clock in", style = MaterialTheme.typography.h4.copy(MaterialTheme.colors.onBackground))

            Row(verticalAlignment = Alignment.Bottom) {
                OutlinedTextField(
                    value = employeeId,
                    onValueChange = { employeeId = it },
                    singleLine = true,
                    label = { Text("Employee ID") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false),
                    keyboardActions = KeyboardActions(
                        onDone = { showPopup = employeeId.isNotEmpty() }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(MaterialTheme.colors.onBackground)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Button(
                    onClick = { showPopup = employeeId.isNotEmpty() },
                    content = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    modifier = Modifier.height(56.dp)
                )
            }
        }
    }

    if (showPopup) {
        ClockInPopup(
            employees,
            employeeId,
            onClose = { showPopup = false },
            onAdminButtonClicked = onAdminButtonClicked
        )
    }
}

/**
 * A popup that shows the employee's name and allows them to clock in or out.
 * If the employee is already clocked in, it shows how long they have been working.
 * @param employees The [EmployeeContainer] to get the employee from.
 * @param employeeId The ID of the employee to clock in. May not exist in the container.
 * @param onClose A callback to close the popup.
 * @param onAdminButtonClicked A callback to call when the admin button is clicked, if the employee is the admin.
 */
@Composable
private fun ClockInPopup(
    employees: EmployeeContainer,
    employeeId: String,
    onClose: () -> Unit,
    onAdminButtonClicked: () -> Unit = {}
) {
    Dialog(onDismissRequest = onClose) {
        Card(modifier = Modifier.width(350.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val maybeEmployee by remember { mutableStateOf(employees[employeeId]) }
                if (maybeEmployee == null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Employee not found")
                    Spacer(modifier = Modifier.height(16.dp))
                    return@Column
                }

                val employee by remember { mutableStateOf(maybeEmployee!!) }
                var clockedIn by remember { mutableStateOf(WorkHistory.isClockedIn(employee.id)) }

                // If it's the employee's birthday, show a special message
                Text(if(employee.isBirthday()) "Happy Birthday,\n${employee.name}!!!" else "Welcome, ${employee.name}.",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )

                // If admin is logged in, don't show the clock in/out button, but show the admin button
                if(employee == Employees.ADMIN) {
                    Button(onClick = onAdminButtonClicked, modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.FourPeople, contentDescription = "Admin", modifier = Modifier.padding(end = 8.dp))
                            Text("Enter Admin Mode")
                        }
                    }
                    return@Card // return early so the stuff below doesn't show
                }

                // If the employee is already clocked in, show how long they have been working
                if(clockedIn) {
                    var state by remember { mutableStateOf(0L) }

                    LaunchedEffect(Unit) { // update state every second
                        while (true) {
                            state = WorkHistory.getClockedInEntry(employee.id)?.durationSeconds ?: 0
                            delay(1000)
                        }
                    }

                    Text(
                        "You have been working for:\n${formatTime(state)}",
                        textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(32.dp))
                }

                Button(onClick = {
                    if(clockedIn) WorkHistory.clockOut(employee.id)
                    else WorkHistory.clockIn(employee.id)
                    clockedIn = WorkHistory.isClockedIn(employee.id)
                }, content = { Text("Clock ${if(clockedIn) "out" else "in"}") })
            }
        }
    }
}