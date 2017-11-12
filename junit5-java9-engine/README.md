# junit5-java9-engine

The `junit5-java9-engine` project demonstrates how to write and register your own
`TestEngine` implementation using Java 9 and Gradle. This engine does not find any
tests in containers, but _discovers_ a configurable amount of ice cream scoops.

This sample project does not aim to demonstrate how to use the JUnit Platform APIs.
For detailed  information on the JUnit Platform programming and extension models,
please consult the [User Guide](http://junit.org/junit5/docs/current/user-guide/).

## Module Descriptor of the Ice-Cream-Machine

Let's start with a basic module descriptor:

```
module ice.cream {
	requires org.junit.platform.engine;

	provides org.junit.platform.engine.TestEngine with ice.cream.Machine;
}
```

## Implementation of the Ice-Cream-Machine

Here's the outline of the `TestEngine` implementation:

```
package ice.cream;

public class Machine implements TestEngine {

	@Override
	public String getId() {
		return "ice-cream-machine";
	}

	@Override
	public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
		...generate Scoops, i.e. TestDescriptors.
	}

	@Override
	public void execute(ExecutionRequest request) {
		..."run" Scoops. Tell engine execution listener what we're doing.
	}
}
```

See classes in package [ice.cream](src/main/java/ice/cream) for details.

## Running the Ice-Cream-Machine

Sample command line to register and execute the ice.cream machine:
```
java
	-Dscoops=3
	--module-path ...                   // main classes + test classes + "external modules"
	--add-modules ALL-MODULE-PATH       // resolve all modules on the module-path by default
	--module org.junit.platform.console // start the JUnit Platform Console module
	--scan-modules                      // scan resolved modules for tests

```

Or let [Gradle](build.gradle) do the work:
```
gradlew build

:[...]
:testIceCreamMachine
╷
└─ Ice Cream Machine 47.11 (ice.cream) ✔
   ├─ Stracciatella ✔
   ├─ Stracciatella ✔
   └─ Vanilla ✔
```
