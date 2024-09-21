/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */
package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory.payPeriods
import org.ryecountryday.samandrhys.cruvna.ui.InlineDatePicker
import org.ryecountryday.samandrhys.cruvna.ui.mutedBorder
import org.ryecountryday.samandrhys.cruvna.ui.save
import org.ryecountryday.samandrhys.cruvna.util.toInstant
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen() {
    val datePickerState = rememberDatePickerState()
    val date by remember { derivedStateOf { datePickerState.selectedDateMillis } }

    var confirmPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp).background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.BottomCenter
    ) {



        Column(modifier = Modifier.width(200.dp).align(Alignment.CenterStart).padding(16.dp)) {
            InlineDatePicker(
                "End Date",
                datePickerState,
                modifier = Modifier.height(100.dp).width(200.dp).padding(16.dp)
            )
            datePickerState.selectedDateMillis = Instant.now().toEpochMilli()
            Button(
                onClick = {
                    var bool = WorkHistory.addPayPeriod(Instant.ofEpochMilli(date!!).atZone(ZoneId.systemDefault()).toLocalDate())
                    print(bool)
                    save()
                },
                modifier = Modifier.width(200.dp).height(100.dp).padding(8.dp)
            ) {
                Text(
                    "Create Pay Period",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    confirmPopup = true
                },
                modifier = Modifier.width(200.dp).height(100.dp).padding(8.dp)
            ) {
                Text(
                    "Remove All Pay Periods",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(modifier = Modifier.width(500.dp).align(Alignment.CenterEnd).padding(16.dp)) {
            var index = 0
            for (payPeriod in payPeriods) {
                index++
                Box(
                    modifier = Modifier.fillMaxWidth().border(border = mutedBorder())
                        .background(MaterialTheme.colors.primary), contentAlignment = Alignment.Center
                )
                {
                    Text(
                        "Pay Period #:${index} ${payPeriod.payPeriodStart} - ${payPeriod.payPeriodEnd}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
                Row(modifier = Modifier.padding(4.dp)) {}
            }
        }
    }
    if (confirmPopup) {
        ConfirmPopup(onClose = { confirmPopup = false })
        print(confirmPopup)
    }
}
    @Composable
    fun ConfirmPopup(onClose: () -> Unit){
        Box(
            modifier = Modifier.size(300.dp).background(MaterialTheme.colors.surface)
                .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface)).padding(4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(modifier = Modifier.padding(32.dp)){Text("Are you sure you want to remove all pay periods?", color = MaterialTheme.colors.onSurface) }
            Row(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        payPeriods.clear()
                        save()
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