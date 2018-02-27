//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

if (Files.notExists(Paths.get("bin/main-jars"))) {
	exe("jshell", "compile.jsh");
}

//
// [test-classpath] compile and run tests on the classpath
//
Arguments args = new Arguments()
args.add("-d").add("bin/test-classpath")
args.add("--class-path").addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib")
args.addAllFiles("src/test", path -> { String name = path.getFileName().toString(); return name.endsWith(".java") && !name.contains("module-info"); } )
run("javac", args.toArray())

args = new Arguments()
args.add("--class-path").addPath("bin/test-classpath", "bin/main-jars/*", "lib/*")
args.add("org.junit.platform.console.ConsoleLauncher")
args.add("--reports-dir").add("bin/test-classpath-results/junit-platform")
args.add("--scan-class-path")
exe("java", args.toArray())

/exit
