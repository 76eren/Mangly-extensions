plugins {
    id("com.android.library") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20" apply false
    kotlin("jvm") version "2.1.20" apply false
}

repositories {
    google()
    mavenCentral()
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}