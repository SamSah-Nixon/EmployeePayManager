/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

@file:JvmName("Main")
@file:OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)

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
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.ryecountryday.samandrhys.epm.backend.EmployeeContainer
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.*
import java.nio.file.FileSystemException
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

/*
 * The main file, contains the main function and other miscellaneous setup code.
 * Also contains the paths for all the files the application uses.
 */

// if unknown, use system theme
private val themeOverride = SystemTheme.Light

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
 * The old main folder for the application's long-term storage. This is used to migrate data from the old location to the new one.
 */
val oldMainFolder: Path = Path(System.getProperty("user.home")).resolve(".EmployeePayManager")

/**
 * The main folder for the application's long-term storage.
 */
val mainFolder: Path = os.applicationDataFolder.resolve("EmployeePayManager")

/**
 * The file that stores [employees]. This is loaded from and saved to on startup and exit.
 */
val employeesFile: Path = mainFolder.resolve("employees.json")

/**
 * The file that stores the [WorkHistory] instance.
 */
val workHistoryFile: Path = mainFolder.resolve("workHistory.json")

/**
 * The list of employees. This is loaded from [employeesFile] on startup
 * and saved to it when it changes or the program exits.
 */
val employees = EmployeeContainer(Employee.ADMIN).apply {
    if(employeesFile.exists()) {
        val container = json.decodeFromStream<EmployeeContainer>(employeesFile.inputStream())
        this.addAll(container)
    }
}

private val setupLoadingAndSaving: Unit = run {
    addShutdownHook { // super lazy way to save on exit but it yk works
        json.encodeToStream(employees, employeesFile.outputStream())
        WorkHistory.save(workHistoryFile)
    }

    if(!mainFolder.isDirectory()) mainFolder.deleteIfExists()
    if (!mainFolder.exists()) {
        println("creating $mainFolder")
        mainFolder.createDirectories()

        if(!mainFolder.exists()) {
            throw FileSystemException("Failed to create main folder at $mainFolder, do you have permission to write there?")
        }
    }

    if(oldMainFolder.exists() && os != OperatingSystem.LINUX) { // on linux old folder is the same as the new one
        // copy the old folder to the new one
        oldMainFolder.copyToRecursively(mainFolder, overwrite = false, followLinks = true)

        @OptIn(ExperimentalPathApi::class)
        oldMainFolder.deleteRecursively()
    }

    if(employeesFile.isDirectory()) employeesFile.deleteRecursively()
    if (!employeesFile.exists()) {
        employeesFile.writeText("{}", Charsets.UTF_8, StandardOpenOption.CREATE)

        if(!employeesFile.exists()) {
            throw FileSystemException("Failed to create employees file at $employeesFile, do you have permission to write there?")
        }
    }

    if(workHistoryFile.isDirectory()) workHistoryFile.deleteRecursively()

    if (!workHistoryFile.exists()) {
        workHistoryFile.writeText("""{"currentPeriod":[],"clockedIn":[],"payPeriods":[]}""", Charsets.UTF_8, StandardOpenOption.CREATE)

        if(!workHistoryFile.exists()) {
            throw FileSystemException("Failed to create work history file at $workHistoryFile, do you have permission to write there?")
        }
    }

    WorkHistory.load(workHistoryFile)
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