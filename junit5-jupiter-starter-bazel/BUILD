load("@rules_jvm_external//:defs.bzl", "artifact")
load("@contrib_rules_jvm//java:defs.bzl", "java_test_suite", "JUNIT5_DEPS")

java_library(
    name = "junit5-jupiter-starter-bazel",
    srcs = glob(["src/main/**/*.java"]),
)

java_test_suite(
    name = "junit5-jupiter-starter-bazel-tests",
    srcs = glob(["src/test/**/*.java"]),
    runner = "junit5",
    test_suffixes = ["Tests.java"],
    deps = [
        ":junit5-jupiter-starter-bazel",
        artifact("org.junit.jupiter:junit-jupiter-api"),
        artifact("org.junit.jupiter:junit-jupiter-params"),
    ],
    runtime_deps = JUNIT5_DEPS,
)
