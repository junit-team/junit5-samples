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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.util.ReflectionUtils;

public class CartesianProductProvider implements TestTemplateInvocationContextProvider {

	@Override
	public boolean supportsTestTemplate(ExtensionContext context) {
		return context.getRequiredTestMethod().isAnnotationPresent(CartesianProductTest.class);
	}

	@Override
	public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
		List<List<?>> lists = new ArrayList<>();
		CartesianProductTest annotation = context.getRequiredTestMethod().getAnnotation(CartesianProductTest.class);
		List<String> alpha = Arrays.asList(annotation.value());
		if (!alpha.isEmpty()) {
			for(int i = 0; i < context.getRequiredTestMethod().getParameterTypes().length; i++) {
				lists.add(alpha);
			}
		} else {
			Class<?> type = context.getRequiredTestClass();
			String name = context.getRequiredTestMethod().getName();
			Optional<Method> optionalMethod = ReflectionUtils.findMethod(type, name);
			if (!optionalMethod.isPresent()) {
				throw new IllegalArgumentException("Method `List<List<?>> "+name+"()` not found");
			}
			lists = (List<List<?>>) ReflectionUtils.invokeMethod(optionalMethod.get(), null);
		}

		List<TestTemplateInvocationContext> contexts = new ArrayList<>();
		for (List<?> parameters : cartesianProduct(lists)) {
			contexts.add(new CartesianProductContext(parameters));
		}
		return contexts.stream();
	}

	private static List<List<?>> cartesianProduct(List<List<?>> lists) {
		List<List<?>> resultLists = new ArrayList<>();
		if (lists.size() == 0) {
			resultLists.add(new ArrayList<>());
			return resultLists;
		}
		List<?> firstList = lists.get(0);
		List<List<?>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
		for (Object condition : firstList) {
			for (List<?> remainingList : remainingLists) {
				ArrayList<Object> resultList = new ArrayList<>();
				resultList.add(condition);
				resultList.addAll(remainingList);
				resultLists.add(resultList);
			}
		}
		return resultLists;
	}

}
