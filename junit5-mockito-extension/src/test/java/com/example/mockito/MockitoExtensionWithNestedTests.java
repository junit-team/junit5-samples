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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

/**
 * @since 5.0
 * @see MockitoExtension
 */
@ExtendWith(MockitoExtension.class)
class MockitoExtensionWithNestedTests {

	boolean baseClassTestRun = false;

	@BeforeEach
	void initializeBaseClass(@Mock MyType myType) {
		when(myType.getName()).thenReturn("base class");
	}

	@Test
	void baseClassTest(@Mock MyType myType) {
		assertEquals("base class", myType.getName());
		baseClassTestRun = true;
	}

	@Nested
	class FirstContext {

		@BeforeEach
		void initializeFirstNesting(@Mock YourType yourType, @Mock MyType myType) {
			when(yourType.getName()).thenReturn("first nesting");
			assertEquals("base class", myType.getName());
		}

		@Test
		void firstNestedTest(@Mock YourType yourType) {
			assertEquals("first nesting", yourType.getName());
		}

		@Nested
		class SecondContext {

			@BeforeEach
			void initializeSecondNesting(@Mock YourType yourType, @Mock MyType myType, @Mock TheirType theirType) {
				when(theirType.getName()).thenReturn("second nesting");
				assertEquals("base class", myType.getName());
				assertEquals("first nesting", yourType.getName());
			}

			@Test
			void secondContextTest(@Mock TheirType theirType) {
				assertEquals("second nesting", theirType.getName());
			}

		}

		@AfterEach
		void afterFirstContext(@Mock YourType yourType, @Mock MyType myType, @Mock TheirType theirType) {
			assertEquals("base class", myType.getName());

			if (baseClassTestRun) {
				assertNull(theirType.getName());
				assertNull(yourType.getName());
			}
		}

	}

}
