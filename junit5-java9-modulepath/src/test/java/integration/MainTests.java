/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import application.Main;
import org.junit.jupiter.api.Test;

class MainTests {

	@Test
	void residesInNamedModule() {
		assertEquals("application", Main.class.getModule().getName());
	}

}
