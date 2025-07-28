import java.net.URI

plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "com.mikhailovskii"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.mikhailovskii.MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.mikhailovskii.MainKt"
    }
}

repositories {
    mavenCentral()
    maven { url = URI.create("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.3.0")
    implementation("io.ktor:ktor-server-netty:3.2.2")
    implementation("io.ktor:ktor-server-core:3.2.2")
    implementation("io.ktor:ktor-server-content-negotiation:3.2.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}