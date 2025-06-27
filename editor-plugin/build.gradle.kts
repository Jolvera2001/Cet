plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.jolvera"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // compose
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation("androidx.compose.material:material-icons-core:1.7.8")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // DI
    implementation("io.insert-koin:koin-core-coroutines")
    implementation("io.insert-koin:koin-compose")

    // editor placeholder
    implementation("com.fifesoft:rsyntaxtextarea:3.1.3")

    // module
    implementation(project(":core"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}