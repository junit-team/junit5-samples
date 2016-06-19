A simple JUnit 5 extension to integrate Mockito into JUnit 5 tests somewhat simpler.

The `MockitoExtension` showcases the `TestInstancePostProcessor` and `ParameterResolver`
extension APIs of JUnit 5 by providing dependency injection support at the field level
via Mockito's `@Mock` annotation and at the method level via the JUnit team's demo
`@InjectMock` annotation.

See also:

- [Mockito issue #438](https://github.com/mockito/mockito/issues/438)
- [Mockito issue #445](https://github.com/mockito/mockito/issues/445)
