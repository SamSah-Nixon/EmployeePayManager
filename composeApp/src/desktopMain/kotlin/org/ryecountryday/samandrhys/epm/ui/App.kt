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
    var admin by remember { mutableStateOf(false) } // remember keeps this state across recompositions

    if (admin) {
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
                        onClick = { admin = false },
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Exit Admin Mode"
                        )
                    }
                }
            }
        }

        AdminScreen(employees)
    } else {
        ClockInScreen(employees, onAdminButtonClicked = { admin = true })
    }
}