//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

exe("java", "--version")

del("bin")
del("lib")

exe("jshell", "compile.jsh")

exe("jshell", "test-classpath.jsh")
exe("jshell", "test-patch-compile.jsh")
exe("jshell", "test-patch-runtime.jsh")

/exit
