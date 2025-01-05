/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

// default package

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class StagingRepoInjector {

    public static void main(String[] args) throws Exception {
        new StagingRepoInjector(args[0]).appendAfter();
    }

    private final String stagingRepoUrl;

    public StagingRepoInjector(String stagingRepoUrl) {
        this.stagingRepoUrl = stagingRepoUrl;
    }

    private void appendAfter() throws Exception {

        appendAfter("junit5-jupiter-extensions/build.gradle", "mavenCentral()",
                "%n\tmaven { url = '%s' }".formatted(stagingRepoUrl));

        replace("junit5-jupiter-starter-ant/build.sh", "https://repo1.maven.org/maven2", stagingRepoUrl);

        appendAfter("junit5-jupiter-starter-bazel/MODULE.bazel", "\"https://repo1.maven.org/maven2\",",
                "%n        \"%s\",".formatted(stagingRepoUrl));

        appendAfter("junit5-jupiter-starter-gradle/build.gradle", "mavenCentral()",
                "%n\tmaven { url = '%s' }".formatted(stagingRepoUrl));

        appendAfter("junit5-jupiter-starter-gradle-groovy/build.gradle", "mavenCentral()",
                "%n\tmaven { url = '%s' }".formatted(stagingRepoUrl));

        appendAfter("junit5-jupiter-starter-gradle-kotlin/build.gradle.kts", "mavenCentral()",
                "%n\tmaven(url = \"%s\")".formatted(stagingRepoUrl));

        var mavenPomRepo = """
                
                
                \t<repositories>
                \t\t<repository>
                \t\t\t<id>central-staging</id>
                \t\t\t<url>%s</url>
                \t\t</repository>
                \t</repositories>
                """.stripIndent().stripTrailing().formatted(stagingRepoUrl);

        appendAfter("junit5-jupiter-starter-maven/pom.xml", "</build>", mavenPomRepo);

        appendAfter("junit5-jupiter-starter-maven-kotlin/pom.xml", "</build>", mavenPomRepo);

        appendAtEnd("junit5-jupiter-starter-sbt/build.sbt",
                "%nresolvers += \"central-staging\" at \"%s\"%n".formatted(stagingRepoUrl));

        appendAfter("junit5-migration-gradle/build.gradle", "mavenCentral()",
                "%n\tmaven { url = '%s' }".formatted(stagingRepoUrl));

        appendAfter("junit5-migration-maven/pom.xml", "</build>", mavenPomRepo);

        replace("junit5-modular-world/BUILDING", "\"https://repo1.maven.org/maven2\"",
                "group.startsWith(\"org.junit\") ? \"%s\" : \"https://repo1.maven.org/maven2\"".formatted(stagingRepoUrl));

        appendAfter("junit5-multiple-engines/build.gradle.kts", "mavenCentral()",
                "%n\tmaven(url = \"%s\")".formatted(stagingRepoUrl));
    }

    void appendAfter(String path, String token, String addedContent) throws IOException {
        process(path, content -> {
            if (content.indexOf(token) == -1) {
                throw new IllegalStateException("Token not found in " + path);
            }
            content.insert(content.indexOf(token) + token.length(), addedContent);
        });
    }

    void replace(String path, String token, String replacement) throws IOException {
        process(path, content -> {
            if (content.indexOf(token) == -1) {
                throw new IllegalStateException("Token not found in " + path);
            }
            content.replace(content.indexOf(token), content.indexOf(token) + token.length(), replacement);
        });
    }

    void appendAtEnd(String path, String addedContent) throws IOException {
        process(path, content -> content.append(addedContent));
    }

    void process(String path, Consumer<StringBuilder> modification) throws IOException {
        System.out.printf("Processing %s...", path);
        System.out.flush();
        var file = Path.of(path);
        var content = new StringBuilder(Files.readString(file));
        modification.accept(content);
        Files.writeString(file, content);
        System.out.println(" OK");
    }
}
