package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee

@Composable
fun EditScreen(employees: MutableSet<Employee>) {
    var screen by remember { mutableStateOf(0) }

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
            3 -> EditWorkHistoryScreen()
        }
    }
}

@Composable
private fun ClockInOutScreen(employees: MutableSet<Employee>) {
    Text("TODO")
}

@Composable
private fun EditCurrentPayPeriodScreen() {
    Text("TODO")
}

@Composable
private fun EditWorkHistoryScreen() {
    Text("TODO")
}