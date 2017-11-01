open module ice.cream {
	//
	// copied from "main"
	//
	requires org.junit.platform.engine;

	provides org.junit.platform.engine.TestEngine with ice.cream.Machine;

	//
	// test dependencies
	//
	requires org.junit.jupiter.api;

	// "open module" or "opens ice.cream to org.junit.platform.commons;"
}
