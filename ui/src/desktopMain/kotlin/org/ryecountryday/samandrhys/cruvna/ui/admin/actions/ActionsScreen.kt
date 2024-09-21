/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */
package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import org.ryecountryday.samandrhys.cruvna.ui.*

/**
 * Wrapper with tabs that lead to [PayrollScreen], [EditScreen], and [FilesScreen].
 */
@Composable
fun ActionsScreen() {
    CoolTabRow(
        mapOf(
            ("Payroll" to Icons.Filled.Money) to { PayrollScreen() },
            ("Edit Work History" to Icons.Filled.HistorySettings) to { EditScreen() },
            ("Files" to Icons.Filled.Storage) to { FilesScreen() },
        ),
        defaultTab = 1,
        indicatorColor = MaterialTheme.colors.secondaryVariant
    )
}