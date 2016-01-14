
package com.example.project;

import static org.junit.gen5.api.Assertions.assertEquals;

import org.junit.gen5.api.DisplayName;
import org.junit.gen5.api.Test;
import org.junit.gen5.api.TestInfo;

class FirstTest {

	@Test
	@DisplayName("My 1st JUnit 5 test! 😎")
	void myFirstTest(TestInfo testInfo) {
		assertEquals(2, 1 + 1, "1 + 1 should equal 2");
		assertEquals("My 1st JUnit 5 test! 😎", testInfo.getDisplayName(), () -> "TestInfo is injected correctly");
	}

}
