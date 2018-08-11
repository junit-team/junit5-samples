load("//:junit5.bzl", "java_junit5_test")

java_library(
    name = "junit5-jupiter-starter-bazel",
    srcs = glob([
        "src/main/java/**/*.java",
    ]),
)

java_junit5_test(
    name = "junit5-jupiter-starter-bazel-test",
    srcs = glob([
        "src/test/java/**/*.java",
    ]),
    test_class = "com.example.project.CalculatorTests",
    deps = [
        ":junit5-jupiter-starter-bazel",
    ],
)
