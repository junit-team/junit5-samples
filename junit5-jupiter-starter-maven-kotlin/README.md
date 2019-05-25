# junit5-jupiter-starter-maven-kotlin [![Build Status](https://travis-ci.org/junit-team/junit5-samples.svg?branch=master)](https://travis-ci.org/junit-team/junit5-samples)
Mixing Java and Kotlin test classes with JUnit 5 Jupiter and JUnit 4 Vintage test engines. Supported JDK 1.8 and JDK 11.

The `junit5-jupiter-starter-maven-kotlin` sample demonstrates how to execute JUnit 4 Vintage test together
with JUnit 5 Jupiter tests by using Maven build tool in mixed java / kotlin test classes in the project:

Please note that this project is uses the [Maven Wrapper](https://github.com/takari/maven-wrapper)
3.6.1 version. This helps you ensure that already tested versions are not going to be failed if
locally installed different maven version. 

Also please make a note, that java test classes are not recognizable from `src/test/kotlin` test sources folder:

```
src/
  test/
    java/
      **/*JavaJUnit4VintageTest.java  +
      **/*JavaJUnit5JupiterTest.java  +
      **/*KotlinJUnit4VintageTest.kt  +
      **/*KotlinJUnit5JupiterTest.kt  +
    kotlin/
      **/*JavaJUnit4VintageTest.java  -
      **/*JavaJUnit5JupiterTest.java  -
      **/*KotlinJUnit4VintageTest.kt  +
      **/*KotlinJUnit5JupiterTest.kt  +
```

Here `+` means that marked type of tests classes by language and engine are going to be executed
