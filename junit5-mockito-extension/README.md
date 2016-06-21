A simple JUnit 5 extension to integrate Mockito into JUnit 5 tests somewhat simpler.

The `MockitoExtension` showcases the `TestInstancePostProcessor` and `ParameterResolver`
extension APIs of JUnit 5 by providing dependency injection support at the field level
and at the method parameter level via via Mockito 2.x's `@Mock` annotation.

See also:

- [Mockito issue #390](https://github.com/mockito/mockito/issues/390)
- [Mockito issue #438](https://github.com/mockito/mockito/issues/438)
- [Mockito issue #445](https://github.com/mockito/mockito/issues/445)
