@file:JvmName("Main")
package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.json.Json
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import java.nio.file.Path
import kotlin.io.path.*

val json = Json(Json.Default) {
    this.prettyPrint = true
}

val mainFolder: Path = Path(System.getProperty("user.home")).resolve(".EmployeePayManager")
val employeesFile: Path = mainFolder.resolve("employees.json").also {
    if (!it.exists()) {
        it.parent.createDirectories()
        it.writeText(json.encodeToString(EmployeeContainer.serializer(), EmployeeContainer()))
    }
}

val employees = EmployeeContainer().apply {
    addChangeListener {
        employeesFile.writeText(
            json.encodeToString(EmployeeContainer.serializer(), it)
        )
    }

    Runtime.getRuntime().addShutdownHook(Thread { // super lazy way to save on exit but it works
        println("Exiting!")
        employeesFile.writeText(
            json.encodeToString(EmployeeContainer.serializer(), this)
        )
    })

    if(employeesFile.exists()) {
        val container = json.decodeFromString(EmployeeContainer.serializer(), employeesFile.readText())
        container.forEach { add(it) }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EmployeePayManager",
    ) {
        MaterialTheme {
            var showEmployeeList by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize()) {
                Button(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { showEmployeeList = !showEmployeeList }
                ) {
                    Icon(
                        imageVector = if (showEmployeeList) Icons.Filled.Person else Icons.Filled.TwoPeople,
                        contentDescription = "Switch to ${if (showEmployeeList) "Clock In" else "Employee List"}"
                    )
                }
            }

            if(showEmployeeList) {
                EmployeeList(employees)
            } else {
                ClockInScreen(employees)
            }
        }
    }
}