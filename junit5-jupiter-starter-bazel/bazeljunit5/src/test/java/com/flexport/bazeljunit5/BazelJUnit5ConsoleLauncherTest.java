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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BazelJUnit5ConsoleLauncherTest {

  static class TestClass {
    void testMethod() {
    }

    void testMethod(int a) {
    }

    void testMethod(int a, Integer b) {
    }

    void testMethod1() {
    }
  }

  @ParameterizedTest
  @CsvSource(
      value = {
          ",false,",
          "foo.xml,false,",
          "foo.xml|TEST-bar.xml|TEST-baz.xml,true,",
      })
  void fixXmlOutputFile(String files, boolean isXmlOutputFileEmpty, String expectedContents)
      throws IOException {
    Path tempFile = File.createTempFile("dummy-", ".dummy").toPath();
    Path reportsDir = Paths.get(tempFile.toString() + ".dir");
    Files.createDirectory(reportsDir);

    if (files != null && !files.isEmpty()) {
      String[] splits = files.split("\\|");
      for (String split : splits) {
        Files.write(reportsDir.resolve(split), split.getBytes());
      }
    }
    Path xmlOutputFile = reportsDir.resolve("test.xml");

    ReflectionTestUtils.invokeMethod(
        BazelJUnit5ConsoleLauncher.class,
        "fixXmlOutputFile",
        isXmlOutputFileEmpty ? "" : xmlOutputFile.toString());

    if (expectedContents != null && !expectedContents.isEmpty()) {
      assertThat(xmlOutputFile.toFile().exists()).isTrue();

      String content = new String(Files.readAllBytes(xmlOutputFile), StandardCharsets.UTF_8);
      assertThat(expectedContents.split("\\|")).contains(content);
    } else {
      assertThat(xmlOutputFile).doesNotExist();
    }
  }

  @ParameterizedTest(name = "{0}; {1} -> {2}")
  @CsvSource(
      value = {
          "''; ; ''",
          "--opt Opt; ; --opt Opt",
          "--opt Opt; ''; --opt Opt",
          "''; com.flexport.bazeljunit5; --select-package=com.flexport.bazeljunit5",
      },
      delimiter = ';')
  void transformArgsForTestBridgeTestOnly(
      String argsText, String testBridgeTestOnly, String expectedArgsText) {
    List<String> args = argsText.isEmpty() ? new ArrayList<>() : Arrays.asList(argsText.split(" "));

    List<String> newArgs =
        ReflectionTestUtils.invokeMethod(
            BazelJUnit5ConsoleLauncher.class,
            "transformArgsForTestBridgeTestOnly",
            args,
            testBridgeTestOnly);

    List<String> expectedArgs =
        expectedArgsText.isEmpty() ? new ArrayList<>() : Arrays.asList(expectedArgsText.split(" "));

    assertThat(newArgs).isEqualTo(expectedArgs);
  }

  @ParameterizedTest(name = "{0}; {1} -> {2}")
  @CsvSource(
      value = {
          "''; ; ''",
          "''; ''; ''",
          "--opt Opt; ; --opt Opt",
          "--opt Opt; ''; --opt Opt",
          "''; foo/bar.xml; --reports-dir=foo",
          "--opt Opt; foo/bar.xml; --opt Opt --reports-dir=foo",
      },
      delimiter = ';')
  void transformArgsForXmlOutputFile(
      String argsText, String xmlOutputFile, String expectedArgsText) {
    List<String> args = argsText.isEmpty() ? new ArrayList<>() : Arrays.asList(argsText.split(" "));

    List<String> newArgs =
        ReflectionTestUtils.invokeMethod(
            BazelJUnit5ConsoleLauncher.class, "transformArgsForXmlOutputFile", args, xmlOutputFile);

    List<String> expectedArgs =
        expectedArgsText.isEmpty() ? new ArrayList<>() : Arrays.asList(expectedArgsText.split(" "));

    assertThat(newArgs).isEqualTo(expectedArgs);
  }

  @ParameterizedTest(name = "{0} -> {1}")
  @CsvSource(
      value = {
          "{package}; --select-package={package}",
          "{class}; --select-class={class}",
          "{class}#; --select-class={class}",
          "{class}#testMethod; "
              + "--select-method={class}#testMethod()|"
              + "--select-method={class}#testMethod(int)|"
              + "--select-method={class}#testMethod(int, java.lang.Integer)",
          "{class}#testMethod$; "
              + "--select-method={class}#testMethod()|"
              + "--select-method={class}#testMethod(int)|"
              + "--select-method={class}#testMethod(int, java.lang.Integer)",
          "{class}#testMethod(); --select-method={class}#testMethod()",
          "{class}#testMethod1; --select-method={class}#testMethod1()",
      },
      delimiter = ';')
  void parseOptions(String testBridgeTestOnly, String expectedOptionsText) {
    List<String> options =
        ReflectionTestUtils.invokeMethod(
            BazelJUnit5ConsoleLauncher.class,
            "parseOptions",
            testBridgeTestOnly.replace("{class}", TestClass.class.getName()));

    List<String> expectedOptions =
        expectedOptionsText.isEmpty()
            ? Arrays.asList()
            : Arrays.stream(expectedOptionsText.split("\\|"))
                .map(s -> s.replace("{class}", TestClass.class.getName()))
                .collect(Collectors.toList());

    assertThat(options).hasSameElementsAs(expectedOptions);
  }

  @Test
  @DisplayName("parseOptions - class not found")
  void parseOptionsWithException() {
    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () ->
                ReflectionTestUtils.invokeMethod(
                    BazelJUnit5ConsoleLauncher.class,
                    "parseOptions",
                    "AnUnknownTestClass#testMethod"));

    assertThat(exception).hasCauseInstanceOf(ClassNotFoundException.class);
  }

  @ParameterizedTest(name = "{0} -> {1}")
  @CsvSource(
      value = {
          "'', ''",
          "--select-package com.flexport, ''",
          "--select-package=com.flexport, ''",
          "--select-package, ''",
          "--select-package=com.flexport --select-package com.flexport --opt=Opt, --opt=Opt",
          "--opt=Opt, --opt=Opt",
          "--opt Opt, --opt Opt",
          "Opt, Opt",
      })
  void filterOptions(String argsText, String expectedArgsText) {
    List<String> args = argsText.isEmpty() ? Arrays.asList() : Arrays.asList(argsText.split(" "));
    List<String> newArgs =
        ReflectionTestUtils.invokeMethod(
            BazelJUnit5ConsoleLauncher.class,
            "filterOptions",
            args,
            Arrays.asList("--select-package"));

    List<String> expectedArgs =
        expectedArgsText.isEmpty() ? Arrays.asList() : Arrays.asList(expectedArgsText.split(" "));
    assertThat(newArgs).isEqualTo(expectedArgs);
  }
}
