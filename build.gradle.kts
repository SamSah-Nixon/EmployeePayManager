plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    idea
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    apply(plugin = "idea")
    idea {
        module {
            isDownloadSources = true
        }
    }

    version = "app_version"()
    group = "maven_group"()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("serialization"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

operator fun String.invoke(): String = rootProject.ext[this] as? String ?: error("Property $this is not defined")