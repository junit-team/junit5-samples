/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("Assume example")
class AssumeTest {

    @Test
    @DisplayName("Passes if current second is even")
    void isSecondEven(TestReporter testReporter) {
        int currentSecond = LocalTime.now().getSecond();
        testReporter.publishEntry("Current second", String.valueOf(currentSecond));
        assumeTrue(currentSecond % 2 == 0, () -> "If current second is even test passes else it is aborted");
    }

}
