package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun App() {
    var showEmployeeList by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Button(
                onClick = { showEmployeeList = !showEmployeeList },
                modifier = Modifier.padding(6.dp)
            ) {
                Icon(
                    imageVector = if (showEmployeeList) Icons.Filled.Person else Icons.Filled.FourPeople,
                    contentDescription = "Switch to ${if (showEmployeeList) "Clock In" else "Employee List"} Screen"
                )
            }
        }
    }

    if (showEmployeeList) {
        EmployeeList(employees)
    } else {
        ClockInScreen(employees)
    }
}