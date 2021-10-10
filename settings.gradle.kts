pluginManagement {
    val kotlinVersion: String by settings
    val korgePluginVersion: String by settings
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.soywiz.korge") {
                useModule("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
            }
        }
    }
    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }

}

val biotopiumArtifactId: String by settings
val biotopiumLoggingArtifactId: String by settings
val biotopiumCoreArtifactId: String by settings
val biotopiumNetworkArtifactId: String by settings
val biotopiumBlocklordArtifactId: String by settings
val gop2pArtifactId: String by settings

rootProject.name = biotopiumArtifactId
include(biotopiumLoggingArtifactId)
include(biotopiumCoreArtifactId)
include(biotopiumNetworkArtifactId)
include(biotopiumBlocklordArtifactId)
include(":$biotopiumNetworkArtifactId:$gop2pArtifactId")
