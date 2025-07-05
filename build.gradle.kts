// vars
val kotestVersion by extra("6.0.0.M4")
val koinVersion by extra("4.0.3")

plugins {
    kotlin("jvm") apply false
    id("org.jetbrains.compose") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        val implementation by configurations
        val testImplementation by configurations

        // misc
        implementation("com.darkrockstudios:mpfilepicker:3.1.0")

        // coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

        // Common DI
        implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koinVersion"))
        implementation("io.insert-koin:koin-core")

        // Common testing
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.mockk:mockk:1.14.2")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
        testImplementation("io.insert-koin:koin-test")
        testImplementation("io.insert-koin:koin-test-junit5")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}