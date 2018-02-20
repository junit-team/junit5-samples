//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

// Build all sample projects

int run(String directory, String executable, String... args) {
    try {
        System.out.printf("%n%n%n[ %s ] executable=%s args=%s%n%n", directory, executable, Arrays.asList(args));
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder processBuilder = new ProcessBuilder(isWindows ? "cmd.exe" : "./" + executable);
        if (isWindows) {
            processBuilder.command().add("/C");
            processBuilder.command().add(executable);
        }
        Arrays.stream(args).forEach(processBuilder.command()::add);
        processBuilder.directory(new File(directory));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.getInputStream().transferTo(System.out);
        return process.waitFor();
    } catch (Throwable throwable) {
        System.err.printf("%n%n%n[ %s ] failed to build!%n", directory);
        throwable.printStackTrace(System.err);
        return 1;
    }
}

int error = run(".", "java", "--version")
if (error == 0) {
  error = run("junit5-vanilla-gradle", "gradlew", "test", "--no-daemon");
}
if (error == 0) {
  error = run("junit5-vanilla-maven", "mvnw", "test");
}
if (error == 0) {
  error = run("junit5-gradle-consumer", "gradlew", "test", "--no-daemon");
}
if (error == 0) {
  error = run("junit5-maven-consumer", "mvnw", "test");
}
if (error == 0) {
  error = run("junit5-mockito-extension", "gradlew", "test", "--no-daemon");
}
if (error == 0) {
  error = run("junit5-java9-engine", "gradlew", "test", "--no-daemon");
}
//if (error == 0) {
//  error = run("junit5-vanilla-modules", "jshell", "build.jsh");
//}

System.out.printf("%n%n%n[ . ] Done. (error = %d)", error)

/exit error
