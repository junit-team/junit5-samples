A ConsoleLauncher adapter between Bazel and JUnit5
---

## Why
[Bazel does not work with JUnit5](https://github.com/bazelbuild/bazel/issues/6681)
### TESTBRIDGE_TEST_ONLY and `--test_arg`
* When you specify the JUnit4 option `--test_filter`, it actually runs all tests,
and also you can not specify the JUnit5 option `--test_args`.
```shell script
# in JUnit4 style, would run all tests
bazel test //...:test "--test_filter=test.package.TestClass#testMethod"

# in JUnit5 style, would be rejected by Bazel
bazel test //...:test "--test_arg=--select-method=test.package.TestClass#testMethod"
```
This breaks your agile development in a big project.

* Bazel invokes your tests in JUnit4's style by a specific environment variable:
```shell script
TESTBRIDGE_TEST_ONLY=test.package.TestClass#testMethod # test by select-method
TESTBRIDGE_TEST_ONLY=test.package.TestClass # test by select-class
TESTBRIDGE_TEST_ONLY=test.package # test by select-package
```

* For JUnit5, the arguments should be:
```shell script
--test_arg=--select-method=test.package.TestClass#testMethod
--test_arg=--select-class=test.package.TestClass
--test_arg=--select-package=test.package
```

### XML_OUTPUT_FILE to `--reports-dir`
* When your IDE specifies XML_OUTPUT_FILE to get the XML test report,
it will get nothing because JUnit5 expects an argument `--reports-dir`
and put the XML test report in a file `TEST-*.xml` in the reports dir.

* We will specify the `--reports-dir` according to XML_OUTPUT_FILE,
and rename the file `TEST-*.xml` to XML_OUTPUT_FILE.

### How
* Import this repository into your bazel `WORKSPACE` file

```python
###########
# JUnit 5 #
###########

git_repository(
    name = "bazel_junit5",
    # branch = "master",
    commit = "<INSERT_COMMIT_SHA>",
    remote = "https://github.com/junit-team/junit5-samples.git",
)

load("@bazel_junit5//junit5-jupiter-starter-bazel:junit5.bzl", "junit_jupiter_java_repositories", "junit_platform_java_repositories")

JUNIT_JUPITER_VERSION = "5.6.2"
JUNIT_PLATFORM_VERSION = "1.6.2"

junit_jupiter_java_repositories(
    version = JUNIT_JUPITER_VERSION,
)

junit_platform_java_repositories(
    version = JUNIT_PLATFORM_VERSION,
)
```

* Set the main_class and deps for java_junit5_test
```python
load("@bazel_junit5//junit5-jupiter-starter-bazel:junit5.bzl", "java_junit5_test")

java_junit5_test(
    # ...
    main_class = "com.flexport.bazeljunit5.BazelJUnit5ConsoleLauncher",
    deps = [
        "//bazeljunit5/src/main/java/com/flexport/bazeljunit5",
        # ...
    ]
)
```
* Run a single test
  * from command line
  ```shell script
  bazel test //...:test "--test_filter=com.example.MyTest#test"
  ```
  * or right click  on a single test in a `*Test.java` file and and select "Run ..." in your IDE (e.g. IntelliJ IDEA).
