/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.singleton;

import com.example.singleton.SingletonExtension.Singleton;
import java.util.function.Supplier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 5.4
 * @see SingletonExtension
 */
@ExtendWith(SingletonExtension.class)
class SingletonExtensionTests {

	static class Builder123 implements AutoCloseable, Supplier<StringBuilder> {

		private final StringBuilder builder = new StringBuilder("123");

		@Override
		public void close() {
			builder.setLength(0);
		}

		@Override
		public StringBuilder get() {
			return builder;
		}
	}

	@Test
	@Disabled("[WIP]")
	void injectsInteger(@Singleton(Builder123.class) StringBuilder builder) {
		assertEquals("123", builder.toString());
	}

}
