plugins {
    kotlin("multiplatform") version "2.2.0" apply false
    kotlin("jvm") version "2.2.0" apply false
    kotlin("plugin.serialization") version "2.2.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}