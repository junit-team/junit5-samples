load("//:junit5.bzl", "java_junit5_test")

java_library(
    name = "calculator",
    srcs = [
        "src/main/java/com/example/project/Calculator.java",
    ],
)

java_junit5_test(
    name = "CalculatorTests",
    srcs = [
        "src/test/java/com/example/project/CalculatorTests.java",
    ],
    #test_class = "com.example.project.CalculatorTests",
    deps = [
        ":calculator",
    ],
)
