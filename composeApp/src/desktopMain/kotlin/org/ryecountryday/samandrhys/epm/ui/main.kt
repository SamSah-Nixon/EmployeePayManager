@file:JvmName("Main")

package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.json.Json
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.util.addShutdownHook
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

// Kotlin doesn't have a concept of static-ness, so we have to do this to run code on startup
private val staticInitializer: Unit = run {
    System.setProperty("apple.awt.application.name", "EmployeePayManager")
    System.setProperty("apple.awt.application.appearance", "system")
}

val json = Json(Json.Default) {
    this.prettyPrint = true
}

val mainFolder: Path = Path(System.getProperty("user.home")).resolve(".EmployeePayManager").also {
    if (!it.exists()) {
        it.createDirectories()
    }
}

val employeesFile: Path = mainFolder.resolve("employees.json").also {
    if (!it.exists()) {
        it.writeText(json.encodeToString(EmployeeContainer.serializer(), EmployeeContainer()), Charsets.UTF_8, StandardOpenOption.CREATE)
    }
}

val employees = EmployeeContainer().apply {
    this.addChangeListener {
        employeesFile.writeText(
            json.encodeToString(EmployeeContainer.serializer(), it)
        )
    }

    addShutdownHook { // super lazy way to save on exit but it yk works
        println("Exiting!")
        employeesFile.writeText(
            json.encodeToString(EmployeeContainer.serializer(), this)
        )
    }

    if(employeesFile.exists()) {
        val container = json.decodeFromString(EmployeeContainer.serializer(), employeesFile.readText())
        this.addAll(container)
    }
}

fun main() = application {
    staticInitializer.toString() // trick the compiler into thinking we're using the staticInitializer
    Window(
        onCloseRequest = ::exitApplication,
        title = "EmployeePayManager",
        content = {
            MaterialTheme(
                colors = if (isSystemInDarkTheme())
                    darkColors()
                else
                    lightColors()
            ) {
                App()
            }
        }
    )
}