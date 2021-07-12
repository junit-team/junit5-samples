load("//:junit5.bzl", "java_junit5_test")

filegroup(
    name = "java_test_files",
    srcs = glob(["**/*.java"]),
)

java_junit5_test(
    name = "test",
    srcs = [
        ":java_test_files",
    ],
    jvm_flags = [],
    main_class = "com.flexport.bazeljunit5.BazelJUnit5ConsoleLauncher",
    test_package = "com.flexport.bazeljunit5",
    deps = [
        # Internal dependencies
        "//bazeljunit5/src/main/java/com/flexport/bazeljunit5:bazeljunit5",

        # External dependencies
        "@commons_logging//:commons_logging",
        "@org_assertj_assertj_core//:org_assertj_assertj_core",
        "@org_springframework_spring_test//:org_springframework_spring_test",
        "@org_springframework_spring_core//:org_springframework_spring_core",
    ],
)
