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
