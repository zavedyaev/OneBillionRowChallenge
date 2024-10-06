plugins {
    application
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.zavediaev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
tasks {
    // Configure the shadowJar task
    shadowJar {
        // Set the Main-Class attribute in the manifest of the shadow jar
        manifest {
            attributes["Main-Class"] = "com.zavediaev.MainKt"
        }
    }
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.zavediaev.MainKt")
}