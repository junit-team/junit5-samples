//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

if (Files.notExists(Paths.get("bin/main-jars"))) {
	exe("jshell", "compile.jsh");
}

//
// [test] compile test modules and run all tests on the module-path
//
Arguments args = new Arguments()
args.add("-d").add("bin/test-patch-compile")
args.add("--module-path").addPath("bin/main-jars", "lib")
args.add("--patch-module").add("com.example.application=src/main/com.example.application")
args.add("--patch-module").add("com.example.tool=src/main/com.example.tool")
args.add("--patch-module").add("ice.cream=src/main/ice.cream")
args.add("--module-source-path").add("src/test")
args.addAllFiles("src/test", ".java")
run("javac", args.toArray())

args = new Arguments()
args.add("--module-path").addPath("bin/test-patch-compile", "bin/main-jars", "lib")
args.add("--add-modules").add("ALL-MODULE-PATH,ALL-DEFAULT")
args.add("--add-opens").add("org.junit.platform.engine/org.junit.platform.engine.support.filter=net.jqwik.engine")
args.add("--add-opens").add("org.junit.platform.commons/org.junit.platform.commons.util=net.jqwik.engine")
args.add("--module").add("org.junit.platform.console")
args.add("--reports-dir").add("bin/test-patch-compile-results/junit-platform")
args.add("--scan-modules")
exe("java", args.toArray())

/exit
