package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showEmployeeList by remember { mutableStateOf(true) }

        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { showEmployeeList = !showEmployeeList }
            ) {
                Icon(
                    imageVector = if (showEmployeeList) Icons.Filled.Person else Icons.Filled.TwoPeople,
                    contentDescription = "Switch to ${if (showEmployeeList) "Clock In" else "Employee List"}"
                )
            }
        }

        if(showEmployeeList) {
            EmployeeList(employees)
        } else {
//            ClockInScreen(employees)
        }
    }
}