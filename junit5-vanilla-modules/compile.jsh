//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

//
// download dependencies
//
get("lib", "org.apiguardian", "apiguardian-api", "1.0.0")
get("lib", "org.opentest4j", "opentest4j", "1.0.0")
String platformVersion = "1.1.0"
get("lib", "org.junit.platform", "junit-platform-commons", platformVersion)
get("lib", "org.junit.platform", "junit-platform-console", platformVersion)
get("lib", "org.junit.platform", "junit-platform-engine", platformVersion)
get("lib", "org.junit.platform", "junit-platform-launcher", platformVersion)
String jupiterVersion = "5.1.0"
get("lib", "org.junit.jupiter", "junit-jupiter-api", jupiterVersion)
get("lib", "org.junit.jupiter", "junit-jupiter-engine", jupiterVersion)
String vintageVersion = "5.1.0"
get("lib", "org.junit.vintage", "junit-vintage-engine", vintageVersion)
get("lib", "junit", "junit", "4.12")
get("lib", "org.hamcrest", "hamcrest-core", "1.3")

get("lib", URI.create("https://oss.sonatype.org/content/repositories/snapshots/net/jqwik/jqwik/0.8.5-SNAPSHOT/jqwik-0.8.5-20180301.181313-3.jar"))

//
// compile and package main modules
//
run("javac", "-d", "bin/main", "--module-source-path", "src/main", "--module", "com.example.tool")
run("javac", "-d", "bin/main", "--module-source-path", "src/main", "--module", "com.example.application")

Files.createDirectories(Paths.get("bin/main-jars"))
run("jar", "--create", "--file", "bin/main-jars/com.example.tool.jar", "-C", "bin/main/com.example.tool", ".")
run("jar", "--create", "--file", "bin/main-jars/com.example.application.jar", "--main-class", "com.example.application.Main", "-C", "bin/main/com.example.application", ".")
run("jar", "--describe-module", "--file", "bin/main-jars/com.example.application.jar")

//
// run application
//
exe("java", "--module-path", "bin/main-jars", "--module", "com.example.application", "3", "4")

/exit
