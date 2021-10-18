plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val jvmTargetVersion: String by project
val kotlinxSerializationJsonVersion: String by project
val biotopiumSubmoduleGroupId: String by project
val biotopiumLoggingArtifactId: String by project
val biotopiumNetworkArtifactId: String by project
val gop2pArtifactId: String by project
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
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
                implementation(project(":$biotopiumLoggingArtifactId"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("libp2p", "0.31.7"))
                implementation(npm("libp2p-mplex", "0.10.4"))
                implementation(npm("libp2p-noise", "4.0.0"))
                implementation(npm("libp2p-bootstrap", "0.13.0"))
                implementation(npm("libp2p-webrtc-star", "0.23.0"))
                implementation(npm("libp2p-websockets", "0.16.1"))
                implementation(npm("libp2p-floodsub", "0.27.0"))
                implementation(npm("peer-id", "0.15.1"))
                implementation(npm("multiaddr", "10.0.0"))
            }
        }
        val jsTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(project(":$biotopiumNetworkArtifactId:$gop2pArtifactId"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
            }
        }
        val jvmTest by getting
    }
}
