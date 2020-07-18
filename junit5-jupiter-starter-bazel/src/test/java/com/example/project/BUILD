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
    main_class = "com.flexport.bazeljunit5.BazelJUnit5ConsoleLauncher",
    test_package = "com.example.project",
    deps = [
        "//bazeljunit5/src/main/java/com/flexport/bazeljunit5:bazeljunit5",
        "//src/main/java/com/example/project:junit5-jupiter-starter-bazel",
    ],
)
