package com.example.singleton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
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

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface New {
		Class<? extends Resource> value();
	}

	private static final Namespace NAMESPACE = Namespace.create(SingletonExtension.class);

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		// TODO Check parameter type...
		return parameterContext.isAnnotated(Singleton.class) ^ parameterContext.isAnnotated(New.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Resource resource = getOrCreateResource(parameterContext, extensionContext);
		if (Resource.class.isAssignableFrom(parameterContext.getParameter().getType())) {
			return resource;
		}
		return resource.getInstance();
	}

	private Resource getOrCreateResource(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Optional<New> methodResource = parameterContext.findAnnotation(New.class);
		if (methodResource.isPresent()) {
			return createMethodResource(methodResource.get(), extensionContext);
		}
		Singleton singleton = parameterContext.findAnnotation(Singleton.class).orElseThrow(AssertionError::new);
		return getOrCreateResource(singleton, extensionContext);
	}

	private Resource createMethodResource(New methodResource, ExtensionContext extensionContext) {
		Class<? extends Resource> type = methodResource.value();
		Store store = extensionContext.getStore(NAMESPACE);
		Resource resource = createResource(type, extensionContext);
		store.put(type.getName(), resource);
		return resource;
	}

	private Resource getOrCreateResource(Singleton singleton, ExtensionContext extensionContext) {
		String id = singleton.id();
		Object key = singleton.value().getName() + (id.isEmpty() ? "" : '/' + id);
		Resource resource = extensionContext.getStore(NAMESPACE).get(key, Resource.class);
		if (resource != null) {
			return resource;
		}
		Store store = extensionContext.getRoot().getStore(NAMESPACE);
		return store.getOrComputeIfAbsent(key, k -> createResource(singleton, extensionContext), Resource.class);
	}

	private Resource createResource(Singleton singleton, ExtensionContext extensionContext) {
		return createResource(singleton.value(), extensionContext);
	}

	// TODO Use ReflectionSupport...
	private Resource createResource(Class<? extends Resource> resourceClass, ExtensionContext extensionContext) {
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
