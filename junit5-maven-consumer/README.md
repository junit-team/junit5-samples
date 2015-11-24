# junit5-maven-consumer

The `junit5-maven-consumer` project demonstrates how to run tests based on the JUnit 5 prototype using Maven.

## Executing JUnit 5 Tests

Invoking `mvn clean test` from the command line will execute all tests in the test source folder that follow one of [Surefire's default naming patterns](http://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html) (`Test*`, `*Test`, or `*TestCase`), resulting in output similar to the following:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.example.project.FirstTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.033 sec - in com.example.project.FirstTest
Running com.example.project.SecondTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0 sec - in com.example.project.SecondTest

Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 1

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

If you comment out the `@Disabled` annotation on `SecondTest#mySecondTest()`, you will then see the build fail with output similar to the following:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.example.project.FirstTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.038 sec - in com.example.project.FirstTest
Running com.example.project.SecondTest
Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.001 sec <<< FAILURE! - in com.example.project.SecondTest
mySecondTest  Time elapsed: 0.001 sec  <<< FAILURE!
org.opentestalliance.AssertionFailedError: 2 is not equal to 1 ==> expected:<2> but was:<1>
	at com.example.project.SecondTest.mySecondTest(SecondTest.java:13)


Results :

Failed tests: 
  SecondTest.mySecondTest:13 2 is not equal to 1 ==> expected:<2> but was:<1>

Tests run: 2, Failures: 1, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```

### Test reports

Maven Surefire writes plain text and XML test reports to `target/surefire-reports`.

### Limitations

Advanced Maven Surefire parameters, such as `forkCount` or `parallel`, do not work yet.
