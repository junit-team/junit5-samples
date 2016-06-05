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

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.api.TestInfo;
import org.junit.gen5.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @see MockitoExtension
 */
@ExtendWith(MockitoExtension.class)
class MockitoExtensionInBaseClassTest {

	@Mock
	private NumberGenerator numberGenerator;

	@BeforeEach
	void initialize(@InjectMock MyType myType, TestInfo testInfo) {
		when(myType.getName()).thenReturn(testInfo.getDisplayName());
		when(numberGenerator.next()).thenReturn(42);
	}

	@Test
	void simpleTestWithInjectedMock(@InjectMock MyType myType) {
		assertEquals("simpleTestWithInjectedMock(MyType)", myType.getName());
		assertEquals(42, numberGenerator.next());
	}

	@Test
	void secondTestWithInjectedMock(@InjectMock MyType myType) {
		assertEquals("secondTestWithInjectedMock(MyType)", myType.getName());
		assertEquals(42, numberGenerator.next());
	}

}
