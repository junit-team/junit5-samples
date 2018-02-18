package black.box;

// import com.example.application.*; "package is declared in module com.example.application, which does not export it"
import com.example.tool.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

@DisplayName("black.box/black.box.BlackBoxTests")
class BlackBoxTests {

	@Test
	void packageName() {
		Assertions.assertEquals("com.example.tool", Calculator.class.getPackage().getName());
	}

	@Test
	// @EnabledIf("com.example.tool.Calculator.class.getModule().isNamed()")
	void moduleName() {
		Assumptions.assumeTrue(Calculator.class.getModule().isNamed(), "Calculator resides in a named module");
		Assertions.assertEquals("com.example.tool", Calculator.class.getModule().getName());
	}

	@RepeatedTest(value = 5, name = "{currentRepetition} + {totalRepetitions}")
	void add(RepetitionInfo info) {
		int a = info.getCurrentRepetition();
		int b = info.getTotalRepetitions();
		int actual = new Calculator().add(a, b);
		int expected = a + b;
		Assertions.assertEquals(expected, actual);
	}
}
