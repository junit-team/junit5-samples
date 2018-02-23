/*
 * Copyright 2015-2018 the original author or authors.
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
import java.util.stream.Collectors;

/**
 * Platform-agnostic builder used by {@code build-all-samples.jsh}.
 */
@SuppressWarnings({ "WeakerAccess", "SameParameterValue" })
class Builder {

	public static void main(String[] args) {
		int status = new Builder().build();
		if (status != 0) {
			throw new AssertionError("Expected exit status of zero, but got: " + status);
		}
	}

	int status = 0;

	int build() {
		System.out.printf("|%n| Building all samples...%n|%n");
		run(".", "java", "--version");
		checkLicense("src/eclipse-public-license-2.0.java", ".java");
		run("junit5-vanilla-gradle", "gradlew", "clean", "test");
		run("junit5-vanilla-maven", "mvnw", "clean", "test");
		run("junit5-vanilla-modules", "jshell", "build.jsh");
		run("junit5-gradle-consumer", "gradlew", "clean", "test");
		run("junit5-maven-consumer", "mvnw", "clean", "test");
		run("junit5-mockito-extension", "gradlew", "clean", "test");
		run("junit5-java9-engine", "gradlew", "clean", "test");
		System.out.printf("%n%n%n|%n| Done. Build exits with status = %d.%n|%n", status);
		return status;
	}

	void run(String directory, String executable, String... args) {
		if (status != 0) {
			return;
		}
		System.out.printf("%n%n%n|%n| %s%n|%n", directory);
		System.out.printf("| %s %s%n|%n", executable, String.join(" ", args));
		Path path = Paths.get(directory);
		boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
		if (!isWindows) {
			if (Files.isExecutable(path.resolve(executable))) {
				executable = "./" + executable;
			}
		}
		ProcessBuilder processBuilder = new ProcessBuilder(isWindows ? "cmd.exe" : executable);
		if (isWindows) {
			processBuilder.command().add("/C");
			processBuilder.command().add(executable);
		}
		Arrays.stream(args).forEach(processBuilder.command()::add);
		processBuilder.directory(path.toFile());
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			process.getInputStream().transferTo(System.out);
			status = process.waitFor();
		} catch (Exception exception) {
			System.err.printf("%n%n%n| %s failed to build!%n", directory);
			exception.printStackTrace(System.err);
			status = 1;
		}
	}

	void checkLicense(String blueprint, String extension) {
		if (status != 0) {
			return;
		}
		try {
			List<String> expected = Files.readAllLines(Paths.get(blueprint));
			expected.forEach(System.out::println);
			int count = 0;
			List<Path> paths = Files.walk(Paths.get(".")).filter(path -> path.getFileName().toString().endsWith(extension)).collect(Collectors.toList());
			for (Path path : paths) {
				if (!checkLicense(path, expected)) {
					System.err.printf("| %s%n", path);
					count++;
				}
			}
			if (count > 0) {
				System.err.printf("| %d file(s) with no or false license.%n", count);
				status = 1;
			}
		}
		catch (Exception exception) {
			System.err.printf("%n%n%n| License `%s` check failed!%n", blueprint);
			exception.printStackTrace(System.err);
			status = 1;
		}
	}

	boolean checkLicense(Path path, List<String> expected) throws Exception {
		List<String> actual = Files.readAllLines(path);
		if (actual.size() >= expected.size()) {
			actual = actual.subList(0, expected.size());
			return actual.equals(expected);
		}
		return false;
	}
}
