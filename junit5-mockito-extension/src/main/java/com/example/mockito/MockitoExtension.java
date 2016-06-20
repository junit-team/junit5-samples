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

import static org.mockito.Mockito.mock;

import java.lang.reflect.Parameter;

import org.junit.gen5.api.extension.ExtensionContext;
import org.junit.gen5.api.extension.ExtensionContext.Namespace;
import org.junit.gen5.api.extension.ExtensionContext.Store;
import org.junit.gen5.api.extension.ParameterContext;
import org.junit.gen5.api.extension.ParameterResolver;
import org.junit.gen5.api.extension.TestInstancePostProcessor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * {@code MockitoExtension} showcases the {@link TestInstancePostProcessor}
 * and {@link ParameterResolver} extension APIs of JUnit 5 by
 * providing dependency injection support at the field level via Mockito's
 * {@link Mock @Mock} annotation and at the method level via our demo
 * {@link InjectMock @InjectMock} annotation.
 *
 * @since 5.0
 */
public class MockitoExtension implements TestInstancePostProcessor, ParameterResolver {

	private static final Namespace namespace = Namespace.of(MockitoExtension.class);

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
		MockitoAnnotations.initMocks(testInstance);
	}

	@Override
	public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getParameter().isAnnotationPresent(InjectMock.class);
	}

	@Override
	public Object resolve(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Store mocks = extensionContext.getStore(namespace);
		return getMock(parameterContext.getParameter(), mocks);
	}

	private Object getMock(Parameter parameter, Store mocks) {
		Class<?> mockType = parameter.getType();
		String mockName = parameter.getName();
		String mockKey = mockType.getCanonicalName() + ":" + mockName;
		return mocks.getOrComputeIfAbsent(mockKey, key -> mock(mockType));
	}

}
