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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory.payPeriods
import org.ryecountryday.samandrhys.cruvna.ui.mutedBorder
import org.ryecountryday.samandrhys.cruvna.ui.save
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen() {
    var confirmPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp).background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(modifier = Modifier.width(200.dp).align(Alignment.CenterStart).padding(16.dp)) {
            Button(
                onClick = {
                    var bool = WorkHistory.addPayPeriod(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate())
                    print(bool)
                    save()
                },
                modifier = Modifier.width(200.dp).height(150.dp).padding(8.dp)
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
                modifier = Modifier.width(200.dp).height(150.dp).padding(8.dp)
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
            var index = payPeriods.size
            for (payPeriod in payPeriods) {

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
                index--
            }
        }
    }
    if (confirmPopup) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface)
                .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface)).padding(4.dp),
        ) {
            Row(modifier = Modifier.padding(32.dp)){Text("Are you sure you want to remove all pay periods?", color = MaterialTheme.colors.onSurface) }
            Row(modifier = Modifier.padding(64.dp)) {
                Button(
                    onClick = {
                        payPeriods.clear()
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