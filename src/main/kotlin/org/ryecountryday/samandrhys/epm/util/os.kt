package org.ryecountryday.samandrhys.epm.util

import java.nio.file.Path
import kotlin.io.path.Path

enum class OperatingSystem {
    WINDOWS,
    MAC,
    LINUX,
    UNKNOWN
}

val os: OperatingSystem = when {
    System.getProperty("os.name").contains("mac", ignoreCase = true) -> OperatingSystem.MAC
    System.getProperty("os.name").contains("win", ignoreCase = true) -> OperatingSystem.WINDOWS
    System.getProperty("os.name").contains("nix", ignoreCase = true) -> OperatingSystem.LINUX
    else -> OperatingSystem.UNKNOWN
}

val OperatingSystem.applicationDataFolder: Path
    get() = when (this) {
        OperatingSystem.MAC -> Path(System.getProperty("user.home")).resolve("Library/Application Support")
        OperatingSystem.WINDOWS -> Path(System.getenv("APPDATA")).resolve("EmployeePayManager")
        OperatingSystem.LINUX -> Path(System.getProperty("user.home")).resolve(".EmployeePayManager")
        OperatingSystem.UNKNOWN -> throw UnsupportedOperationException("Unknown operating system")
    }