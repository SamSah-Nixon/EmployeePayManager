@file:JvmName("Main")
package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EmployeePayManager",
    ) {
        App()
    }
}