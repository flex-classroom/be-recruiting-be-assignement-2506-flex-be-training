/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

import com.linecorp.support.project.multi.recipe.configureByTypeHaving
import com.linecorp.support.project.multi.recipe.configureByTypePrefix
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    base
    jacoco
    `jvm-test-suite`
    `jacoco-report-aggregation`
    java
    `java-library`
    `maven-publish`
    id("com.linecorp.build-recipe-plugin") version "1.1.1"
    id("com.ryandens.javaagent-jib") version "0.5.1" apply false
    id("org.springframework.boot") version "3.4.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("com.google.cloud.tools.jib") version "3.4.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.4"

    val kotlinVersion = "2.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

val kotlinVersion = "1.9.24"
extra["kotlin.version"] = kotlinVersion

allprojects {
    val dependencyGroups = mapOf(
        "com.squareup.retrofit2" to "2.11.0",
        "com.squareup.okhttp3" to "4.12.0",
        "org.testcontainers" to "1.20.4",
        "org.springdoc" to "2.6.0",
        "com.mysql" to "9.0.0",
        "com.h2database.h2" to "2.2.224",
    )

    configurations.all {
        resolutionStrategy.eachDependency {
            dependencyGroups[requested.group]?.also { constraintVersion ->
                useVersion(constraintVersion)
                because("custom dependency group")
            }
        }
    }
}

configureByTypePrefix("kotlin") {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "jacoco-report-aggregation")
    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val javaVersion = "21"

    java {
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
        withSourcesJar()
    }

    testing {
        suites {
            val test by getting(JvmTestSuite::class) {
                useJUnitJupiter()

                targets {
                    all {
                        testTask.configure {
                            testLogging {
                                events = mutableSetOf(TestLogEvent.FAILED)
                                exceptionFormat = TestExceptionFormat.FULL
                            }
                        }
                    }
                }
            }

            val integrationTest by registering(JvmTestSuite::class) {
                useJUnitJupiter()
                testType.set(TestSuiteType.INTEGRATION_TEST)

                kotlin.target {
                    compilations.getByName("integrationTest")
                        .associateWith(kotlin.target.compilations.getByName("main"))
                }

                targets {
                    all {
                        testTask.configure {
                            shouldRunAfter(test)
                            testLogging {
                                events = mutableSetOf(TestLogEvent.FAILED)
                                exceptionFormat = TestExceptionFormat.FULL
                            }
                        }
                    }
                }
            }
        }
    }

    val integrationTestImplementation by configurations.getting {
        extendsFrom(configurations.testImplementation.get())
    }

    tasks {
        withType<JavaCompile> {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.fromTarget(javaVersion)
                freeCompilerArgs = listOf(
                    "-Xjsr305=strict",
                    "-Xjvm-default=all",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xemit-jvm-type-annotations",
                )
            }
        }

        val check by getting {
            dependsOn("integrationTest")
        }
    }

    dependencies {
        implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
        implementation(enforcedPlatform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.1"))

        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib"))

        implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

        testImplementation(enforcedPlatform(SpringBootPlugin.BOM_COORDINATES))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.4"))
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    }
}

configureByTypeHaving("dependencies") {
    dependencies {
        implementation(enforcedPlatform(SpringBootPlugin.BOM_COORDINATES))
    }
}

configureByTypeHaving("boot") {
    dependencies {
        implementation(enforcedPlatform(SpringBootPlugin.BOM_COORDINATES))
        implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.4"))

        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    }
}

configureByTypeHaving("kotlin", "boot") {
    apply(plugin = "kotlin-spring")
}

configureByTypeHaving("boot", "mvc") {
    dependencies {
        implementation("org.springframework.security:spring-security-core")
        implementation("org.springframework.boot:spring-boot-starter-web")

        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    }
}

configureByTypeHaving("boot", "jdbc", "repository") {
    dependencies {
        api("org.springframework.boot:spring-boot-starter-data-jdbc")
    }
}

configureByTypeHaving("boot", "application") {
    apply(plugin = "org.springframework.boot")

    dependencies {
        implementation("io.micrometer:micrometer-tracing-bridge-otel")
    }
}

configureByTypeHaving("boot", "mvc", "application") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
    }
}
