/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */
package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.ui.InlineTimePicker
import org.ryecountryday.samandrhys.cruvna.ui.LabeledCard
import org.ryecountryday.samandrhys.cruvna.ui.mutedBorder
import org.ryecountryday.samandrhys.cruvna.ui.verticalScroll
import org.ryecountryday.samandrhys.cruvna.util.*

@Composable
fun EditScreen() {
    val entries by remember {
        mutableStateOf(
            mutableSetOf(
                *WorkHistory.currentPeriod.toTypedArray<WorkEntry>(),
                *WorkHistory.clockedIn.toTypedArray<WorkEntry>()
            )
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        if(entries.isEmpty()) {
            Text("No entries to edit",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onSurface
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().verticalScroll()) {
            for (entry in entries) {
                EditWorkEntry(entry)
            }
        }
    }
}

@Composable
private fun EditWorkEntry(entry: WorkEntry) {
    var show by remember { mutableStateOf(false) }
    Button(
        onClick = { show = true },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        modifier = Modifier.padding(2.dp),
        border = mutedBorder()
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface)) {
            Column(modifier = Modifier.padding(8.dp).width(200.dp)) {
                Text("ID: ${entry.id}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Start: ${entry.start.toLocalDateTime().toNiceString()}")
                Text("End: ${entry.end?.toLocalDateTime()?.toNiceString() ?: "Ongoing"}")
            }
        }
    }

    if (show) {
        Dialog(onDismissRequest = { show = false }) {
            Card(modifier = Modifier.width(500.dp), backgroundColor = MaterialTheme.colors.background) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Entry Details", modifier = Modifier.padding(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    val modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)

                    LabeledCard("ID", modifier = modifier) { Text(entry.id) }

                    InlineTimePicker(
                        label = "Start Time",
                        initialTime = entry.start.toLocalDateTime().toLocalTime(),
                        onTimeChange = {
                            entry.start = entry.start.toLocalDate().withTime(it).toInstant()
                        },
                        modifier = modifier,
                    )

                    if (entry.end != null) {
                        InlineTimePicker(
                            label = "End Time",
                            initialTime = entry.end!!.toLocalDateTime().toLocalTime(),
                            onTimeChange = {
                                entry.end = entry.end!!.toLocalDate().withTime(it).toInstant()
                            },
                            modifier = modifier
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
