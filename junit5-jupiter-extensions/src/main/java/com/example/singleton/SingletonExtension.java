package com.example.singleton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SingletonExtension implements ParameterResolver {

	public interface Resource<T> extends AutoCloseable, CloseableResource {

		@Override
		default void close() throws Exception {
			T instance = getInstance();
			if (instance instanceof AutoCloseable) {
				((AutoCloseable) instance).close();
			}
		}

		T getInstance();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface Singleton {
		Class<? extends Resource> value();
		String id() default "";
	}

	private static final Namespace NAMESPACE = Namespace.create(SingletonExtension.class);

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.isAnnotated(Singleton.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Singleton singleton = parameterContext.findAnnotation(Singleton.class).orElseThrow(AssertionError::new);
		Resource resource = getOrCreateResource(singleton, extensionContext);
		if (Resource.class.isAssignableFrom(parameterContext.getParameter().getType())) {
			return resource;
		}
		return resource.getInstance();
	}

	private Resource getOrCreateResource(Singleton singleton, ExtensionContext extensionContext) {
		String id = singleton.id();
		Object key = singleton.value().getName() + (id.isEmpty() ? "" : '/' + id);
		Resource resource = extensionContext.getStore(NAMESPACE).get(key, Resource.class);
		if (resource != null) {
			return resource;
		}
		Store store = extensionContext.getRoot().getStore(NAMESPACE);
		return store.getOrComputeIfAbsent(key, k -> newResource(singleton, extensionContext), Resource.class);
	}

	// TODO Use ReflectionSupport...
	private Resource newResource(Singleton singleton, ExtensionContext extensionContext) {
		Class<? extends Resource> resourceClass = singleton.value();
		try {
			try {
				// prefer constructor that takes an extension context argument
				return resourceClass.getConstructor(ExtensionContext.class).newInstance(extensionContext);
			}
			catch (NoSuchMethodException e) {
				// fall-back to no-arg constructor
				return resourceClass.getConstructor().newInstance();
			}
		}
		catch (ReflectiveOperationException e) {
			throw new AssertionError("Creating instance of resource " + resourceClass + " failed", e);
		}
	}
}
