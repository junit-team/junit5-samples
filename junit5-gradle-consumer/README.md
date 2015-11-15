# junit5-gradle-consumer

The `junit5-gradle-consumer` project demonstrates how to run tests based
on the JUnit 5 prototype using Gradle.

## Executing JUnit 5 Tests

Invoking `gradlew clean junit5` from the command line will execute all
tests in `com.example.project.FirstTest`, resulting in output similar
to the following:

```
Test execution started. Number of static tests: 2
Engine started: junit5
Test started:     My 1st JUnit 5 test! 😎 [junit5:com.example.project.FirstTest#myFirstTest(java.lang.String)]
Test succeeded:   My 1st JUnit 5 test! 😎 [junit5:com.example.project.FirstTest#myFirstTest(java.lang.String)]
Test skipped:     mySecondTest [junit5:com.example.project.FirstTest#mySecondTest()]
                  => Exception:   Skipped test method [void com.example.project.FirstTest.mySecondTest()] due to failed condition
Engine finished: junit5
Test execution finished.

Test run finished after 29 ms
[         2 tests found     ]
[         1 tests started   ]
[         1 tests skipped   ]
[         0 tests aborted   ]
[         0 tests failed    ]
[         1 tests successful]
```

### Current Limitations

- The `junit5` task currently only executes tests in `com.example.project.FirstTest`.
- The build will not fail if a test fails; however, the results can be visually inspected in the console.
