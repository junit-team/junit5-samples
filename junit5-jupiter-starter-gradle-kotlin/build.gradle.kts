plugins {
	kotlin("jvm") version "1.2.41"
}

repositories {
	mavenCentral()
}

dependencies {
	testCompile("org.junit.jupiter:junit-jupiter-api:5.3.0-RC1")
	testCompile("org.junit.jupiter:junit-jupiter-params:5.3.0-RC1")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.0-RC1")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<Wrapper> {
	gradleVersion = "4.8"
}
