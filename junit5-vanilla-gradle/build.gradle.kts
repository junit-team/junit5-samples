plugins {
	java
}

repositories {
	mavenCentral()
}

dependencies {
	testCompile("org.junit.jupiter:junit-jupiter-api:5.1.0")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.1.0")
}

tasks.withType<Test> {
	useJUnitPlatform()

	testLogging {
		events("passed", "skipped", "failed")
	}

	reports {
		html.isEnabled = true
	}
}
