/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.{DisplayName, Test}

class CalculatorTests {

	@Test
	@DisplayName("should add two numbers")
	def test(): Unit = Seq(
		(0, 1, 1),
		(1, 1, 2),
		(1, 2, 3),
		(49, 51, 100),
		(1, 100, 101),
	) collect {
		case Tuple3(a, b, result) =>
			assertEquals(result, Calculator.add(a, b), s"$a + $b should equal $result")
	}
}
