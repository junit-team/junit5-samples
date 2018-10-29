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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.singleton.SingletonExtension.Singleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @since 5.4
 * @see SingletonExtension
 */
@ExtendWith(SingletonExtension.class)
class SingletonExtensionTests {

	@Test
	void test1(@Singleton(Builder123.class) StringBuilder builder) {
		assertEquals("123", builder.toString());
	}

	@Test
	void test2(@Singleton(Builder123.class) StringBuilder builder) {
		assertEquals("123", builder.toString());
	}

	@Test
	void test3(@Singleton(Builder123.class) StringBuilder builder) {
		assertEquals("123", builder.toString());
	}

}
