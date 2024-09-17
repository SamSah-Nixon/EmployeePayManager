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
import org.ryecountryday.samandrhys.epm.util.roundToTwoDecimalPlaces

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

                Text("Welcome, ${employee.name}", style = MaterialTheme.typography.h5)
                Spacer(modifier = Modifier.height(16.dp))

                if (clockedIn) {
                    Text("You have been working for ${(WorkHistory.getEntry(employee.id)!!.durationSeconds / 60.0).roundToTwoDecimalPlaces()} minutes")
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