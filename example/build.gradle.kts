
plugins {
    kotlin("jvm")  // Odebrání verze - použije se z root build.gradle.kts
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":fioapi"))
    implementation("io.ktor:ktor-client-java:3.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1-0.6.x-compat")
}

application {
    mainClass.set("cz.gpt.fioexample.MainKt")
}