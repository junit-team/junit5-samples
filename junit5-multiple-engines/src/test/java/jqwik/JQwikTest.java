package jqwik;

import net.jqwik.api.Example;
import net.jqwik.api.Label;

class JQwikTest {
	@Example
	@Label("Hello from JQwik!")
	boolean helloFromJQwik() {
		return true;
	}
}
