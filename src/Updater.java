/*
 * Copyright 2015-2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

// default package

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Updates the versions of JUnit Platform artifacts in all sample projects.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
class Updater {

    private static final String VERSION_REGEX = "([0-9.]+)";

    public static void main(String[] args) throws Exception {
        new Updater(args[0]).update();
    }

    private final String jupiterVersion;

    public Updater(String jupiterVersion) {
        this.jupiterVersion = jupiterVersion;
    }

    void update() throws IOException {
        var gradleBomReplacement = new Replacement("org.junit:junit-bom:" + VERSION_REGEX, VersionType.BOM);
        var mavenBomReplacement = new Replacement(
                Pattern.compile("""
                        \\s*<groupId>org.junit</groupId>
                        \\s*<artifactId>junit-bom</artifactId>
                        \\s*<version>([0-9.]+)</version>
                        """, Pattern.MULTILINE),
                VersionType.BOM
        );

        update(Path.of("junit5-jupiter-extensions/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit5-jupiter-starter-ant/build.sh"), List.of(
                new Replacement("junit_platform_version='" + VERSION_REGEX + "'", VersionType.PLATFORM)
        ));
        update(Path.of("junit5-jupiter-starter-bazel/MODULE.bazel"), List.of(
                new Replacement("JUNIT_JUPITER_VERSION = \"" + VERSION_REGEX + '"', VersionType.JUPITER),
                new Replacement("JUNIT_PLATFORM_VERSION = \"" + VERSION_REGEX + '"', VersionType.PLATFORM)
        ));
        update(Path.of("junit5-jupiter-starter-gradle/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit5-jupiter-starter-gradle-groovy/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit5-jupiter-starter-gradle-kotlin/build.gradle.kts"), List.of(gradleBomReplacement));
        update(Path.of("junit5-jupiter-starter-maven/pom.xml"), List.of(mavenBomReplacement));
        update(Path.of("junit5-jupiter-starter-maven-kotlin/pom.xml"), List.of(mavenBomReplacement));
        update(Path.of("junit5-jupiter-starter-sbt/build.sbt"), List.of(
                new Replacement("\"org.junit.jupiter\" % \"junit-jupiter\" % \"" + VERSION_REGEX + '"', VersionType.JUPITER),
                new Replacement("\"org.junit.platform\" % \"junit-platform-launcher\" % \"" + VERSION_REGEX + '"', VersionType.PLATFORM)
        ));
        update(Path.of("junit5-migration-gradle/build.gradle"), List.of(gradleBomReplacement));
        update(Path.of("junit5-migration-gradle/README.md"), List.of(
                new Replacement("org.junit.jupiter:junit-jupiter:" + VERSION_REGEX, VersionType.JUPITER),
                new Replacement("org.junit.vintage:junit-vintage-engine:" + VERSION_REGEX, VersionType.VINTAGE)
        ));
        update(Path.of("junit5-migration-maven/pom.xml"), List.of(mavenBomReplacement));
        update(Path.of("junit5-modular-world/compile.jsh"), List.of(
                new Replacement("platformVersion = \"" + VERSION_REGEX + '"', VersionType.PLATFORM),
                new Replacement("jupiterVersion = \"" + VERSION_REGEX + '"', VersionType.JUPITER),
                new Replacement("vintageVersion = \"" + VERSION_REGEX + '"', VersionType.VINTAGE)
        ));
        update(Path.of("junit5-multiple-engines/build.gradle.kts"), List.of(
                new Replacement("junitBomVersion = \"" + VERSION_REGEX + '"', VersionType.BOM)
        ));
    }

    void update(Path path, List<Replacement> replacements) throws IOException {
        System.out.printf("Updating %s...", path);
        System.out.flush();
        int matches = 0;
        var content = new StringBuilder(Files.readString(path));
        for (var replacement : replacements) {
            var minIndex = 0;
            var matcher = replacement.regex.matcher(content);
            while (matcher.find(minIndex)) {
                matches++;
                int start = matcher.start(1);
                int end = matcher.end(1);
                int oldLength = end - start;
                String newVersion = replacement.newVersion(jupiterVersion);
                content.replace(start, end, newVersion);
                minIndex = end + newVersion.length() - oldLength;
            }
        }
        Files.writeString(path, content);
        System.out.printf(" %d%n", matches);
        if (matches == 0) {
            throw new IllegalStateException("No matches found in " + path);
        }
    }

    record Replacement(Pattern regex, VersionType versionType) {
        Replacement(String regex, VersionType versionType) {
            this(Pattern.compile(regex), versionType);
        }

        String newVersion(String jupiterVersion) {
            return switch (versionType) {
                case BOM, JUPITER, VINTAGE -> jupiterVersion;
                case PLATFORM -> jupiterVersion.replaceFirst("\\d+\\.", "1.");
            };
        }
    }

    enum VersionType {
        BOM,
        JUPITER,
        PLATFORM,
        VINTAGE
    }
}
