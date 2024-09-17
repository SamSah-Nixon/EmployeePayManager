import java.util.Calendar

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    idea
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.seriazliation)
    alias(libs.plugins.licenser)
}

allprojects {
    apply(plugin = "idea")
    apply(plugin = "org.cadixdev.licenser")
    idea {
        module {
            isDownloadSources = true
        }
    }

    version = "app_version"()
    group = "maven_group"()

    license {
        header(rootProject.file("HEADER"))
        include("**/*.kt")

        tasks {
            create("gradle") {
                include("*.gradle.kts")
            }
        }

        properties {
            this["YEAR"] = Calendar.getInstance().get(Calendar.YEAR).toString()
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.serialization.json)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    outputs.upToDateWhen { false }
}

operator fun String.invoke(): String = rootProject.ext[this] as? String ?: error("Property $this is not defined")