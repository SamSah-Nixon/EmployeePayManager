/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */
package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.ryecountryday.samandrhys.cruvna.ui.*
import org.ryecountryday.samandrhys.cruvna.util.openFolder
import org.ryecountryday.samandrhys.cruvna.util.os
import org.ryecountryday.samandrhys.cruvna.util.removeShutdownHook
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

/**
 * Screen with 4 buttons:
 * - Open Application Data Folder - opens the folder where all the files are stored in the OS's file explorer
 * - Save Data to Disk - saves all in-memory data to disk
 * - Load Data from Disk - loads all data from disk into memory (with confirmation screen, since it overwrites current data)
 * - Delete All Data and Exit - deletes all data from disk and exits the program (with confirmation screen)
 */
@Composable
fun FilesScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(32.dp))

        // helper function for all the buttons we use here
        @Composable
        fun button(
            text: String,
            icon: ImageVector,
            color: Color = MaterialTheme.colors.primary,
            onClick: () -> Unit
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier.padding(horizontal = 6.dp).width(500.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = color),
                content = { TextWithIcon(text, icon, spacerWidth = 6, color = MaterialTheme.colors.onPrimary) }
            )
        }

        button("Open Application Data Folder", Icons.Filled.FolderOpen) {
            os.openFolder(mainFolder)
        }

        button("Save Data to Disk", Icons.Filled.Save) {
            save()
        }

        var confirmationScreen by remember { mutableStateOf(0) }
        button("Load Data from Disk", Icons.Filled.Upload) {
            confirmationScreen = 1
        }
        button("Delete All Data and Exit", Icons.Outlined.Delete, color = MaterialTheme.colors.error) {
            confirmationScreen = 2
        }

        when (confirmationScreen) {
            1 -> LoadConfirmationPopup { confirmationScreen = 0 }
            2 -> DeleteConfirmationPopup { confirmationScreen = 0 }
        }
    }
}

// Confirmation screens

@Composable
private fun LoadConfirmationPopup(exit: () -> Unit) {
    Dialog(onDismissRequest = exit) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Are you sure you want to load data from disk?")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { load(); exit() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                    ) {
                        Text("Yes")
                    }

                    Spacer(Modifier.width(6.dp))
                    Button(onClick = exit) {
                        Text("No")
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationPopup(exit: () -> Unit) {
    Dialog(onDismissRequest = exit) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Are you sure you want to delete all data and exit?")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        removeShutdownHook("SaveOnExit")
                        @OptIn(ExperimentalPathApi::class)
                        mainFolder.deleteRecursively()
                        Thread {
                            Thread.sleep(50)
                            Runtime.getRuntime().exit(0)
                        }.start()
                    },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text("Yes")
                    }

                    Spacer(Modifier.width(6.dp))
                    Button(onClick = exit) {
                        Text("No")
                    }
                }
            }
        }
    }
}