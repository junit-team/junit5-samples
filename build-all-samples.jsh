//usr/bin/env jshell --show-version "$0" "$@"; exit $?

/open src/Builder.java

Builder builder = new Builder()

int error = 0
void run(String sample, String exec, String... args) {
   if (error == 0) error = builder.run(sample, exec, args);
}

run(".", "java", "--version")

error = builder.checkLicense()

run("junit5-vanilla-gradle", "gradlew", "clean", "test", "--no-daemon")
run("junit5-vanilla-maven", "mvnw", "clean", "test")
run("junit5-vanilla-modules", "jshell", "build.jsh")
run("junit5-gradle-consumer", "gradlew", "clean", "test", "--no-daemon")
run("junit5-maven-consumer", "mvnw", "clean", "test")
run("junit5-mockito-extension", "gradlew", "clean", "test", "--no-daemon")
run("junit5-java9-engine", "gradlew", "clean", "test", "--no-daemon")

System.out.printf("%n%n%n|%n| Done. Build exited with %d.%n|%n", error)

/exit error
