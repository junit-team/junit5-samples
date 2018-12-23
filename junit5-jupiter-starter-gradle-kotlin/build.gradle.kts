plugins {
	kotlin("jvm") version "1.2.41"
}

repositories {
	mavenCentral()
}

dependencies {
	testCompile("org.junit.jupiter:junit-jupiter-api:5.4.0-M1")
	testCompile("org.junit.jupiter:junit-jupiter-params:5.4.0-M1")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.0-M1")
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
