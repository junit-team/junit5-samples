package org.openjdk.hello;

import java.io.*;
import org.junit.jupiter.api.*;

class MainTests {

	@Test
	void simpleName() {
		Assertions.assertEquals("Main", Main.class.getSimpleName());
	}

	@Test
	void main() {
		PrintStream standard = System.out;
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			System.setOut(new PrintStream(bytes));
			Main.main("modular");
			String actual = bytes.toString().replace("\n", "").replace("\r", "");
			Assertions.assertEquals(90, actual.length());
			Assertions.assertTrue(actual.endsWith("Hello, modular world!"));
		} finally {
			System.setOut(standard);
		}
	}
}
