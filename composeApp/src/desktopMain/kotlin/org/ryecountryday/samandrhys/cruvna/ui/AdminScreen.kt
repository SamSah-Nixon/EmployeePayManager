/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employees
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.util.openFolder
import org.ryecountryday.samandrhys.cruvna.util.os

/**
 * Main screen for viewing all employees and their status.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AdminScreen(employees: MutableSet<Employee>, exitFunc: () -> Unit) {
    // Admin controls
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Column(modifier = Modifier.align(Alignment.Bottom)) {
                Button(
                    onClick = { os.openFolder(mainFolder) },
                    modifier = Modifier.padding(horizontal = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FolderOpen,
                        contentDescription = "Open Application Data Folder"
                    )
                }

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

    Column(modifier = Modifier.fillMaxSize()) {
        var tab by remember { mutableStateOf(0) }
        val tabs = listOf("All Employees", "Clocked In Employees", "Payroll")
        SecondaryTabRow(selectedTabIndex = tab, containerColor = MaterialTheme.colors.background) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = MaterialTheme.colors.onBackground) },
                    selected = tab == index,
                    onClick = { tab = index }
                )
            }
        }

        Box { // use Box to reset the alignments in the Column
            when (tab) {
                0 -> EmployeeList(employees) // just a regular list of all employees, sorted by id
                1 -> EmployeeList(
                    employees.filter { WorkHistory.isClockedIn(it.id) }.toMutableSet(),
                    mainList = false,
                    defaultComparator = Employees.compareByTimeWorking
                )
                2 -> {}//PayrollScreen(employees)
            }
        }
    }
}