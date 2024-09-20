package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.ui.InlineTimePicker
import org.ryecountryday.samandrhys.cruvna.ui.LabeledCard
import org.ryecountryday.samandrhys.cruvna.ui.mutedBorder
import org.ryecountryday.samandrhys.cruvna.ui.verticalScroll
import org.ryecountryday.samandrhys.cruvna.util.*

@Composable
fun EditScreen(employees: MutableSet<Employee>) {
    var screen by remember { mutableStateOf(0) }

    // main content
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        when (screen) {
            0 -> EditCurrentPayPeriodScreen()
            1 -> ViewPastPayPeriodsScreen()
        }
    }

    Column(modifier = Modifier.padding(start = 6.dp, top = 6.dp)) {
        Button(onClick = { screen = 0 }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.List,
                contentDescription = "Edit Current Pay Period"
            )
        }
        Button(onClick = { screen = 1 }) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "View Past Pay Periods"
            )
        }
    }
}

@Composable
private fun EditCurrentPayPeriodScreen() {
    val entries by remember { mutableStateOf(
        mutableSetOf(
            *WorkHistory.currentPeriod.toTypedArray(),
            *WorkHistory.clockedIn.toTypedArray()
        )
    ) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().verticalScroll()) {
        for(entry in entries) {
            EditWorkEntry(entry)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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

                    if(entry.end != null) {
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

@Composable
private fun ViewPastPayPeriodsScreen() {
    Text("TODO")
}