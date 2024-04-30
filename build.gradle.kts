import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion = 17

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.papermc.paperweight.userdev") version "1.5.12"
    id("xyz.jpenilla.run-paper") version "2.2.2"
    id("de.nycode.spigot-dependency-loader") version "1.1.2"
}

group = "de.dqmme"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    // KSpigot dependency
    spigot("net.axay", "kspigot", "1.20.3")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjdk-release=$javaVersion",
            )
            jvmTarget = "$javaVersion"
        }
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }
    assemble {
        dependsOn(reobfJar)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
