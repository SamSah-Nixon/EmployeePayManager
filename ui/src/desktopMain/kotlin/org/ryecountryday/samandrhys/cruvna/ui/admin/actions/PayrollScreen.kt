/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */
package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employees
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.ui.employees
import org.ryecountryday.samandrhys.cruvna.ui.mutedBorder
import org.ryecountryday.samandrhys.cruvna.ui.save
import org.ryecountryday.samandrhys.cruvna.util.*
import java.time.Instant
import kotlin.io.path.bufferedWriter

/**
 * Screen for managing pay periods, with buttons to create and remove pay periods, and a list on the right of all pay periods.
 */
@Composable
fun PayrollScreen() {
    var confirmPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp).background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.BottomCenter
    ) {

        var showFailedPopup by remember { mutableStateOf(false) }

        Column(modifier = Modifier.width(200.dp).align(Alignment.CenterStart).padding(16.dp)) {

            // When this is called it opens a dialog to select a directory to save the pay report
            val launcher = rememberDirectoryPickerLauncher(
                title = "Select a directory to save the pay report",
            ) { dir ->
                if(dir == null) return@rememberDirectoryPickerLauncher
                val file = dir.file.toPath().resolve("pay_report_${Instant.now().toLocalDate().toDateString(separator = '_')}.txt")
                file.bufferedWriter().use {
                    val pp = WorkHistory.payPeriods.first()
                    it.write("Pay Report for ${Instant.now().toLocalDate()}\n\n")
                    it.write("Spanned from ${pp.payPeriodStart.toDateString()} to ${pp.payPeriodEnd.toDateString()} (${pp.daysInPeriod} days)\n\n")
                    it.write("Pay:\n\n")

                    var printed = false

                    for(e in employees) {
                        if(pp.hoursWorked(e.id) <= 0 || e == Employees.ADMIN) continue
                        printed = true
                        it.write("Employee: ${e.name}\n")
                        it.write("  - ID: ${e.id}\n")
                        it.write("  - Hours: ${pp.hoursWorked(e.id)}\n")
                        it.write("  - Pay: ${e.pay.calculateSalary(pp, e.id)}\n\n")
                    }

                    if(!printed) {
                        it.write("No employees worked during this pay period.")
                    }
                }

                os.openFile(file)
            }

            Button(
                onClick = {
                    println("Creating Pay Period")
                    val bool = WorkHistory.addPayPeriod(Instant.now().toLocalDate())
                    println("Did it work? $bool")
                    showFailedPopup = !bool
                    if(bool) {
                        save()
                        launcher.launch()
                    }
                },
                modifier = Modifier.width(200.dp).height(150.dp).padding(8.dp)
            ) {
                Text(
                    "Create Pay Period",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { confirmPopup = true },
                modifier = Modifier.width(200.dp).height(150.dp).padding(8.dp)
            ) {
                Text(
                    "Remove All Pay Periods",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }

        if(showFailedPopup) {
            Dialog(onDismissRequest = { showFailedPopup = false }) {
                Box(
                    modifier = Modifier.background(MaterialTheme.colors.surface)
                        .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface)).padding(4.dp),
                ) {
                    Column(modifier = Modifier.padding(64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Failed to create pay period!", color = MaterialTheme.colors.onSurface)
                        Text("There is already a pay period that ends today.", color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showFailedPopup = false },
                        ) {
                            Text("Ok",
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.width(500.dp).align(Alignment.CenterEnd).padding(16.dp)) {
            var index = WorkHistory.payPeriods.size
            for (payPeriod in WorkHistory.payPeriods) {
                Card(
                    modifier = Modifier.fillMaxWidth().border(border = mutedBorder())
                        .background(MaterialTheme.colors.primary),
                ) {
                    Text(
                        "Pay Period #:${index} ${payPeriod.payPeriodStart} - ${payPeriod.payPeriodEnd}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
                Row(modifier = Modifier.padding(4.dp)) {}
                index--
            }
        }
    }

    if (confirmPopup) {
        Dialog(onDismissRequest = { confirmPopup = false }) {
            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(modifier = Modifier.padding(32.dp)) {
                        Text(
                            "Are you sure you want to remove all pay periods?",
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    Row(modifier = Modifier.padding(64.dp)) {
                        Button(
                            onClick = {
                                WorkHistory.payPeriods.clear()
                                save()
                                confirmPopup = false
                            },
                            modifier = Modifier.size(150.dp).padding(16.dp)
                        ) {
                            Text(
                                "Yes",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = {
                                confirmPopup = false
                            },
                            modifier = Modifier.size(150.dp).padding(16.dp)
                        ) {
                            Text(
                                "No",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}