plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
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
                implementation(libs.bundles.ktor.common)
                implementation(libs.bundles.kotlinx.common)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.bundles.test.common)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.java)
            }
        }

        // Vytvoření iOS source setu hierarchie
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        // Propojení konkrétních iOS targetů s iosMain
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}
