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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.console.ConsoleLauncher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A ConsoleLauncher to transform a test into JUnit5 fashion for Bazel.
 */
public class BazelJUnit5ConsoleLauncher {

  private static final String SELECT_PACKAGE = "--select-package";
  private static final String SELECT_CLASS = "--select-class";
  private static final String SELECT_METHOD = "--select-method";

  // LegacyXmlReportGeneratingListener with junit-jupiter generates this file in `--reports-dir` by
  // default
  // https://github.com/junit-team/junit5/blob/37e0f559277f0065f8057cc465a1e8eb91563af6/junit-platform-reporting/src/main/java/org/junit/platform/reporting/legacy/xml/LegacyXmlReportGeneratingListener.java#L116
  private static final String XML_OUTPUT_FILE_PATTERN = "^TEST-.*\\.xml$";

  /**
   * Transform args and invoke the real implementation.
   */
  public static void main(String... args) {
    int exitCode =
        ConsoleLauncher.execute(System.out, System.err, transformArgs(args)).getExitCode();
    afterExecute(exitCode);

    System.exit(exitCode);
  }

  /**
   * Move the generated reports to where they should be.
   */
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

    try {
      Document mergedXmlOutput = mergeTestResultXmls(files);
      writeXmlOutputToFile(mergedXmlOutput, requiredPath.toString());
    } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
      e.printStackTrace();
    }
  }

  /**
   * Merges multiple JUnit test result xmls into a single one by grouping <testsuite> from individual
   * files under <testsuites> in the final output file. Useful if ConsoleLauncher generates test result
   * xmls for both 'JUnit Jupiter' and 'JUnit Vintage'
   */
  private static Document mergeTestResultXmls(File[] files)
      throws ParserConfigurationException, IOException, SAXException {
    List<Document> xmlDocuments = new ArrayList<>();
    for (File file : files) {
      Document xmlDocument = loadXmlDocument(file);
      removeParenthesesFromTestCaseNames(xmlDocument);
      xmlDocuments.add(xmlDocument);
    }

    Document mergedXmlOutput = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        .newDocument();
    Element rootElement = mergedXmlOutput.createElement("testsuites");
    mergedXmlOutput.appendChild(rootElement);

    for (Document xmlDocument : xmlDocuments) {
      NodeList testSuites = xmlDocument.getElementsByTagName("testsuite");
      for (int i = 0; i < testSuites.getLength(); i++) {
        rootElement.appendChild(mergedXmlOutput.importNode(testSuites.item(i), true));
      }
    }

    addEmptyMessageAttributeToNodes(mergedXmlOutput.getElementsByTagName("failure"));
    addEmptyMessageAttributeToNodes(mergedXmlOutput.getElementsByTagName("skipped"));
    return mergedXmlOutput;
  }

  private static Document loadXmlDocument(File file)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = documentBuilder.parse(file);
    return document;
  }

  /**
   * Having parentheses in the test case names seems to cause issues in IntelliJ - `jump to source`
   * doesn't work in test explorer. This method simply trims everything following the test method name.
   */
  private static void removeParenthesesFromTestCaseNames(Document document) {
    NodeList nodeList = document.getElementsByTagName("testcase");
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      Node nameAttribute = node.getAttributes().getNamedItem("name");
      String testCaseName =
          nameAttribute.getNodeValue().split("\\(")[0];

      // Appends display name to test case name in the case of parameterized tests (a bit hacky)
      String testDisplayName = getTestCaseDisplayName(node);
      if (testDisplayName.trim().startsWith("[")) {
        testCaseName += testDisplayName;
      }

      nameAttribute.setNodeValue(testCaseName);
    }
  }

  /**
   * Adds an empty 'message' attribute to specified nodes in test cases. This was needed to get
   * IntelliJ to show failed/ignored tests correctly.
   */
  private static void addEmptyMessageAttributeToNodes(NodeList failureNodes) {
    for (int i = 0; i < failureNodes.getLength(); ++i) {
      Node node = failureNodes.item(i);
      ((Element) node).setAttribute("message", "");
    }
  }

  /**
   * Every <testcase> node in the xml has a <system-out> node which has some additional info including
   * a friendly 'display-name'. This is useful especially for parameterized tests to show the params used
   * in individual runs of the test-case.
   */
  private static String getTestCaseDisplayName(Node testCase) {
    String systemOutText = ((Element) testCase).getElementsByTagName("system-out").item(0)
        .getFirstChild().getNodeValue();
    final String displayNameTag = "display-name:";
    String displayName = systemOutText
        .substring(systemOutText.indexOf(displayNameTag) + displayNameTag.length());
    displayName = displayName.replaceAll("\\(,\\)", "");
    return displayName;
  }

  private static void writeXmlOutputToFile(Document xmlDocument, String fileName)
      throws TransformerException {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    DOMSource source = new DOMSource(xmlDocument);
    StreamResult streamResult = new StreamResult(new File(fileName));
    transformer.transform(source, streamResult);
  }

  /**
   * Transform args into JUnit5 fashion.
   */
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
      // There could be multiple classes in TESTBRIDGE_TEST_ONLY which are separated by $|
      String[] splits = testBridgeTestOnly.split("\\$\\|");
      HashSet<String> methodNames = new HashSet<>();
      HashSet<String> classNames = new HashSet<>();

      for (String split : splits) {
        String[] parts = split.split("#");
        String className = parts[0];
        classNames.add(className);
        methodNames.addAll(getMethodNames(parts[1]));
      }

      List<String> parsedOptions = new ArrayList<>();

      for (String className : classNames) {
        Class<?> klass;
        try {
          klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
          throw new IllegalStateException(e);
        }

        parsedOptions.addAll(Arrays.stream(klass.getDeclaredMethods())
            .filter(method -> methodNames.contains(method.getName()))
            .map(
                method ->
                    SELECT_METHOD + "=" + ReflectionUtils
                        .getFullyQualifiedMethodName(klass, method))
            .collect(Collectors.toList()));
      }

      return parsedOptions;
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
   * bazel --test_filter seems to pass multiple method names in different ways
   * 1. method1,method2,method3
   * 2. (method1|method2|method3)
   * <p>
   * Also in the case of parameterized tests, split on whitespace, '[' or '\' to get only the method name.
   */
  private static HashSet<String> getMethodNames(String testFilterMethodsSubstring) {
    testFilterMethodsSubstring = testFilterMethodsSubstring.replace("(", "").replace(")", "");
    HashSet<String> methodNames = Arrays.stream(testFilterMethodsSubstring.split("[,|]")).distinct()
        .map(s -> s.split("[ \\[\\\\]")[0]).collect(Collectors.toCollection(HashSet::new));
    return methodNames;
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
