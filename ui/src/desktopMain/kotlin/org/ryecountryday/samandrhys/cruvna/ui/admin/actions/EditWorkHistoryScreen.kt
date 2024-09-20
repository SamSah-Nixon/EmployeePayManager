package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkEntry
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.ui.mutedBorder
import org.ryecountryday.samandrhys.cruvna.ui.verticalScroll
import org.ryecountryday.samandrhys.cruvna.util.toLocalDateTime
import org.ryecountryday.samandrhys.cruvna.util.toNiceString

@Composable
fun EditScreen(employees: MutableSet<Employee>) {
    var screen by remember { mutableStateOf(2) }

    // 3 buttons to switch between the 3 screens
    Column(modifier = Modifier.padding(start = 6.dp, top = 6.dp)) {
        Button(onClick = { screen = 1 }) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Clock In/Out"
            )
        }
        Button(onClick = { screen = 2 }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.List,
                contentDescription = "Edit Current Pay Period"
            )
        }
        Button(onClick = { screen = 3 }) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "View Past Pay Periods"
            )
        }
    }

    // main content
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        when (screen) {
            1 -> ClockInOutScreen(employees)
            2 -> EditCurrentPayPeriodScreen()
            3 -> ViewPastPayPeriodsScreen()
        }
    }
}

@Composable
private fun ClockInOutScreen(employees: MutableSet<Employee>) {
    Text("TODO")
}

@Composable
private fun EditCurrentPayPeriodScreen() {
    val entries by remember { mutableStateOf(WorkHistory.currentPeriod) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().verticalScroll()) {
        for(entry in entries) {
            EditWorkEntry(entry)
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


}

@Composable
private fun ViewPastPayPeriodsScreen() {
    Text("TODO")
}