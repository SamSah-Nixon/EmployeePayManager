/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employees
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.ui.CoolTabRow
import org.ryecountryday.samandrhys.cruvna.ui.admin.actions.ActionsScreen

/**
 * Main screen for viewing all employees and their status.
 */
@Composable
fun AdminScreen(employees: MutableSet<Employee>, exitFunc: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        // Main content
        CoolTabRow(
            mapOf(
                "All Employees" to { EmployeeList(employees) },
                "Clocked In Employees" to {
                    EmployeeList(
                        employees.filter { WorkHistory.isClockedIn(it.id) }.toMutableSet(),
                        mainList = false,
                        defaultComparator = Employees.compareByTimeWorking
                    )
                },
                "Actions" to { ActionsScreen(employees) }
            ),
            secondary = true
        )

        // Exit button
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Button(
                onClick = exitFunc,
                modifier = Modifier.padding(horizontal = 6.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Exit Admin Mode"
                )
            }
        }
    }
}