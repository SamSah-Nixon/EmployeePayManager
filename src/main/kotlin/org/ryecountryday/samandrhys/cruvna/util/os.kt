/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.util

import java.nio.file.Path
import kotlin.io.path.Path

/**
 * An enum representing different operating systems that the program can run on.
 */
enum class OperatingSystem {
    WINDOWS,
    MAC,
    LINUX,
    UNKNOWN
}

/**
 * The operating system the program is running on.
 */
val os: OperatingSystem = when {
    System.getProperty("os.name").contains("mac", ignoreCase = true) -> OperatingSystem.MAC
    System.getProperty("os.name").contains("win", ignoreCase = true) -> OperatingSystem.WINDOWS
    System.getProperty("os.name").contains("nix", ignoreCase = true) -> OperatingSystem.LINUX
    else -> OperatingSystem.UNKNOWN
}

/**
 * The folder where applications' data is stored.
 */
val OperatingSystem.applicationDataFolder: Path
    get() = when (this) {
        OperatingSystem.MAC -> Path(System.getProperty("user.home")).resolve("Library/Application Support")
        OperatingSystem.WINDOWS -> Path(System.getenv("APPDATA"))
        OperatingSystem.LINUX -> Path(System.getProperty("user.home"))
        OperatingSystem.UNKNOWN -> throw UnsupportedOperationException("Unknown operating system")
    }

/**
 * Opens the folder at the given path in the system's file explorer.
 */
fun OperatingSystem.openFolder(path: Path) {
    val pb = ProcessBuilder().inheritIO()
    when (this) {
        OperatingSystem.MAC -> pb.command("open", "-R", path.toString())
        OperatingSystem.WINDOWS -> pb.command("explorer", path.toString())
        OperatingSystem.LINUX -> pb.command("xdg-open", path.toString())
        OperatingSystem.UNKNOWN -> throw UnsupportedOperationException("Unknown operating system")
    }

    pb.start()
}