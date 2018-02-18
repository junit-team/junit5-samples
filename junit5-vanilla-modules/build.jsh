//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

exec("java", "--version")

del("bin")

// compile main modules
run("javac", "-d", "bin/main", "--module-source-path", "src/main", "--module", "org.openjdk.text")
run("javac", "-d", "bin/main", "--module-source-path", "src/main", "--module", "org.openjdk.hello")

// run hello application
exec("java", "--module-path", "bin/main", "--module", "org.openjdk.hello/org.openjdk.hello.Main", "brave new")

// download dependencies
load("lib", "org.apiguardian", "apiguardian-api", "1.0.0")
load("lib", "org.junit.jupiter", "junit-jupiter-api", "5.1.0")
load("lib", "org.junit.jupiter", "junit-jupiter-engine", "5.1.0")
load("lib", "org.junit.platform", "junit-platform-commons", "1.1.0")
load("lib", "org.junit.platform", "junit-platform-console", "1.1.0")
load("lib", "org.junit.platform", "junit-platform-engine", "1.1.0")
load("lib", "org.junit.platform", "junit-platform-launcher", "1.1.0")
load("lib", "org.opentest4j", "opentest4j", "1.0.0")

// compile test modules
List<String> args = new ArrayList<>()
args.addAll(List.of("-d", "bin/test"))
args.addAll(List.of("--module-path", String.join(File.pathSeparator, "bin/main", "lib")))
args.addAll(List.of("--patch-module", "org.openjdk.hello=src/main/org.openjdk.hello"))
args.addAll(List.of("--patch-module", "org.openjdk.text=src/main/org.openjdk.text"))
args.addAll(List.of("--module-source-path", "src/test"))
Files.walk(Paths.get("src/test")).filter(path -> path.getFileName().toString().endsWith(".java")).map(Object::toString).forEach(args::add)
run("javac", args.toArray(new String[0]))

// run tests
args.clear()
args.addAll(List.of("--module-path", String.join(File.pathSeparator, "bin/test", "bin/main","lib")))
args.addAll(List.of("--add-modules", "ALL-MODULE-PATH,ALL-DEFAULT"))
args.addAll(List.of("--module", "org.junit.platform.console"))
args.add("--scan-modules")
args.addAll(List.of("--reports-dir", "bin/test-results/junit-platform"))
exec("java", args.toArray(new String[0]))

/exit
