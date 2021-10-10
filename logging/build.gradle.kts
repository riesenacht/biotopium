plugins {
    kotlin("multiplatform")
}

val jvmTargetVersion: String by project
val biotopiumSubmoduleGroupId: String by project
val biotopiumVersion: String by project

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
                api("io.github.microutils:kotlin-logging:2.0.11")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.slf4j:slf4j-api:1.7.32")
                implementation("org.slf4j:slf4j-simple:1.7.32")
            }
        }

        val jsMain by getting
    }
}