import com.android.tools.r8.D8
import com.android.tools.r8.D8Command
import com.android.tools.r8.OutputMode
import org.gradle.kotlin.dsl.register

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.manglyextension.plugins.testsource"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
    implementation("org.jsoup:jsoup:1.21.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

val d8OutputDir = layout.buildDirectory.dir("customDex")
val extractedClassesJar = layout.buildDirectory.file("customDex/classes.jar")

tasks.register<Copy>("extractClassesJar") {
    group = "build"
    description = "Extracts classes.jar from the AAR"
    val aar = layout.buildDirectory.file("outputs/aar/${project.name}-release.aar")
    from(zipTree(aar))
    include("classes.jar")
    into(d8OutputDir)
    dependsOn("bundleReleaseAar")
}

tasks.register("d8Dex") {
    group = "build"
    description = "Dexes the compiled classes using d8"

    doFirst {
        d8OutputDir.get().asFile.mkdirs()
    }

    doLast {
        D8.run(
            D8Command.builder()
                .addProgramFiles(extractedClassesJar.get().asFile.toPath())
                .setOutput(d8OutputDir.get().asFile.toPath(), OutputMode.DexIndexed)
                .build())
    }

    dependsOn("extractClassesJar")
}

tasks.register("buildPluginZip", Zip::class) {
    group = "build"
    description = "Builds a Mangly file for the plugin"

    archiveFileName.set("${project.name}-${project.version}.mangly")
    destinationDirectory.set(file("$rootDir/build/plugins"))

    // Include .dex files from d8 output
    from(d8OutputDir) {
        into("dex")
        include("**/*.dex")
    }

    from("plugin.json") {
        into("meta")
    }

    from("icon.svg") {
        into("meta")
    }

    project.version = "1.0"

    dependsOn(tasks.named("d8Dex"))
}

tasks.named("build") {
    dependsOn(tasks.named("buildPluginZip"))
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}