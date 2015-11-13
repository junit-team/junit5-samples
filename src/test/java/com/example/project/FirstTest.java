
package com.example.project;

import static org.junit.gen5.api.Assertions.assertEquals;

import org.junit.gen5.api.Name;
import org.junit.gen5.api.Test;
import org.junit.gen5.api.TestName;

class FirstTest {

	@Test
	@Name("1 + 1 = 2")
	void myTest(@TestName String testName) {
		assertEquals(2, 1 + 1, "1 + 1 should equal 2");
		assertEquals("1 + 1 = 2", testName, () -> "testName is injected correctly");
	}

}
