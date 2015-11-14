# junit5-maven-consumer

The `junit5-maven-consumer` project demonstrates how to run tests based
on the JUnit 5 prototype using Maven.

## Executing JUnit 5 Tests

Invoking `mvn clean test` from the command line will execute all
tests in `com.example.project.FirstTest`, resulting in output similar
to the following:

```
Engine started: junit5
Test started:     1 + 1 = 2 [junit5:com.example.project.FirstTest#myTest(java.lang.String)]
Test succeeded:   1 + 1 = 2 [junit5:com.example.project.FirstTest#myTest(java.lang.String)]
Engine finished: junit5
Test execution finished.

Test run finished after 52 ms
[         1 tests found     ]
[         1 tests started   ]
[         0 tests skipped   ]
[         0 tests aborted   ]
[         0 tests failed    ]
[         1 tests successful]
```

### Current Limitations

- It will only execute tests in `com.example.project.FirstTest`.
- The build will not fail if a test fails.
