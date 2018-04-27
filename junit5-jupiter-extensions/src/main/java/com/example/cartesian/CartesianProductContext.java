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

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

public class CartesianProductContext implements TestTemplateInvocationContext, ParameterResolver {

	private final List<?> parameters;

	CartesianProductContext(List<?> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String getDisplayName(int invocationIndex) {
		return invocationIndex + ": " + parameters.toString();
	}

	@Override
	public List<Extension> getAdditionalExtensions() {
		return Collections.singletonList(this);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getIndex() < parameters.size();
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameters.get(parameterContext.getIndex());
	}

}
