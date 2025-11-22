plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.liquibase)
}

group = "com.starter"
version = "0.0.1"

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


repositories {
    mavenCentral()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    api("org.springframework.boot:spring-boot-autoconfigure")
    api("org.springframework:spring-context")
    api("org.springframework.boot:spring-boot")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation(kotlin("test"))

    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("com.zaxxer:HikariCP")

    //liquibase
    runtimeOnly(libs.postgresql)
//    liquibaseRuntime(libs.liquibase.groovy.dsl)
//    implementation(libs.postgresql)
    implementation(libs.liquibase.core)
//    liquibaseRuntime(libs.liquibase.core)
//    liquibaseRuntime(libs.picocli)
}

tasks.test {
    useJUnitPlatform()
}