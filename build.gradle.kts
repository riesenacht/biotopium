import com.soywiz.korge.gradle.*

plugins {
    kotlin("multiplatform")
    id("com.soywiz.korge")
}

val biotopiumGroupId: String by project
val biotopiumArtifactId: String by project
val biotopiumVersion: String by project
val biotopiumCoreArtifactId: String by project
val jvmTargetVersion: String by project

group = biotopiumGroupId
version = biotopiumVersion

repositories {
    mavenCentral()
}

korge {
    id = "${biotopiumGroupId}.${biotopiumArtifactId}"

    targetJvm()
    targetJs()
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
                implementation(project(":$biotopiumCoreArtifactId"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(devNpm("process", "0.11.10"))
            }

        }
        val jsTest by getting
        val jvmMain by getting
        val jvmTest by getting
    }
}