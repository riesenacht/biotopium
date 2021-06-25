plugins {
    kotlin("multiplatform")
}

val jvmTargetVersion: String by project
val biotopiumP2pArtifactId: String by project
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
                implementation(project(":${biotopiumP2pArtifactId}"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":${biotopiumP2pArtifactId}"))
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(project(":${biotopiumP2pArtifactId}"))
            }
        }
        val jsTest by getting
    }
}
