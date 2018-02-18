open module black.box {
	//
	// modules under test
	//
	requires com.example.application;
	requires com.example.tool;

	//
	// test framework api
	//
	requires org.junit.jupiter.api;
	requires junit; // JUnit 4 "automatic module"
}
