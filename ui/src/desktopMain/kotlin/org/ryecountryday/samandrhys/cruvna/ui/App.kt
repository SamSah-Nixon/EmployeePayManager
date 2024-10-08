/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import org.ryecountryday.samandrhys.cruvna.ui.admin.AdminScreen

/**
 * The main entry point for the application. This is called by [main]
 * and holds the outer ui behind [AdminScreen] and [ClockInScreen].
 */
@Preview
@Composable
fun App() {
    var admin by remember { mutableStateOf(false) } // remember keeps this state across recompositions

    if (admin) {
        AdminScreen(employees, exitFunc = { admin = false })
    } else {
        ClockInScreen(employees, onAdminButtonClicked = { admin = true })
    }
}