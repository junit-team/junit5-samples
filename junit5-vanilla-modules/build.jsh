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
String jupiterVersion = "5.1.0"
String platformVersion = "1.1.0"
load("lib", "org.apiguardian", "apiguardian-api", "1.0.0")
load("lib", "org.opentest4j", "opentest4j", "1.0.0")
load("lib", "org.junit.jupiter", "junit-jupiter-api", jupiterVersion)
load("lib", "org.junit.jupiter", "junit-jupiter-engine", jupiterVersion)
load("lib", "org.junit.platform", "junit-platform-commons", platformVersion)
load("lib", "org.junit.platform", "junit-platform-console", platformVersion)
load("lib", "org.junit.platform", "junit-platform-engine", platformVersion)
load("lib", "org.junit.platform", "junit-platform-launcher", platformVersion)

// compile test modules
Arguments args = new Arguments()
args.add("-d").add("bin/test")
args.add("--module-path").add(String.join(File.pathSeparator, "bin/main", "lib"))
args.add("--patch-module").add("org.openjdk.hello=src/main/org.openjdk.hello")
args.add("--patch-module").add("org.openjdk.text=src/main/org.openjdk.text")
args.add("--module-source-path").add("src/test")
args.addAllJavaFiles("src/test")
run("javac", args.toArray())

// run tests
args = new Arguments()
args.add("--module-path").add(String.join(File.pathSeparator, "bin/test", "bin/main","lib"))
args.add("--add-modules").add("ALL-MODULE-PATH,ALL-DEFAULT")
args.add("--module").add("org.junit.platform.console")
args.add("--scan-modules")
args.add("--reports-dir").add("bin/test-results/junit-platform")
exec("java", args.toArray())

/exit
