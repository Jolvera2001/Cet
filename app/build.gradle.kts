import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

group = "com.jolvera"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(compose.material3)

    // DI
    implementation("io.insert-koin:koin-core-coroutines")
    implementation("io.insert-koin:koin-compose")

    // modules
    implementation(project(":core"))
    runtimeOnly(project(":editor-plugin"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

compose {
    desktop {
        application {
            mainClass = "MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "Cet"
                packageVersion = "1.0.0"
            }
        }
    }
}
