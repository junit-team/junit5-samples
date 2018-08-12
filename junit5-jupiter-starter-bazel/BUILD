load("//:junit5.bzl", "java_junit5_test")

java_library(
    name = "junit5-jupiter-starter-bazel",
    srcs = glob([
        "src/main/java/**/*.java",
    ]),
)

# Use java_test after bazel officially support junit5
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
