# junit5-gradle-consumer

The `junit5-gradle-consumer` project demonstrates how to run tests based on
JUnit Jupiter milestones using Gradle with the help of a very basic Gradle plugin
for the JUnit Platform.

This sample project does not aim to demonstrate how to use the JUnit Jupiter APIs.
For detailed  information on the JUnit Jupiter programming and extension models,
please consult the [User Guide](http://junit.org/junit5/docs/current/user-guide/).

## Enabling the JUnit Platform Gradle Plugin

To use the JUnit Platform Gradle plugin, you first need to configure `build.gradle` as follows.

```groovy
buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath 'org.junit.platform:junit-platform-gradle-plugin:1.0.0-SNAPSHOT'
	}
}

apply plugin: 'org.junit.platform.gradle.plugin'
```

## Configuring the JUnit Platform Gradle Plugin

Once the JUnit Platform Gradle plugin has been applied, you can configure it as follows.

```groovy
junitPlatform {
	// platformVersion '1.0.0-SNAPSHOT'
	filters {
		engines {
			// include 'junit-jupiter', 'junit-vintage'
			// exclude 'custom-engine'
		}
		tags {
			// include 'fast'
			exclude 'slow'
		}
		// includeClassNamePattern '.*Test'
	}
	// enableStandardTestTask true
	// reportsDir file('build/test-results/junit-platform') // this is the default
	logManager 'org.apache.logging.log4j.jul.LogManager'
}
```

By default all engines and tags are included in the test plan.

If you supply a _Test Engine ID_ via `engines {include ...}` or `engines {exclude ...}`,
the JUnit Platform Gradle plugin will only run tests for the desired test engines.

If you supply a _tag_ via `tags {include ...}`, the JUnit Platform Gradle plugin will only
run tests that are _tagged_ accordingly (e.g., via the `@Tag` annotation for
JUnit Jupiter based tests). Similarly, if you supply a _tag_ via `tags {exclude ...}`,
the JUnit Platform Gradle plugin will not run tests that are _tagged_ accordingly.

By default, the JUnit Platform Gradle plugin disables the standard Gradle `test` task, but
this be overridden via the `enableStandardTestTask` flag.

## Configuring Test Engines

In order to have the JUnit Platform Gradle plugin run any tests at all, a TestEngine
implementation must be on the classpath.

To configure support for JUnit Jupiter based tests, configure a `testCompile` dependency
on the JUnit Jupiter API and a `testRuntime` dependency on the JUnit Jupiter TestEngine
implementation similar to the following.

```groovy
dependencies {
	testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0-SNAPSHOT")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.0-SNAPSHOT")
}
```

The JUnit Platform Gradle plugin can also run JUnit 3 and JUnit 4 based tests as long as you
configure a `testCompile` dependency on JUnit 4 and a `testRuntime` dependency on the
JUnit Vintage TestEngine implementation similar to the following.

```groovy
dependencies {
	testCompile("junit:junit:4.12")
	testRuntime("org.junit.vintage:junit-vintage-engine:4.12.0-SNAPSHOT")
}
```

## Executing Tests on the JUnit Platform

Once the JUnit Platform Gradle plugin has been applied and configured, you have a new
`junitPlatformTest` task at your disposal.

Invoking `gradlew clean junitPlatformTest` (or `gradlew clean test`) from the command
line will execute all tests within the project whose class names match the pattern
`^.*Tests?$`. This will result in output similar to the following:

```
:junitPlatformTest

Test run finished after 86 ms
[         6 containers found      ]
[         0 containers skipped    ]
[         6 containers started    ]
[         0 containers aborted    ]
[         6 containers successful ]
[         0 containers failed     ]
[         5 tests found           ]
[         1 tests skipped         ]
[         4 tests started         ]
[         0 tests aborted         ]
[         4 tests successful      ]
[         0 tests failed          ]

:test SKIPPED

BUILD SUCCESSFUL
```

If you comment out the `@Disabled` annotation on `SecondTest#mySecondTest()`, you will
then see the build fail with output similar to the following:

```
:junitPlatformTest

Failures (1):
  JUnit Jupiter:SecondTest:mySecondTest()
    MethodSource [className = 'com.example.project.SecondTest', methodName = 'mySecondTest', methodParameterTypes = '']
    => org.opentest4j.AssertionFailedError: 2 is not equal to 1 ==> expected: <2> but was: <1>

Test run finished after 90 ms
[         6 containers found      ]
[         0 containers skipped    ]
[         6 containers started    ]
[         0 containers aborted    ]
[         6 containers successful ]
[         0 containers failed     ]
[         5 tests found           ]
[         0 tests skipped         ]
[         5 tests started         ]
[         0 tests aborted         ]
[         4 tests successful      ]
[         1 tests failed          ]

:junitPlatformTest FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':junitPlatformTest'.
> Process 'command '/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.

BUILD FAILED
```

**Note**: The _exit value_ is `1` if any containers or tests failed; otherwise, it is `0`.

### Current Limitations

- The results of any tests run via the JUnit Platform Gradle plugin will not be included
  in the standard test report generated by Gradle; however, the test results
  can typically be aggregated by your CI server software. See the `reportsDir` property of the plugin.
