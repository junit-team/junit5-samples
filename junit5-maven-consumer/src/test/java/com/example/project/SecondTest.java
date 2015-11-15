
package com.example.project;

import static org.junit.gen5.api.Assertions.assertEquals;

import org.junit.gen5.api.Disabled;
import org.junit.gen5.api.Test;

class SecondTest {

	@Test
	@Disabled
	void mySecondTest() {
		assertEquals(2, 1, "2 is not equal to 1");
	}

}
