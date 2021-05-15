import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.30"
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.7.2"))
	testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

// config JVM target to 1.8 for kotlin compilation tasks
tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions.jvmTarget = "1.8"
}
