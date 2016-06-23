/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package com.example.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

/**
 * @since 5.0
 * @see MockitoExtension
 */
@ExtendWith(MockitoExtension.class)
class MockitoExtensionInBaseClassTest {

	@Mock
	private NumberGenerator numberGenerator;

	@BeforeEach
	void initialize(@Mock MyType myType, TestInfo testInfo) {
		when(myType.getName()).thenReturn(testInfo.getDisplayName());
		when(numberGenerator.next()).thenReturn(42);
	}

	@Test
	void firstTestWithInjectedMock(@Mock MyType myType) {
		assertEquals("firstTestWithInjectedMock(MyType)", myType.getName());
		assertEquals(42, numberGenerator.next());
	}

	@Test
	void secondTestWithInjectedMock(@Mock MyType myType) {
		assertEquals("secondTestWithInjectedMock(MyType)", myType.getName());
		assertEquals(42, numberGenerator.next());
	}

	@Test
	void multipleImplicitlyNamedInjectedMocksOfSameTypeAreNotTheSameInstance(@Mock MyType myType1,
			@Mock MyType myType2) {

		assertNotNull(myType1);
		assertNotNull(myType2);

		assertNotSame(myType1, myType2, "Make sure the project was compiled with -parameters");

		assertTrue(myType1.toString().contains("myType1"));
		assertTrue(myType2.toString().contains("myType2"));
	}

	@Test
	void multipleExplicitlyNamedInjectedMocksOfSameTypeAreNotTheSameInstanceTest(@Mock(name = "one") MyType myType1,
			@Mock(name = "two") MyType myType2) {

		assertNotNull(myType1);
		assertNotNull(myType2);
		assertNotSame(myType1, myType2);

		assertTrue(myType1.toString().contains("one"));
		assertTrue(myType2.toString().contains("two"));
	}

}
