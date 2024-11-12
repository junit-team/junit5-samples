load("@rules_jvm_external//:defs.bzl", "artifact")
load("@contrib_rules_jvm//java:defs.bzl", "JUNIT5_DEPS", "java_test_suite")

java_library(
    name = "junit5-jupiter-starter-bazel",
    srcs = glob(["src/main/java/**/*.java"]),
)

java_test_suite(
    name = "junit5-jupiter-starter-bazel-tests",
    size = "small",
    srcs = glob(["src/test/java/**/*.java"]),
    runner = "junit5",
    test_suffixes = ["Tests.java"],
    runtime_deps = JUNIT5_DEPS,
    deps = [
        ":junit5-jupiter-starter-bazel",
        artifact("org.junit.jupiter:junit-jupiter-api"),
        artifact("org.junit.jupiter:junit-jupiter-params"),
    ],
)
