/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

@file:JvmName("Main")
@file:OptIn(ExperimentalSerializationApi::class, ExperimentalPathApi::class)

package org.ryecountryday.samandrhys.cruvna.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.SystemTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.ryecountryday.samandrhys.cruvna.backend.EmployeeContainer
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employees
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.util.*
import java.nio.file.FileSystemException
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

/*
 * The main file, contains the main function and other miscellaneous setup code.
 * Also contains the paths for all the files the application uses.
 */

// if unknown, use system theme
private val themeOverride = SystemTheme.Unknown

// Kotlin doesn't have a concept of static-ness, so we have to do this to run code on startup
private val setupTheming: Unit = run {
    System.setProperty("apple.awt.application.name", "Cruvná")
    System.setProperty("apple.awt.application.appearance", when (themeOverride) {
        SystemTheme.Dark -> "NSAppearanceNameDarkAqua"
        SystemTheme.Light -> "NSAppearanceNameAqua"
        SystemTheme.Unknown -> "system"
    })
}

/**
 * The old main folder for the application's long-term storage. This is used to migrate data from the old location to the new one.
 */
val oldMainFolders: List<Path> = listOf(
    Path(System.getProperty("user.home")).resolve(".EmployeePayManager"),
    os.applicationDataFolder.resolve("EmployeePayManager"),
    os.applicationDataFolder.resolve("EmployeePayManager").resolve("EmployeePayManager"),
    os.applicationDataFolder.resolve("EmployeePayManager").resolve(".EmployeePayManager")
)

/**
 * The main folder for the application's long-term storage.
 */
val mainFolder: Path = os.applicationDataFolder.resolve("cruvna")

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
val employees = EmployeeContainer(Employees.ADMIN)

/**
 * Saves [employees] and [WorkHistory] to their respective files.
 */
fun save() {
    json.encodeToStream(employees, employeesFile.outputStream())
    WorkHistory.save(workHistoryFile)
}

/**
 * Loads [employees] and [WorkHistory] from their respective files. If the main folder doesn't exist, it is created.
 * Data is also migrated from legacy locations to the new one.
 */
fun load() {
    if(!mainFolder.isDirectory()) mainFolder.deleteIfExists()
    if (!mainFolder.exists()) {
        println("creating $mainFolder")
        mainFolder.createDirectories()

        if(!mainFolder.exists()) {
            throw FileSystemException("Failed to create main folder at $mainFolder, do you have permission to write there?")
        }
    }

    for(oldMainFolder in oldMainFolders) {
        if(oldMainFolder.exists() && os != OperatingSystem.LINUX) { // on linux old folder is the same as the new one
            // copy the old folder to the new one
            oldMainFolder.copyToRecursively(mainFolder, overwrite = false, followLinks = true)
            oldMainFolder.deleteRecursively()
        }
    }

    if(employeesFile.isDirectory()) employeesFile.deleteRecursively()
    if (!employeesFile.exists()) {
        employeesFile.writeText("{}", Charsets.UTF_8, StandardOpenOption.CREATE)

        if(!employeesFile.exists()) {
            throw FileSystemException("Failed to create employees file at $employeesFile, do you have permission to write there?")
        }
    }

    val newEmployees = json.decodeFromStream<EmployeeContainer>(employeesFile.inputStream())
    for(employee in newEmployees) {
        employees.replaceEmployee(employee)
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

private val setupLoadingAndSaving: Unit = run {
    addShutdownHook("SaveOnExit", ::save) // super lazy way to save on exit but yk it works
    load()
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
        title = "Cruvná",
        content = {
            MaterialTheme(
                colors =
                if(themeOverride == SystemTheme.Dark || (themeOverride == SystemTheme.Unknown && isSystemInDarkTheme()))
                    darkColors(
                        primary = Color(0xFF1C2A50), // rcds blue
                        primaryVariant = Color(0xFF1A2948), // slightly darker rcds blue
                        secondary = Color(0xFFFED456), // rcds gold
                        secondaryVariant = Color(0xFFCFAF18), // slightly darker rcds gold
                        onPrimary = Color.White,
                    )
                else lightColors(
                    primary = Color(0xFF1C2A50),
                    primaryVariant = Color(0xFF1A2948),
                    secondary = Color(0xFFFED456),
                    secondaryVariant = Color(0xFFCFAF18),
                    onPrimary = Color.White,
                )
            ) {
                App()
            }
        }
    )
}