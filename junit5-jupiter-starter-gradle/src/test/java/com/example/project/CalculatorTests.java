/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTests {
	@Test
	void addsTwoNumbers() {
		Calculator calculator = new Calculator();
		assertAll(
				() -> assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2"),
				() -> assertEquals(2, calculator.add(0, 2), "0 + 2 should equal 2")
		);
	}
}
