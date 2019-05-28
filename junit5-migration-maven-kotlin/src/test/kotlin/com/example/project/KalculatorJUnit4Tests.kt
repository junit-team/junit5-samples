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

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class KalculatorJUnit4Tests(private val a: Int, private val b: Int, private val result: Int) {

	companion object {
		@JvmStatic
		@Parameters(name = "{index}: add({0}, {1}) = {2}")
		fun data() = arrayOf(
				arrayOf(0, 1, 1),
				arrayOf(1, 1, 2),
				arrayOf(1, 2, 3),
				arrayOf(49, 51, 100),
				arrayOf(1, 100, 101),
				arrayOf(-1, 200, 199)
		)
	}

	@Test
	fun test() = assertEquals("$a + $b should equal $result", result, Kalculator().add(a, b))
}
