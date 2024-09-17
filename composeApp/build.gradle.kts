import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")
    
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
        mainClass = "org.ryecountryday.samandrhys.epm.ui.Main"

        nativeDistributions {
            targetFormats(TargetFormat.Pkg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "EPM"
            packageVersion = project.version.toString()
            macOS.iconFile = rootProject.file("src/main/resources/icon.icns")
            windows.iconFile = rootProject.file("src/main/resources/icon.ico")
            linux.iconFile = rootProject.file("src/main/resources/icon.png")
        }
    }
}
