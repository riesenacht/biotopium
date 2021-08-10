plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val jvmTargetVersion: String by project
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
                implementation(project(":${biotopiumNetworkArtifactId}"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":${biotopiumNetworkArtifactId}"))

                // LazySodium Java, libsodium for JVM
                implementation("com.goterl:lazysodium-java:5.1.1")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(project(":${biotopiumNetworkArtifactId}"))

                // SHA-3 implementation for JavaScript (SHA-3 FIPS 202 standard)
                implementation(npm("sha3", "2.1.4"))

                // Port of TweetNaCl / NaCl
                implementation(npm("tweetnacl", "1.0.3"))
                implementation(npm("tweetnacl-util", "0.15.1"))
            }
        }
        val jsTest by getting
    }
}
