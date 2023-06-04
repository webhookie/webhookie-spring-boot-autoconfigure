plugins {
  kotlin("jvm") version "1.8.21"
  kotlin("plugin.spring") version "1.8.21"
  kotlin("plugin.allopen") version "1.8.21"
  kotlin("kapt") version "1.8.21"
}

kapt.includeCompileClasspath = false

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

  compileOnly("org.reflections:reflections:${property("reflectionsVersion")}")

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
