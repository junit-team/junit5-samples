/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.flexport.bazeljunit5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.console.ConsoleLauncher;

/** A ConsoleLauncher to transform a test into JUnit5 fashion for Bazel. */
public class BazelJUnit5ConsoleLauncher {

  private static final String SELECT_PACKAGE = "--select-package";
  private static final String SELECT_CLASS = "--select-class";
  private static final String SELECT_METHOD = "--select-method";

  // LegacyXmlReportGeneratingListener with junit-jupiter generates this file in `--reports-dir` by
  // default
  // https://github.com/junit-team/junit5/blob/37e0f559277f0065f8057cc465a1e8eb91563af6/junit-platform-reporting/src/main/java/org/junit/platform/reporting/legacy/xml/LegacyXmlReportGeneratingListener.java#L116
  private static final String XML_OUTPUT_FILE_PATTERN = "^TEST-.*\\.xml$";

  /** Transform args and invoke the real implementation. */
  public static void main(String... args) {
    int exitCode =
        ConsoleLauncher.execute(System.out, System.err, transformArgs(args)).getExitCode();
    afterExecute(exitCode);

    System.exit(exitCode);
  }

  /** Move the generated reports to where they should be. */
  public static void afterExecute(int exitCode) {
    fixXmlOutputFile(System.getenv("XML_OUTPUT_FILE"));
  }

  private static void fixXmlOutputFile(String xmlOutputFile) {
    if (xmlOutputFile == null || xmlOutputFile.isEmpty()) {
      return;
    }

    Path requiredPath = Paths.get(xmlOutputFile);
    Path dir = requiredPath.getParent();

    File[] files =
        dir.toFile().listFiles((file, filename) -> filename.matches(XML_OUTPUT_FILE_PATTERN));
    if (files == null || files.length == 0) {
      System.err.println("The XML output file is not found");
      return;
    }

    if (files.length > 1) {
      // The XML output file is not found.
      System.err.println("More than one XML output file is found");
    }

    try {
      Files.move(files[0].toPath(), requiredPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Transform args into JUnit5 fashion. */
  public static String[] transformArgs(String[] args) {
    return transformArgsForXmlOutputFile(
        transformArgsForTestBridgeTestOnly(
            Arrays.asList(args), System.getenv("TESTBRIDGE_TEST_ONLY")),
        System.getenv("XML_OUTPUT_FILE"))
        .stream()
        .toArray(String[]::new);
  }

  private static List<String> transformArgsForTestBridgeTestOnly(
      List<String> args, String testBridgeTestOnly) {

    if (testBridgeTestOnly == null || testBridgeTestOnly.isEmpty()) {
      return args;
    }

    // When e.g. `bazel test --test_filter=foo //bar:test` is run, the `--test_filter=` value is
    // set in the TESTBRIDGE_TEST_ONLY environment variable by Bazel. For that example, value of the
    // environment variable would simply be `foo`. Here are some examples of what it might look
    // like in practice: test.package.TestClass#testMethod, test.package.TestClass, test.package

    List<String> newArgs = new ArrayList<>();
    newArgs.addAll(filterOptions(args, Arrays.asList(SELECT_PACKAGE)));
    newArgs.addAll(parseOptions(testBridgeTestOnly));

    return newArgs;
  }

  private static List<String> transformArgsForXmlOutputFile(
      List<String> args, String xmlOutputFile) {

    if (xmlOutputFile == null || xmlOutputFile.isEmpty()) {
      return args;
    }

    // The XML output reporting file is set in the XML_OUTPUT_FILE environment variable by JUnit4.
    // And JUnit5 will get the reports directory from `--reports-dir`.

    List<String> newArgs = new ArrayList<>(args);
    newArgs.add("--reports-dir=" + Paths.get(xmlOutputFile).getParent().toString());

    return newArgs;
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
   * @param testBridgeTestOnly env.TESTBRIDGE_TEST_ONLY
   * @return option
   */
  private static List<String> parseOptions(String testBridgeTestOnly) {
    // transform env.TESTBRIDGE_TEST_ONLY

    // test.package.TestClass#
    if (testBridgeTestOnly.endsWith("#") || testBridgeTestOnly.endsWith("$")) {
      testBridgeTestOnly = testBridgeTestOnly.substring(0, testBridgeTestOnly.length() - 1);
    }

    if (testBridgeTestOnly.contains("#")) {
      String[] splits = testBridgeTestOnly.split("#");
      String className = splits[0];
      String methodName = splits[1];

      // already in format test.package.TestClass#testMethod(...)
      if (methodName.matches(".*\\(.*\\)")) {
        return Arrays.asList(SELECT_METHOD + "=" + testBridgeTestOnly);
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
      Class.forName(testBridgeTestOnly);
      return Arrays.asList(SELECT_CLASS + "=" + testBridgeTestOnly);
    } catch (ClassNotFoundException e) {
      // should be a package
      return Arrays.asList(SELECT_PACKAGE + "=" + testBridgeTestOnly);
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
