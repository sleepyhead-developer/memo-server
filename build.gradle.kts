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
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation ("com.google.firebase:firebase-admin:8.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.hibernate:hibernate-core:5.5.6.Final")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation ("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	implementation("io.springfox",  "springfox-swagger-ui", "2.9.2")
	implementation("io.springfox", "springfox-swagger2", "2.9.2")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.5.10")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.5.10")
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
