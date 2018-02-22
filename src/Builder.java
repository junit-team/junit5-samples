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
@SuppressWarnings("unused")
class Builder {

	int run(String directory, String executable, String... args) {
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
			return process.waitFor();
		} catch (Throwable throwable) {
			System.err.printf("%n%n%n| %s failed to build!%n", directory);
			throwable.printStackTrace(System.err);
			return 1;
		}
	}

	int checkLicense() throws Exception {
		List<String> expected = Files.readAllLines(Paths.get("src/eclipse-public-license-2.0.java"));
		expected.forEach(System.out::println);
		int error = 0;
		int count = 0;
		List<Path> paths = Files.walk(Paths.get("."))
				.filter(path -> path.getFileName().toString().endsWith(".java"))
				.collect(Collectors.toList());
		for (Path path : paths) {
			int current = checkLicense(path, expected);
			if (current != 0) {
				System.err.printf("| %s%n", path);
				count++;
			}
			error += current;
		}
		if (count > 0) {
			System.err.printf("| %d file(s) with no or false license.%n", count);
		}
		return error;
	}


	private int checkLicense(Path path, List<String> expected) throws Exception {
		List<String> actual = Files.readAllLines(path);
		if (actual.size() >= expected.size()) {
			actual = actual.subList(0, expected.size());
			return actual.equals(expected) ? 0 : 1;
		}
		return 1;
	}
}
