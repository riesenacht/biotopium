plugins {
    kotlin("multiplatform")
}

val jvmTargetVersion: String by project
val biotopiumCoreArtifactId: String by project
val biotopiumSubmoduleGroupId: String by project
val biotopiumVersion: String by project

group = biotopiumSubmoduleGroupId
version = biotopiumVersion

repositories {
    mavenCentral()
}

kotlin {
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
                implementation(project(":${biotopiumCoreArtifactId}"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":${biotopiumCoreArtifactId}"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }

        }
    }
}
