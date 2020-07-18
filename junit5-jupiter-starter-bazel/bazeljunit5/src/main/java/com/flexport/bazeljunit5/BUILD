load("@rules_java//java:defs.bzl", "java_library")

filegroup(
    name = "java_files",
    srcs = glob(["**/*.java"]),
)

java_library(
    name = "bazeljunit5",
    srcs = [
        ":java_files",
    ],
    visibility = [
        "//visibility:public",
    ],
    deps = [
        # Internal dependencies

        # External dependencies
        "@org_junit_platform_junit_platform_commons//:org_junit_platform_junit_platform_commons",
        "@org_junit_platform_junit_platform_console//:org_junit_platform_junit_platform_console"
    ],
)
