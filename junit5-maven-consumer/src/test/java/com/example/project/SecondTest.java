
package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SecondTest {

	@Test
	@Disabled
	void mySecondTest() {
		assertEquals(2, 1, "2 is not equal to 1");
	}

}
