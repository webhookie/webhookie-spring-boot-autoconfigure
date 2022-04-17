import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.6.6"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.6.10"
  kotlin("plugin.spring") version "1.6.10"
  kotlin("plugin.allopen") version "1.6.10"
  kotlin("kapt") version "1.6.10"
}

kapt.includeCompileClasspath = false

allOpen {
  annotation("com.webhookie.common.annotation.Open")
  // annotations("com.another.Annotation", "com.third.Annotation")
}

group = "com.webhookie"
version = "2.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "2021.0.1"
extra["springdocVersion"] = "1.5.12"

dependencies {
  api("io.projectreactor:reactor-tools")

  compileOnly(project(":webhookie-common"))
  compileOnly(project(":webhookie-security-autoconfigure"))
  compileOnly("org.springframework.boot:spring-boot-starter-webflux")
  compileOnly("org.springframework.boot:spring-boot-starter-security")
  compileOnly("org.springframework.boot:spring-boot-starter-actuator")
  compileOnly("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
  compileOnly("org.springframework.boot:spring-boot-starter-integration")
  compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
  compileOnly("io.projectreactor.kotlin:reactor-kotlin-extensions")
  compileOnly("org.jetbrains.kotlin:kotlin-reflect")
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  compileOnly("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

  compileOnly("org.springframework.boot:spring-boot-starter-oauth2-client")
  compileOnly("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

  compileOnly("org.springdoc:springdoc-openapi-webflux-ui:${property("springdocVersion")}")
  compileOnly("org.springdoc:springdoc-openapi-kotlin:${property("springdocVersion")}")
  compileOnly("org.springdoc:springdoc-openapi-security:${property("springdocVersion")}")

  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

springBoot {
  buildInfo()
}
