import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
	id ("org.jetbrains.kotlin.plugin.jpa") version "1.3.61"
	id ("org.jetbrains.kotlin.plugin.allopen") version "1.3.61"
}


allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

group = "com.sleepyhead"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.5.4")
	implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.4")
	implementation ("org.springframework.boot:spring-boot-configuration-processor:2.5.4")
	annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor:2.5.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.4")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation ("com.google.firebase:firebase-admin:8.0.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.4")
	implementation("org.hibernate:hibernate-core:5.5.7.Final")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client:2.7.3")
	implementation ("org.projectlombok:lombok:1.18.20")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.4")
	testImplementation("io.projectreactor:reactor-test:3.4.10")
	implementation("io.springfox",  "springfox-swagger-ui", "2.9.2")
	implementation("io.springfox", "springfox-swagger2", "2.9.2")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.5.10")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.5.10")
	implementation("org.springframework.boot:spring-boot-starter-security:2.5.4")
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
	
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
