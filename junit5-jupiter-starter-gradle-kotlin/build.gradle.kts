plugins {
	kotlin("jvm") version "1.2.41"
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter:5.4.0-M1")
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
