//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

if (Files.notExists(Paths.get("bin/main-jars"))) {
	exe("jshell", "compile.jsh");
}

//
// [test-patch-runtime] compile and run tests on the patched module-path
//
Arguments args = new Arguments()
args.add("-d").add("bin/test-patch-runtime/com.example.application")
args.add("--class-path").addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib")
args.addAllFiles("src/test/com.example.application", path -> { String name = path.getFileName().toString(); return name.endsWith(".java") && !name.contains("module-info"); } )
run("javac", args.toArray())
args = new Arguments()
args.add("-d").add("bin/test-patch-runtime/com.example.tool")
args.add("--class-path").addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib")
args.addAllFiles("src/test/com.example.tool", path -> { String name = path.getFileName().toString(); return name.endsWith(".java") && !name.contains("module-info"); } )
run("javac", args.toArray())
args = new Arguments()
args.add("-d").add("bin/test-patch-runtime/ice.cream")
args.add("--class-path").addPath(path -> path.getFileName().toString().endsWith(".jar"), "bin/main-jars", "lib")
args.addAllFiles("src/test/ice.cream", path -> { String name = path.getFileName().toString(); return name.endsWith(".java") && !name.contains("module-info"); } )
run("javac", args.toArray())

args = new Arguments()
args.add("--module-path").addPath("bin/main-jars", "lib", "bin/test-patch-compile/extra.modular") // re-use compiled "extra.modular" module here
args.add("--add-modules").add("ALL-MODULE-PATH,ALL-DEFAULT")
args.add("--patch-module").add("com.example.application=bin/test-patch-runtime/com.example.application")
args.add("--patch-module").add("com.example.tool=bin/test-patch-runtime/com.example.tool")
args.add("--patch-module").add("ice.cream=bin/test-patch-runtime/ice.cream")
args.add("--add-opens").add("com.example.application/com.example.application=org.junit.platform.commons")
args.add("--add-opens").add("com.example.tool/com.example.tool=org.junit.platform.commons")
args.add("--add-opens").add("com.example.tool/com.example.tool.internal=org.junit.platform.commons")
args.add("--add-opens").add("ice.cream/ice.cream=org.junit.platform.commons")
args.add("--add-opens").add("org.junit.platform.engine/org.junit.platform.engine.support.filter=net.jqwik.engine")
args.add("--add-opens").add("org.junit.platform.commons/org.junit.platform.commons.util=net.jqwik.engine")
args.add("--add-reads").add("ice.cream=org.junit.jupiter.api")
args.add("--add-reads").add("com.example.application=org.junit.jupiter.api")
args.add("--add-reads").add("com.example.tool=org.junit.jupiter.api")
args.add("--module").add("org.junit.platform.console")
args.add("--reports-dir").add("bin/test-patch-runtime-results/junit-platform")
args.add("--scan-modules")
exe("java", args.toArray())

/exit
