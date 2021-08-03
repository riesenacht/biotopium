
pluginManagement {
    val kotlinVersion: String by settings
    val korgePluginVersion: String by settings
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        google()
        maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "korge") {
                useModule("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:${korgePluginVersion}")
            }
        }
    }
    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

val biotopiumArtifactId: String by settings
val biotopiumCoreArtifactId: String by settings
val biotopiumNetworkArtifactId: String by settings
val biotopiumBlocklordArtifactId: String by settings

rootProject.name = biotopiumArtifactId
include(biotopiumCoreArtifactId)
include(biotopiumNetworkArtifactId)
include(biotopiumBlocklordArtifactId)
