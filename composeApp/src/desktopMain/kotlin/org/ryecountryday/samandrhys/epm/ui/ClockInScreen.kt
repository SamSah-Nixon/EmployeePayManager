/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory

/**
 * A very simple screen that allows employees to clock in and out.
 * It just has a text field for the employee ID and a button to clock in, which opens a [ClockInPopup].
 */
@Composable
fun ClockInScreen(employees: EmployeeContainer) {
    var showPopup by remember { mutableStateOf(false) }
    var employeeId by remember { mutableStateOf("") }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Clock in", style = MaterialTheme.typography.h4.copy(MaterialTheme.colors.onBackground))
            OutlinedTextField(
                value = employeeId,
                onValueChange = { employeeId = it },
                singleLine = true,
                label = { Text("Employee ID") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { showPopup = employeeId.isNotEmpty() }
                )
            )
        }
    }

    if (showPopup) {
        ClockInPopup(employees, employeeId) {
            showPopup = false
        }
    }
}

/**
 * A popup that shows the employee's name and allows them to clock in or out.
 * If the employee is already clocked in, it shows how long they have been working.
 */
@Composable
private fun ClockInPopup(employees: EmployeeContainer, employeeId: String, onClose: () -> Unit) {
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

                if(employee.isBirthday()){
                    Text("!!!Happy Birthday,\n${employee.name}!!!", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
                }
                else{
                    Text("Welcome,\n${employee.name}", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
                }
                val timeWorkedFormatted = run {
                    val timeWorkedSeconds = WorkHistory.getClockedInEntry(employee.id)?.durationSeconds ?: 0
                    val hours = timeWorkedSeconds / 3600
                    val minutes = (timeWorkedSeconds % 3600) / 60
                    val seconds = timeWorkedSeconds % 60

                    buildString {
                        if (hours > 0) append("$hours hours, ")
                        if (minutes > 0) append("$minutes minutes, ")
                        if (seconds > 0) append("$seconds seconds")

                        if(isEmpty()) {
                            append("0 seconds")
                        }
                    }
                }

                if (clockedIn) {
                    Text("You have been working for $timeWorkedFormatted")
                    Button(onClick = {
                        WorkHistory.clockOut(employee.id)
                        clockedIn = WorkHistory.isClockedIn(employee.id)
                    }) {
                        Text("Clock out")
                    }
                } else {
                    Button(onClick = {
                        WorkHistory.clockIn(employee.id)
                        clockedIn = WorkHistory.isClockedIn(employee.id)
                    }) {
                        Text("Clock in")
                    }
                }
            }
        }
    }
}