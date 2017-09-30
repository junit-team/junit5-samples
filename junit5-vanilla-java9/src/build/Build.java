/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

// unnamed package

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.spi.ToolProvider;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
class Build {

  Path deps = Paths.get("deps");

  Path src = Paths.get("src");
  Path mods = Paths.get("mods");
  Path mainSource = src.resolve("main");
  Path mainTarget = mods.resolve("main");
  Path testSource = src.resolve("test");
  Path testTarget = mods.resolve("test");
  Path userSource = src.resolve("user");
  Path userTarget = mods.resolve("user");

  void build() {
    clean();
    resolve();
    compileMain();
    compileTest();
    compileUser();
    test();
  }

  void clean() {
    Util.log.info("Clean output directories");
    Util.clean(mods);
  }

  void compileMain() {
    Util.log.info("Compile main application modules");
    Util.findDirectoryNames(mainSource).forEach(this::compileMain);
  }

  void compileMain(String module) {
    Args args = new Args();
    args.add("-d").add(mainTarget);
    args.add("--module-path").add(deps);
    args.add("--module-source-path").add(mainSource);
    args.add("--module").add(module);
    Util.run("javac", args.list);
  }

  void compileTest() {
    Util.log.info("Compile test application modules");
    Util.findDirectoryNames(testSource).forEach(this::compileTest);
  }

  void compileTest(String module) {
    Args args = new Args();
    args.add("-d").add(testTarget);
    args.add("--module-path").add(mainTarget, deps);
    args.add("--module-source-path").add(testSource);
    args.add("--patch-module").add(module + "=" + mainSource.resolve(module));
    args.add("--module").add(module);
    Util.run("javac", args.list);
  }

  void compileUser() {
    Util.log.info("Compile user-view test integration modules");
    Util.findDirectoryNames(userSource).forEach(this::compileUser);
  }

  void compileUser(String module) {
    Args args = new Args();
    args.add("-d").add(userTarget);
    args.add("--module-path").add(mainTarget, deps);
    args.add("--module-source-path").add(userSource);
    args.add("--module").add(module);
    Util.run("javac", args.list);
  }

  void test() {
    Util.log.info("Launch test runs");
    test(testTarget, mainTarget, deps);
    test(userTarget, mainTarget, deps);
  }

  void test(Path... modulePath) {
    Args args = new Args();
    args.add("--module-path").add(modulePath);
    args.add("--add-modules").add("ALL-MODULE-PATH");
    args.add("--module").add("org.junit.platform.console");
    args.add("--scan-class-path"); // TODO replace with "--scan-module-path"
    Util.run("java", args.list);
  }

  /** Resolve all external dependencies. */
  void resolve() {
    Util.log.info("Resolve dependencies");
    String repository = "http://central.maven.org/maven2/";
    String version = "1.0.0";
    resolve(repository + "org/apiguardian", "apiguardian-api", version);
    resolve(repository + "org/opentest4j", "opentest4j", version);
    // branch "jigsaw"
    repository = "https://jitpack.io/com/github/junit-team/junit5/";
    version = "jigsaw-r5.0.0-gbe104bb-69";
    resolve(repository, "junit-jupiter-api", version);
    resolve(repository, "junit-jupiter-engine", version);
    resolve(repository, "junit-platform-commons", version);
    resolve(repository, "junit-platform-console", version);
    resolve(repository, "junit-platform-engine", version);
    resolve(repository, "junit-platform-launcher", version);
  }

  /** Resolve dependency by downloading the associated jar file for the given coordinates. */
  Path resolve(String base, String artifact, String version) {
    String file = artifact + "-" + version + ".jar";
    String uri = String.join("/", base, artifact, version, file);
    return Util.download(URI.create(uri), deps);
  }

  /** Entry-point for building... */
  public static void main(String... args) {
    Util.log.info("BEGIN");
    new Build().build();
    Util.log.info("END.");
  }

  /** Command line option builder. */
  class Args {
    List<String> list = new ArrayList<>();

    Args add(Object arg) {
      list.add(arg.toString());
      return this;
    }

    Args add(Path... paths) {
      String delimiter = File.pathSeparator;
      Stream<Path> stream = Arrays.stream(paths);
      list.add(String.join(delimiter, stream.map(Object::toString).collect(toList())));
      return this;
    }
  }

  /** Static helpers. */
  interface Util {

    Logger log = Logger.getLogger("junit5-vanilla-java9");

    /** Delete all files and directories from the root directory. */
    static void clean(Path root) {
      if (Files.notExists(root)) {
        return;
      }
      try {
        try (Stream<Path> stream = Files.walk(root)) {
          Stream<Path> selected = stream.sorted((p, q) -> -p.compareTo(q));
          for (Path path : selected.collect(toList())) {
            Files.deleteIfExists(path);
          }
        }
      } catch (IOException e) {
        throw new UncheckedIOException("clean failed for: " + root, e);
      }
    }

    /** Download the specified resource into the target directory. */
    static Path download(URI uri, Path directory) {
      String fileName = uri.getPath();
      int begin = fileName.lastIndexOf('/') + 1;
      fileName = fileName.substring(begin).split("\\?")[0].split("#")[0];
      try {
        URL url = uri.toURL();
        Files.createDirectories(directory);
        Path target = directory.resolve(fileName);
        if (Files.exists(target)) {
          return target;
        }
        try (InputStream sourceStream = url.openStream();
            OutputStream targetStream = Files.newOutputStream(target)) {
          System.out.println("Loading " + fileName + " from " + url.getHost() + "...");
          sourceStream.transferTo(targetStream);
        }
        return target;
      } catch (IOException e) {
        throw new UncheckedIOException("download failed for: " + uri, e);
      }
    }

    static List<Path> findDirectories(Path root) {
      if (Files.notExists(root)) {
        return List.of();
      }
      try (Stream<Path> paths = Files.find(root, 1, (path, attr) -> Files.isDirectory(path))) {
        return paths.filter(path -> !root.equals(path)).collect(toList());
      } catch (IOException e) {
        throw new UncheckedIOException("find directories failed for: " + root, e);
      }
    }

    static List<String> findDirectoryNames(Path root) {
      return findDirectories(root)
          .stream()
          .map(root::relativize)
          .map(Path::toString)
          .collect(toList());
    }

    /** Run an internal or external tool with the supplied arguments. */
    static void run(String name, Object... args) {
      List<String> strings = new ArrayList<>();
      Arrays.stream(args).map(Object::toString).forEach(strings::add);
      run(name, strings);
    }

    /** Run an internal or external tool with the supplied arguments. */
    static void run(String name, List<String> args) {
      int result;
      System.out.println(name + " " + args);
      PrintStream standardOut = System.out;
      PrintStream standardErr = System.err;
      ToolProvider tool = ToolProvider.findFirst(name).orElse(null);
      if (tool != null) {
        String[] strings = args.toArray(new String[args.size()]);
        result = tool.run(standardOut, standardErr, strings);
      } else {
        ProcessBuilder processBuilder = new ProcessBuilder(name);
        processBuilder.command().addAll(args);
        processBuilder.redirectErrorStream(true);
        try {
          Process process = processBuilder.start();
          process.getInputStream().transferTo(standardOut);
          result = process.waitFor();
        } catch (IOException | InterruptedException e) {
          throw new Error("process `" + name + "` failed", e);
        }
      }
      if (result != 0) {
        throw new RuntimeException(name + " failed with error code " + result);
      }
    }
  }
}
