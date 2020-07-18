package com.flexport.bazeljunit5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.console.ConsoleLauncher;

/** A ConsoleLauncher to transform a test into JUnit5 fashion for Bazel. */
public class BazelJUnit5ConsoleLauncher {

  private static final String SELECT_PACKAGE = "--select-package";
  private static final String SELECT_CLASS = "--select-class";
  private static final String SELECT_METHOD = "--select-method";

  /** Transform args and invoke the real implementation. */
  public static void main(String... args) {
    int exitCode =
        ConsoleLauncher.execute(System.out, System.err, transformArgs(args)).getExitCode();
    System.exit(exitCode);
  }

  /** Transform args into JUnit5 fashion. */
  public static String[] transformArgs(String[] args) {
    return transformArgs(args, System.getenv("TESTBRIDGE_TEST_ONLY"));
  }

  private static String[] transformArgs(String[] args, String testOnly) {
    // When e.g. `bazel test --test_filter=foo //bar:test` is run, the `--test_filter=` value is set
    // in the TESTBRIDGE_TEST_ONLY environment variable by Bazel. For that example, value of the
    // environment variable would simply be `foo`. Here are some examples of what it might look like
    // in practice:
    // test.package.TestClass#testMethod, test.package.TestClass, test.package
    if (testOnly == null || testOnly.isEmpty()) {
      return args;
    }

    return Stream.concat(
            // remove any --select-package
            filterOptions(Arrays.asList(args), Arrays.asList(SELECT_PACKAGE)).stream(),
            parseOptions(testOnly).stream())
        .toArray(String[]::new);
  }

  /**
   * Parse JUnit5 option from env.TESTBRIDGE_TEST_ONLY.
   *
   * <p>test.package: --select-package=test.package
   *
   * <p>test.package.TestClass: --select-class=test.package.TestClass
   *
   * <p>test.package.TestClass#testMethod: --select-method=test.package.TestClass#testMethod
   *
   * @param testOnly env.TESTBRIDGE_TEST_ONLY
   * @return option
   */
  private static List<String> parseOptions(String testOnly) {
    // transform env.TESTBRIDGE_TEST_ONLY

    // test.package.TestClass#
    if (testOnly.endsWith("#") || testOnly.endsWith("$")) {
      testOnly = testOnly.substring(0, testOnly.length() - 1);
    }

    if (testOnly.contains("#")) {
      String[] splits = testOnly.split("#");
      String className = splits[0];
      String methodName = splits[1];

      // already in format test.package.TestClass#testMethod(...)
      if (methodName.matches(".*\\(.*\\)")) {
        return Arrays.asList(SELECT_METHOD + "=" + testOnly);
      }

      Class<?> klass;
      try {
        klass = Class.forName(className);
      } catch (ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }

      // pick all overloaded methods
      return Arrays.stream(klass.getDeclaredMethods())
          .filter(method -> method.getName().equals(methodName))
          .map(
              method ->
                  SELECT_METHOD + "=" + ReflectionUtils.getFullyQualifiedMethodName(klass, method))
          .collect(Collectors.toList());
    }

    try {
      Class.forName(testOnly);
      return Arrays.asList(SELECT_CLASS + "=" + testOnly);
    } catch (ClassNotFoundException e) {
      // should be a package
      return Arrays.asList(SELECT_PACKAGE + "=" + testOnly);
    }
  }

  /**
   * Remove any argument options like:
   *
   * <p>`--select-package=test.package` and `--select-package "test.package"`.
   */
  private static List<String> filterOptions(List<String> args, List<String> excludeOptions) {
    AtomicInteger skipNext = new AtomicInteger(0);
    return args.stream()
        .filter(
            arg -> {
              if (excludeOptions.contains(arg)) {
                skipNext.set(1);
                return false;
              }

              if (skipNext.get() == 1) {
                skipNext.set(0);
                return false;
              }

              return !excludeOptions.stream().anyMatch(option -> arg.startsWith(option + "="));
            })
        .collect(Collectors.toList());
  }
}
