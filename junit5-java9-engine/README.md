# junit5-java9-engine

The `junit5-java9-engine` project demonstrates how to write and register your own
TestEngine implementation using Java 9 and Gradle. This engine does not find any
tests in containers, but _discovers_ a configurable amount of ice cream scoops.

This sample project does not aim to demonstrate how to use the JUnit Platform APIs.
For detailed  information on the JUnit Jupiter programming and extension models,
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

See details in package [ice.cream](src/main/java/ice/cream).

## Running the Ice-Cream-Machine

Sample command line to run register and execute the ice.cream machine:
```
	java
		-Dscoops=3
		--module-path ...                   // deps;build/classes/java/main
		--add-modules ice.cream             // resolve ice.cream module by default
		--module org.junit.platform.console // start the JUnit Platform Console module
		--scan-class-path                   // does nothing, just here to satisfy the launcher
	]
}
```

Or just let Gradle do the work:
```
gradlew build

:[...]
:testIceCreamMachine
.
'-- Ice Cream Machine 47.11 (ice.cream) [OK]
  +-- Stracciatella [OK]
  +-- Vanilla [OK]
  '-- Chocolate [OK]
```