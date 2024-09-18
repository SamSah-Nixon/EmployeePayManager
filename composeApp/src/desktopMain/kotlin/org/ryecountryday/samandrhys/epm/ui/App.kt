/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory

/**
 * The main entry point for the application. This is called by [main]
 * and holds the outer ui behind [EmployeeList] and [ClockInScreen].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App() {
    var admin by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Button(
                onClick = { admin = !admin },
                modifier = Modifier.padding(6.dp)
            ) {
                Icon(
                    imageVector = if (admin) Icons.Filled.Person else Icons.Filled.FourPeople,
                    contentDescription = "Switch to ${if (admin) "Clock In" else "Admin"} Screen"
                )
            }
        }
    }

    if (admin) {
        Column(modifier = Modifier.fillMaxSize()) {
            var tab by remember { mutableStateOf(0) }
            SecondaryTabRow(selectedTabIndex = tab) {
                Tab(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    text = { Text("All Employees") }
                )
                Tab(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    text = { Text("Clocked In Employees") }
                )
            }

            Box { // use Box to reset the alignments in the Column
                if (tab == 0) {
                    EmployeeList(employees)
                } else if(tab == 1) {
                    EmployeeList(employees.filter { WorkHistory.isClockedIn(it.id) }.toMutableSet(), false)
                }
            }
        }
    } else {
        ClockInScreen(employees)
    }
}