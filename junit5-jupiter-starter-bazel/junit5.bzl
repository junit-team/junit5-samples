"""External dependencies & java_junit5_test rule"""

load("@bazel_tools//tools/build_defs/repo:jvm.bzl", "jvm_maven_import_external")

JUNIT_JUPITER_GROUP_ID = "org.junit.jupiter"
JUNIT_JUPITER_ARTIFACT_ID_LIST = [
    "junit-jupiter-api",
    "junit-jupiter-engine",
    "junit-jupiter-params",
]

JUNIT_PLATFORM_GROUP_ID = "org.junit.platform"
JUNIT_PLATFORM_ARTIFACT_ID_LIST = [
    "junit-platform-commons",
    "junit-platform-console",
    "junit-platform-engine",
    "junit-platform-launcher",
    "junit-platform-suite-api",
]

JUNIT_EXTRA_DEPENDENCIES = [
    ("org.apiguardian", "apiguardian-api", "1.0.0"),
    ("org.opentest4j", "opentest4j", "1.1.1"),
]

JUNIT_SHA256_CHECKSUMS = {
    "junit-jupiter-api": "3f476de9b214f20ca69da51e801186d001f67328a686df81bc3de3ba11953870",
    "junit-jupiter-engine": "0eb1ab3fc8e4130943b54f4d86824b106ef1cd90d96789343f3944e48b3c501c",
    "junit-jupiter-params": "52f7aeb346cfa41bb33a8d3dbab8c577f9c37f8f5a79a632af10b5c8f1e92186",
    "junit-platform-commons": "341621f4d998fd7b539b38baa7e1a3da80b7cac00b983e6206b01c9290915fe9",
    "junit-platform-console": "2a2407737b147004642ee8e65618d51cc3895fe03b9c42f54b07d480ce6b7190",
    "junit-platform-engine": "23b41ac95e4673f7b31e8502424451be4154fe4db1d448448945e2215473c246",
    "junit-platform-launcher": "d1ebfafa2bd87b05c7dce7249e1437a1c0e4f16af99d81f89c5a0c0d66dc1510",
    "junit-platform-suite-api": "130bf4d60958e4345583e5804f4a2932c6b60832bf2bc797b9521ffe6c9acadc"
}

def junit_jupiter_java_repositories(
        version = "5.6.2"):
    """Imports dependencies for JUnit Jupiter"""
    for artifact_id in JUNIT_JUPITER_ARTIFACT_ID_LIST:
        jvm_maven_import_external(
            name = _format_maven_jar_name(JUNIT_JUPITER_GROUP_ID, artifact_id),
            artifact = "%s:%s:%s" % (
                JUNIT_JUPITER_GROUP_ID,
                artifact_id,
                version,
            ),
            artifact_sha256 = JUNIT_SHA256_CHECKSUMS[artifact_id],
            server_urls = ["https://repo1.maven.org/maven2"],
            licenses = ["notice"], # EPL 2.0 License
        )

    for t in JUNIT_EXTRA_DEPENDENCIES:
        jvm_maven_import_external(
            name = _format_maven_jar_name(t[0], t[1]),
            artifact = "%s:%s:%s" % t,
            artifact_sha256 = JUNIT_SHA256_CHECKSUMS[artifact_id],
            server_urls = ["https://repo1.maven.org/maven2"],
            licenses = ["notice"], # EPL 2.0 License
        )

def junit_platform_java_repositories(
        version = "1.6.2"):
    """Imports dependencies for JUnit Platform"""
    for artifact_id in JUNIT_PLATFORM_ARTIFACT_ID_LIST:
        jvm_maven_import_external(
            name = _format_maven_jar_name(JUNIT_PLATFORM_GROUP_ID, artifact_id),
            artifact = "%s:%s:%s" % (
                JUNIT_PLATFORM_GROUP_ID,
                artifact_id,
                version,
            ),
            artifact_sha256 = JUNIT_SHA256_CHECKSUMS[artifact_id],
            server_urls = ["https://repo1.maven.org/maven2"],
            licenses = ["notice"], # EPL 2.0 License
        )

def java_junit5_test(name, srcs, test_package, deps = [], runtime_deps = [], **kwargs):
    FILTER_KWARGS = [
        "main_class",
        "use_testrunner",
        "args",
    ]

    for arg in FILTER_KWARGS:
        if arg in kwargs.keys():
            kwargs.pop(arg)

    junit_console_args = []
    if test_package:
        junit_console_args += ["--select-package", test_package]
    else:
        fail("must specify 'test_package'")

    native.java_test(
        name = name,
        srcs = srcs,
        use_testrunner = False,
        main_class = "org.junit.platform.console.ConsoleLauncher",
        args = junit_console_args,
        deps = deps + [
            _format_maven_jar_dep_name(JUNIT_JUPITER_GROUP_ID, artifact_id)
            for artifact_id in JUNIT_JUPITER_ARTIFACT_ID_LIST
        ] + [
            _format_maven_jar_dep_name(JUNIT_PLATFORM_GROUP_ID, "junit-platform-suite-api"),
        ] + [
            _format_maven_jar_dep_name(t[0], t[1])
            for t in JUNIT_EXTRA_DEPENDENCIES
        ],
        runtime_deps = runtime_deps + [
            _format_maven_jar_dep_name(JUNIT_PLATFORM_GROUP_ID, artifact_id)
            for artifact_id in JUNIT_PLATFORM_ARTIFACT_ID_LIST
        ],
        **kwargs
    )

def _format_maven_jar_name(group_id, artifact_id):
    return ("%s_%s" % (group_id, artifact_id)).replace(".", "_").replace("-", "_")

def _format_maven_jar_dep_name(group_id, artifact_id):
    return "@%s//jar" % _format_maven_jar_name(group_id, artifact_id)
