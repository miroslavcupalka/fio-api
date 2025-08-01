plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.application)
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":fioapi"))
	implementation("io.ktor:ktor-client-java:3.1.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1-0.6.x-compat")
	implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
	mainClass.set("cz.gpt.fioexample.MainKt")
}