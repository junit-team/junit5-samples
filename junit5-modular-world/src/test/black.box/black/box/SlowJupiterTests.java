/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package black.box;

import org.junit.jupiter.api.*;

@DisplayName("black.box/black.box.SlowJupiterTests")
class SlowJupiterTests {

	@Test
	void wait600() throws Exception {
		Thread.sleep(600);
	}

	@Test
	void wait700() throws Exception {
		Thread.sleep(700);
	}
}
