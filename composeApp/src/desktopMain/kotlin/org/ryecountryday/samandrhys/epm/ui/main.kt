/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

@file:JvmName("Main")
@file:OptIn(ExperimentalSerializationApi::class)

package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.SystemTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.addShutdownHook
import org.ryecountryday.samandrhys.epm.util.json
import java.nio.file.FileSystemException
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

// if unknown, use system theme
private val themeOverride = SystemTheme.Dark

// Kotlin doesn't have a concept of static-ness, so we have to do this to run code on startup
private val setupTheming: Unit = run {
    System.setProperty("apple.awt.application.name", "EmployeePayManager")
    System.setProperty("apple.awt.application.appearance", when (themeOverride) {
        SystemTheme.Dark -> "NSAppearanceNameDarkAqua"
        SystemTheme.Light -> "NSAppearanceNameAqua"
        SystemTheme.Unknown -> "system"
    })
}

/**
 * The main folder for the application's long-term storage.
 */
val mainFolder: Path = Path(System.getProperty("user.home")).resolve(".EmployeePayManager").also {
    if(!it.isDirectory()) it.deleteIfExists()
    if (!it.exists()) {
        println("creating $it")
        it.createDirectories()

        if(!it.exists()) {
            throw FileSystemException("Failed to create main folder at $it, do you have permission to write there?")
        }
    }
}

/**
 * The file that stores the list of employees.
 */
val employeesFile: Path = mainFolder.resolve("employees.json").also {
    @OptIn(ExperimentalPathApi::class)
    if(it.isDirectory()) it.deleteRecursively()

    if (!it.exists()) {
        it.writeText(json.encodeToString(EmployeeContainer()), Charsets.UTF_8, StandardOpenOption.CREATE)

        if(!it.exists()) {
            throw FileSystemException("Failed to create employees file at $it, do you have permission to write there?")
        }
    }
}

val workHistoryFile: Path = mainFolder.resolve("workHistory.json").also {
    @OptIn(ExperimentalPathApi::class)
    if(it.isDirectory()) it.deleteRecursively()

    if (!it.exists()) {
        it.writeText("""{"currentPeriod":[],"entries":[],"payPeriods":[]}""", Charsets.UTF_8, StandardOpenOption.CREATE)

        if(!it.exists()) {
            throw FileSystemException("Failed to create work history file at $it, do you have permission to write there?")
        }
    }
}

/**
 * The list of employees. This is loaded from [employeesFile] on startup
 * and saved to it when it changes or the program exits.
 */
val employees = EmployeeContainer().apply {
    if(employeesFile.exists()) {
        val container = json.decodeFromStream<EmployeeContainer>(employeesFile.inputStream())
        this.addAll(container)
    }
}

private val setupLoadingAndSaving: Unit = run {
    WorkHistory.load(workHistoryFile)

    addShutdownHook { // super lazy way to save on exit but it yk works
        json.encodeToStream(employees, employeesFile.outputStream())
        WorkHistory.save(workHistoryFile)
    }
}

/**
 * The main function. Calls [application] to start the application.
 */
fun main() = application {
    // trick the compiler into thinking we're using our "static initializer" variables - also prevents proguard from removing them
    setupTheming.serializer()
    setupLoadingAndSaving.serializer()

    Window(
        onCloseRequest = ::exitApplication,
        title = "EmployeePayManager",
        content = {
            MaterialTheme(
                colors =
                if(themeOverride == SystemTheme.Dark || (themeOverride == SystemTheme.Unknown && isSystemInDarkTheme()))
                    darkColors()
                else lightColors()
            ) {
                App()
            }
        }
    )
}