import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    java
}

repositories {
    mavenCentral()
}

val jvmTargetVersion: String by project
val biotopiumSubmoduleGroupId: String by project
val biotopiumNetworkArtifactId: String by project
val biotopiumVersion: String by project

group = biotopiumNetworkArtifactId
version = biotopiumVersion

dependencies {
    implementation("com.github.jnr:jnr-ffi:2.2.4")
}

val libraryEnding = when {
    Os.isFamily(Os.FAMILY_WINDOWS) -> "dll"
    Os.isFamily(Os.FAMILY_UNIX) -> "so"
    else -> throw Exception("unknown OS")
}

val goModTidy = task<Exec>("goModTidy") {
    workingDir("src/main/go/gop2p")
    commandLine("go", "mod",  "tidy")
}

val goBuild = task<Exec>("goBuild") {
    //TODO enable again
    //dependsOn(goModTidy)
    workingDir("src/main/go/gop2p")
    val libraryName = "gop2p.$libraryEnding"
    val libraryPath = "${project.buildDir.absolutePath}${File.separator}$libraryName"
    commandLine("go", "build", "-o", libraryPath, "-buildmode", "c-shared")
}

tasks.named("build") {
    dependsOn(goBuild)
}
