plugins {
	kotlin("jvm") version "1.2.41"
}

repositories {
	mavenCentral()
}

dependencies {
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
