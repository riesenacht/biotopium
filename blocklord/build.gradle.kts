plugins {
    kotlin("multiplatform")
    id("application")
}

val jvmTargetVersion: String by project
val reaktiveVersion: String by project
val biotopiumCoreArtifactId: String by project
val biotopiumLoggingArtifactId: String by project
val biotopiumNetworkArtifactId: String by project
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
                implementation(project(":${biotopiumLoggingArtifactId}"))
                implementation(project(":${biotopiumCoreArtifactId}"))
                implementation(project(":${biotopiumNetworkArtifactId}"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
                implementation("com.badoo.reaktive:reaktive:$reaktiveVersion")
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

tasks.register("runBlocklord") {
    group = "run"
    dependsOn(tasks.getByPath("run"))
}

application {
    mainClass.set("ch.riesenacht.biotopium.blocklord.BlocklordKt")
}