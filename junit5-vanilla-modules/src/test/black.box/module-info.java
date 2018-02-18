open module black.box {
	//
    // modules under test
	//
	requires org.openjdk.hello;
	requires org.openjdk.text;

	//
	// test framework api
	//
	requires org.junit.jupiter.api;
}
