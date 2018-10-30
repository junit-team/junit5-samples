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

import static com.example.singleton.SingletonExtension.Layer.CONTAINER;
import static com.example.singleton.SingletonExtension.Layer.GLOBAL;
import static com.example.singleton.SingletonExtension.Layer.TEST;

import com.example.singleton.SingletonExtension.Singleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @since 5.4
 * @see SingletonExtension
 */
@ExtendWith(SingletonExtension.class)
class SingletonExtensionTests {

	private void log(String message, StringBuilder builder) {
		String identity = "0x" + Integer.toHexString(System.identityHashCode(builder)).toUpperCase();
		System.out.println(identity + ": " + message);
	}

	@Test
	void test1(@Singleton(Builder123.class) StringBuilder builder) {
		log("test1", builder);
	}

	@Test
	void test2(@Singleton(value = Builder123.class, layer = GLOBAL) StringBuilder builder) {
		log("test2", builder);
	}

	@Test
	void test3(@Singleton(value = Builder123.class, layer = CONTAINER) StringBuilder builder) {
		log("test3", builder);
	}

	@Test
	void test4(@Singleton(value = Builder123.class, layer = CONTAINER) Builder123 resource) {
		log("test4", resource.getInstance());
	}

	@Test
	void test5(@Singleton(value = Builder123.class, layer = TEST) StringBuilder builder) {
		log("test5", builder);
	}

	@Test
	void test6(@Singleton(value = Builder123.class, layer = TEST) StringBuilder builder) {
		log("test6", builder);
	}

}
