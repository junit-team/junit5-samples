/*
 * Copyright 2015-2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

// default package

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Platform-agnostic builder
 */
@SuppressWarnings({ "WeakerAccess", "SameParameterValue" })
class Builder {

	public static void main(String[] args) {
		var status = new Builder().build();
		if (status != 0) {
			throw new AssertionError("Expected exit status of zero, but got: " + status);
		}
	}

	int status = 0;

	int build() {
		System.out.printf("|%n| Building all samples...%n|%n");
		run(".", "java", "--version");
		checkLicense("src/eclipse-public-license-2.0.java", ".java", ".kt", ".scala", ".groovy");

		// jupiter-starter
		if (!isWindows()) { // TODO https://github.com/junit-team/junit5-samples/issues/66
			run("junit5-jupiter-starter-ant", "build.sh");
		}
		run("junit5-jupiter-starter-gradle", "gradlew", "test");
		run("junit5-jupiter-starter-gradle-groovy", "gradlew", "test");
		run("junit5-jupiter-starter-gradle-kotlin", "gradlew", "test");
		run("junit5-jupiter-starter-maven", "mvnw", "--batch-mode", "clean", "test");
		run("junit5-jupiter-starter-maven-kotlin", "mvnw", "--batch-mode", "clean", "test");
		run("junit5-jupiter-starter-bazel", "bazel", "test", "//...", "--test_output", "all");
		run("junit5-jupiter-starter-sbt", "sbt", "test");

		// jupiter-extensions
		run("junit5-jupiter-extensions", "gradlew", "test");

		// migration
		run("junit5-migration-gradle", "gradlew", "test");
		run("junit5-migration-maven", "mvnw", "--batch-mode", "clean", "test");
		run("junit5-multiple-engines", "gradlew", "test");

		// modular
		run("junit5-modular-world", "jshell", "build.jsh");

		System.out.printf("%n%n%n|%n| Done. Build exits with status = %d.%n|%n", status);
		return status;
	}

	void run(String directory, String executable, String... args) {
		if (status != 0) {
			return;
		}
		System.out.printf("%n%n%n|%n| %s%n|%n", directory);
		System.out.printf("| %s %s%n|%n", executable, String.join(" ", args));
		var path = Paths.get(directory);
		var isWindows = isWindows();
		if (!isWindows) {
			if (Files.isExecutable(path.resolve(executable))) {
				executable = "./" + executable;
			}
		}
		var processBuilder = new ProcessBuilder(isWindows ? "cmd.exe" : executable);
		if (isWindows) {
			processBuilder.command().add("/C");
			processBuilder.command().add(executable);
		}
		Arrays.stream(args).forEach(processBuilder.command()::add);
		processBuilder.directory(path.toFile());
		processBuilder.redirectErrorStream(true);
		try {
			var process = processBuilder.start();
			process.getInputStream().transferTo(System.out);
			status = process.waitFor();
		} catch (Exception exception) {
			System.out.printf("%n%n%n| %s failed to build!%n", directory);
			exception.printStackTrace(System.err);
			status = 1;
		}
	}

	boolean isWindows() {
		return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
	}

	void checkLicense(String blueprint, String... extensions) {
		if (status != 0) {
			return;
		}
		System.out.printf("%n%n%n|%n| check license: %s%n|%n", blueprint);
		try {
			var expected = Files.readAllLines(Paths.get(blueprint));
			var errors = 0;
			try (var paths = Files.walk(Paths.get("."))
					.filter(path -> Arrays.stream(extensions).anyMatch(extension -> path.getFileName().toString().endsWith(extension)))
					.filter(path -> !path.getFileName().toString().equals("MavenWrapperDownloader.java"))) {
				for (var path : paths.toList()) {
					if (checkLicense(path, expected)) {
						continue;
					}
					System.out.printf("| %s%n", path);
					errors++;
				}
			}
			if (errors > 0) {
				System.out.printf("| %d file(s) with no or false license.%n", errors);
				status = 1;
			}
		}
		catch (Exception exception) {
			System.out.printf("%n%n%n| License `%s` check failed!%n", blueprint);
			exception.printStackTrace(System.err);
			status = 1;
		}
	}

	boolean checkLicense(Path path, List<String> expected) throws Exception {
		var actual = Files.readAllLines(path);
		if (actual.size() >= expected.size()) {
			actual = actual.subList(0, expected.size());
			return actual.equals(expected);
		}
		return false;
	}
}
