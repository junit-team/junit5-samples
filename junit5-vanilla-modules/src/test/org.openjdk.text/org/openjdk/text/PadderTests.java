package org.openjdk.text;

import org.junit.jupiter.api.*;

class PadderTests {

	@Test
	void simpleLeftPad() {
		Assertions.assertEquals("    |", Padder.leftPad("|", 5));
	}

	@Test
	void packagePrivate() {
		Assertions.assertEquals("package-<>-private", Padder.packagePrivateWrap("<>"));
	}
}
