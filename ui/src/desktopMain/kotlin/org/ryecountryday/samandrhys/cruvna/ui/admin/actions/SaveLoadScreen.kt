package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.ryecountryday.samandrhys.cruvna.ui.FolderOpen
import org.ryecountryday.samandrhys.cruvna.ui.load
import org.ryecountryday.samandrhys.cruvna.ui.mainFolder
import org.ryecountryday.samandrhys.cruvna.ui.save
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
        Button(
            onClick = { os.openFolder(mainFolder) },
            modifier = Modifier.padding(horizontal = 6.dp).width(500.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Open Application Data Folder")
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Outlined.FolderOpen,
                    contentDescription = "Open Application Data Folder"
                )
            }
        }

        Button(onClick = { save() }, modifier = Modifier.padding(horizontal = 6.dp).width(500.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Save Data to Disk")
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Save Data to Disk"
                )
            }
        }

        var confirmationScreen by remember { mutableStateOf(0) }
        Button(onClick = { confirmationScreen = 1 }, modifier = Modifier.padding(horizontal = 6.dp).width(500.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Load Data from Disk")
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Load Data from Disk"
                )
            }
        }

        Button(
            onClick = { confirmationScreen = 2 },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
            modifier = Modifier.padding(horizontal = 6.dp).width(500.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Delete All Data and Exit")
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete All Data and Exit"
                )
            }
        }

        when (confirmationScreen) {
            1 -> LoadConfirmationPopup { confirmationScreen = 0 }
            2 -> DeleteConfirmationPopup { confirmationScreen = 0 }
        }
    }
}

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
                        Runtime.getRuntime().exit(0)
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