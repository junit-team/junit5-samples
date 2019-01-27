import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.20"
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	// or compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testCompile("org.junit.jupiter:junit-jupiter-api:5.3.2")
	testCompile("org.junit.jupiter:junit-jupiter-params:5.3.2")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<Wrapper> {
	gradleVersion = "5.0"
}

// config JVM target to 1.8 for kotlin compilation tasks
tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}
