plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val jvmTargetVersion: String by project
val kotlinxSerializationJsonVersion: String by project
val reaktiveVersion: String by project
val biotopiumLoggingArtifactId: String by project
val biotopiumNetworkArtifactId: String by project
val biotopiumSubmoduleGroupId: String by project
val biotopiumVersion: String by project
val kotlinVersion: String by project

group = biotopiumSubmoduleGroupId
version = biotopiumVersion

repositories {
    mavenCentral()
}

kotlin {

    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = jvmTargetVersion
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.badoo.reaktive:reaktive:1.2.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}