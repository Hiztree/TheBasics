/*
 * MIT License
 *
 * Copyright (c) 2021 Levi Pawlak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.5.31"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-reflect:1.5.31"))
            exclude(dependency(":SpigotProcessor"))
        }

        destinationDir = file("C:/Users/Levi/Downloads/SpigotTest/plugins")
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
