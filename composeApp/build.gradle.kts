import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask
import java.time.LocalDate

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(rootProject)
            implementation(libs.kotlinx.serialization.json)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

afterEvaluate {
    tasks.withType<AbstractProguardTask> {
        proguardVersion = "7.5.0"
        enabled = false // proguard is broken for some reason, maybe figure it out later
    }
}

compose.desktop {
    application {
        mainClass = "org.ryecountryday.samandrhys.cruvna.ui.Main"

        nativeDistributions {
            targetFormats(TargetFormat.Pkg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Cruvná"
            vendor = "RCDS"
            copyright = "Copyright ${LocalDate.now().year} Rhys and Sam. All rights reserved."
            packageVersion = project.version.toString()
            macOS.bundleID = project.group.toString()
            macOS.iconFile = rootProject.file("src/main/resources/icon.icns")
            windows.iconFile = rootProject.file("src/main/resources/icon.ico")
            linux.iconFile = rootProject.file("src/main/resources/icon.png")
        }
    }
}
