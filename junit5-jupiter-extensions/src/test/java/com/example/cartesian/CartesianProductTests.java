/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.cartesian;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;

class CartesianProductTests {

	@CartesianProductTest({"0", "1"})
	void fourBits(String a, String b, String c, String d) {
		int value = Integer.parseUnsignedInt(a + b + c + d, 2);
		assertTrue((0B0000 <= value) && (value <= 0B1111));
	}

	@CartesianProductTest
	@DisplayName("S ⨯ T ⨯ U")
	void multiType(String string, Class<?> type, TimeUnit unit, TestInfo info) {
		assertTrue(string.endsWith("a"));
		assertTrue(type.isInterface());
		assertTrue(unit.name().endsWith("S"));
		assertTrue(info.getTags().isEmpty());
	}

	static List<List<?>> multiType() {
		return Arrays.asList(
				Arrays.asList("Alpha", "Omega"),
				Arrays.asList(Runnable.class, Comparable.class),
				Arrays.asList(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES)
		);
	}

}
