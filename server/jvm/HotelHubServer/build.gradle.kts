import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")

    // for JDBI
    implementation("org.jdbi:jdbi3-core:3.38.0")
    implementation("org.jdbi:jdbi3-kotlin:3.38.0")
    implementation("org.jdbi:jdbi3-postgres:3.38.0")
    implementation("org.postgresql:postgresql:42.3.1")

    // To use Kotlin specific date and time functions
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")

    // To get password encode
    implementation("org.springframework.security:spring-security-core:6.0.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0")
    // To use WebTestClient on tests
    testImplementation("org.springframework.boot:spring-boot-starter-webflux:2.7.0")
    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
