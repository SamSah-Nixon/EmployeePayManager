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

@Composable
fun ClockInScreen(employees: EmployeeContainer) {
    var showPopup by remember { mutableStateOf(false) }
    var employeeId by remember { mutableStateOf("") }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Clock in", style = MaterialTheme.typography.h4)
            OutlinedTextField(
                value = employeeId,
                onValueChange = { employeeId = it },
                singleLine = true,
                label = { Text("Employee ID") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        showPopup = employeeId.isNotEmpty().apply {
                            if(this) {
                                println("Clocking in employee $employeeId")
                            }
                        }
                    }
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
        Card(modifier = Modifier.width(500.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val maybeEmployee by remember { mutableStateOf(employees[employeeId]) }
                if (maybeEmployee == null) {
                    Text("Employee not found")
                    return@Column
                }

                val employee by remember { mutableStateOf(maybeEmployee!!) }

                val clockedIn = WorkHistory.clockedIn.firstOrNull { it.id == employee.id } != null

                if (clockedIn) {
                    Button(onClick = {
                        WorkHistory.clockOut(employee.id)
                    }) {
                        Text("Clock out")
                    }
                } else {
                    Button(onClick = {
                        WorkHistory.clockIn(employee.id)
                    }) {
                        Text("Clock in")
                    }
                }
            }
        }
    }
}