package black.box;

import org.junit.jupiter.api.*;
import org.openjdk.text.*;

class BlackBoxTests {

	@Test
	void moduleAndPackageMatch() {
		Assertions.assertEquals("org.openjdk.text", Padder.class.getModule().getName());
		Assertions.assertEquals("org.openjdk.text", Padder.class.getPackage().getName());
	}

	@RepeatedTest(value = 5, name = "Padder.leftPad(\"|\", {currentRepetition})")
	void leftPad(RepetitionInfo info) {
		int w = info.getCurrentRepetition();
		String actual = Padder.leftPad("|", w);
		StringBuilder expected = new StringBuilder();
		for (int i = 1; i < w; i++) {
			expected.append(' ');
		}
		expected.append('|');
		Assertions.assertEquals(expected.toString(), actual);
	}
}
