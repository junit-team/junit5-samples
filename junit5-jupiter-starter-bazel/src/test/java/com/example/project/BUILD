package(
    default_visibility = ["//src/test:__subpackages__"],
)

load("//:junit5.bzl", "java_junit5_test")

# Use java_test after bazel officially support junit5
java_junit5_test(
    name = "junit5-jupiter-starter-bazel-test",
    srcs = glob([
        "CalculatorTests.java",
    ]),
    test_class = "com.example.project.CalculatorTests",
    deps = [
        "//src/main/java/com/example/project:junit5-jupiter-starter-bazel",
    ],
)
