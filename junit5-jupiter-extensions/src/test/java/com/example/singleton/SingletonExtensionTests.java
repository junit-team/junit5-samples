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
import java.lang.reflect.Method;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @since 5.4
 * @see SingletonExtension
 */
@ExtendWith(SingletonExtension.class)
class SingletonExtensionTests {

	private void log(String message, StringBuilder builder, TestInfo info) {
		String identity = "0x" + Integer.toHexString(System.identityHashCode(builder)).toUpperCase();
		System.out.println(identity + ":   " + message + " // " + info.getTestMethod().map(Method::getName).orElse("?"));
	}

	@Test
	void test1(@Singleton(Builder123.class) StringBuilder builder, TestInfo info) {
		log("GLOBAL" , builder, info);
	}

	@Test
	void test2(@Singleton(Builder123.class) StringBuilder builder, TestInfo info) {
		log("GLOBAL", builder, info);
	}

	@Test
	void test3(@Singleton(value = Builder123.class, local = true) StringBuilder builder, TestInfo info) {
		log("LOCAL ", builder, info);
	}

	@Test
	void test4(@Singleton(value = Builder123.class, id = "T45") Builder123 resource, TestInfo info) {
		log("T45   ", resource.getInstance(), info);
	}

	@Test
	void test5(@Singleton(value = Builder123.class, id = "T45") StringBuilder builder, TestInfo info) {
		log("T45   ", builder, info);
	}

	@Nested
	class N1 {

		@Nested
		class N2 {
			@Test
			void n1(@Singleton(value = Builder123.class, id = "T45") Builder123 resource, TestInfo info) {
				log("T45   ", resource.getInstance(), info);
			}

			@Test
			void n2(@Singleton(Builder123.class) StringBuilder builder, TestInfo info) {
				log("GLOBAL", builder, info);
			}
		}
	}
}
