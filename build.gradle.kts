import com.soywiz.korge.gradle.korge

plugins {
    kotlin("multiplatform")
    id("korge")
}

val biotopiumGroupId: String by project
val biotopiumArtifactId: String by project
val biotopiumVersion: String by project

group = biotopiumGroupId
version = biotopiumVersion

repositories {
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
    jcenter()
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
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            //useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting
        val jsTest by getting
        val jvmMain by getting
        val jvmTest by getting
    }
}

korge {
    id = "${biotopiumGroupId}.${biotopiumArtifactId}"
    targetJvm()
    targetJs()
}
