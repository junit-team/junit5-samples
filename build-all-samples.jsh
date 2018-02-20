//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

// Build all sample projects

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

System.out.printf("%n%n%n|%n| Building all samples...%n|%n");

int error = run(".", "java", "--version")
if (error == 0) {
	error = run("junit5-vanilla-gradle", "gradlew", "clean", "test", "--no-daemon");
}
if (error == 0) {
	error = run("junit5-vanilla-maven", "mvnw", "clean", "test");
}
if (error == 0) {
	error = run("junit5-gradle-consumer", "gradlew", "clean", "test", "--no-daemon");
}
if (error == 0) {
	error = run("junit5-maven-consumer", "mvnw", "clean", "test");
}
if (error == 0) {
	error = run("junit5-mockito-extension", "gradlew", "clean", "test", "--no-daemon");
}
if (error == 0) {
	error = run("junit5-java9-engine", "gradlew", "clean", "test", "--no-daemon");
}
//if (error == 0) {
//	error = run("junit5-vanilla-modules", "jshell", "build.jsh");
//}

System.out.printf("%n%n%n|%n| Done. (error = %d)%n|%n", error);

/exit error
