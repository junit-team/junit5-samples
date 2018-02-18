package com.example.application;

import java.io.*;
import org.junit.jupiter.api.*;

@DisplayName("com.example.application/com.example.application.MainTests")
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
			Main.main("99", "1");
			String actual = bytes.toString().replace("\n", "").replace("\r", "");
			Assertions.assertEquals("99 + 1 = 100", actual);
		} finally {
			System.setOut(standard);
		}
	}
}
