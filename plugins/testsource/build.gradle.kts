import com.android.tools.r8.D8
import com.android.tools.r8.D8Command
import com.android.tools.r8.OutputMode
import org.gradle.kotlin.dsl.register

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.manglyextension.plugins.weebcentral"
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
val mergedJarsDir = layout.buildDirectory.dir("mergedJars")
val mergedJar = layout.buildDirectory.file("mergedJars/all-classes.jar")

tasks.register<Copy>("extractClassesJar") {
    group = "build"
    description = "Extracts classes.jar from the AAR"
    val aar = layout.buildDirectory.file("outputs/aar/${project.name}-release.aar")
    from(zipTree(aar))
    include("classes.jar")
    into(d8OutputDir)
    dependsOn("bundleReleaseAar")
}

tasks.register<Copy>("collectDependencyJars") {
    group = "build"
    description = "Collects all dependency JARs including plugin classes"

    doFirst {
        mergedJarsDir.get().asFile.mkdirs()
    }

    from(extractedClassesJar)

    from(configurations.getByName("releaseRuntimeClasspath").files.filter {
        it.extension == "jar" && !it.name.contains("android.jar")
    })

    into(mergedJarsDir.get().asFile)

    dependsOn("extractClassesJar")
}

tasks.register("mergeAllJars") {
    group = "build"
    description = "Merges all JARs into a single JAR"

    doLast {
        val outputJar = mergedJar.get().asFile
        outputJar.parentFile.mkdirs()

        ant.withGroovyBuilder {
            "jar"("destfile" to outputJar.absolutePath) {
                mergedJarsDir.get().asFile.listFiles()?.forEach { jar ->
                    if (jar.extension == "jar") {
                        "zipfileset"("src" to jar.absolutePath) {
                            "exclude"("name" to "META-INF/*.SF")
                            "exclude"("name" to "META-INF/*.DSA")
                            "exclude"("name" to "META-INF/*.RSA")
                            "exclude"("name" to "META-INF/versions/**")
                        }
                    }
                }
            }
        }
    }

    dependsOn("collectDependencyJars")
}

tasks.register("d8Dex") {
    group = "build"
    description = "Dexes all classes and dependencies using D8"

    doFirst {
        d8OutputDir.get().asFile.mkdirs()
    }

    doLast {
        D8.run(
            D8Command.builder()
                .addProgramFiles(mergedJar.get().asFile.toPath())
                .setOutput(d8OutputDir.get().asFile.toPath(), OutputMode.DexIndexed)
                .setMinApiLevel(21) // Set minimum API level
                .build())
    }

    dependsOn("mergeAllJars")
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