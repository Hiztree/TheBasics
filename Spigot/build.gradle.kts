import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    java
}

group = "io.github.hiztree"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compile("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compile(project(":Core"))
}

tasks {
    named<ShadowJar>("shadowJar") {
        relocate("kotlin", "io.github.hiztree.kotlin")
        dependencies {
            exclude(dependency("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT"))
            exclude(dependency("com.google.guava:guava:21.0"))
            exclude(dependency(":SpigotProcessor"))
        }
        destinationDir = file("C:/Users/Levi/Downloads/Spigot/plugins")
        archiveBaseName.value("thebasics")
        archiveClassifier.value("spigot")
        minimize {
            exclude(dependency(":Core"))
        }
        mergeServiceFiles()
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
