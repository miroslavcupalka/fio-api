plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()

    // iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // JS target
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:3.1.3")
                implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1-0.6.x-compat")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-java:3.1.3")
            }
        }

        // Vytvoření iOS source setu hierarchie
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:3.1.3")
            }
        }

        // Propojení konkrétních iOS targetů s iosMain
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:3.1.3")
            }
        }
    }
}
