import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractJPackageTask
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
        val desktopMain by getting {
            dependencies {
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

                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

afterEvaluate {
    tasks.withType<AbstractJPackageTask> {
        destinationDir = rootProject.file("build/dist/${destinationDir.asFile.get().name}")
    }
}

compose.desktop {
    application {
        mainClass = "org.ryecountryday.samandrhys.cruvna.ui.Main"

        nativeDistributions {
            targetFormats(TargetFormat.Pkg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Cruvna"
            vendor = "RCDS"
            copyright = "Copyright ${LocalDate.now().year} Rhys and Sam. All rights reserved."
            packageVersion = project.version.toString()
            macOS {
                dockName = "Cruvná"
                bundleID = "${project.group}.${rootProject.name}"
                iconFile = rootProject.file("src/main/resources/icon.icns")

                signing {

                }
            }
            windows {
                iconFile = rootProject.file("src/main/resources/icon.ico")
            }
            linux {
                iconFile = rootProject.file("src/main/resources/icon.png")
            }
        }

        buildTypes.release {
            proguard {
                version = "7.5.0"
                configurationFiles.from(rootProject.file("guard.pro"))
                optimize = false
            }
        }
    }
}
