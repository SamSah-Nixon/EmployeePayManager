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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.epm.util.openFolder
import org.ryecountryday.samandrhys.epm.util.os

/**
 * The main entry point for the application. This is called by [main]
 * and holds the outer ui behind [EmployeeList] and [ClockInScreen].
 */
@Preview
@Composable
fun App() {
    var admin by remember { mutableStateOf(false) }


    // The main screen of the app, with buttons to switch between the admin and clock in screens
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Column(modifier = Modifier.align(Alignment.Bottom)) {
                if(admin) {
                    Button(
                        onClick = { os.openFolder(mainFolder) },
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FolderOpen,
                            contentDescription = "Open Application Data Folder"
                        )
                    }
                }

                Button(
                    onClick = { admin = !admin },
                    modifier = Modifier.padding(horizontal = 6.dp)
                ) {
                    Icon(
                        imageVector = if (admin) Icons.Filled.Person else Icons.Filled.FourPeople,
                        contentDescription = "Switch to ${if (admin) "Clock In" else "Admin"} Screen"
                    )
                }
            }
        }
    }

    if (admin) {
        AdminScreen(employees)
    } else {
        ClockInScreen(employees)
    }
}