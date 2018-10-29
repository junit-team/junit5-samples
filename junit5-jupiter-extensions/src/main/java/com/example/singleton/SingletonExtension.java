package com.example.singleton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SingletonExtension implements ParameterResolver {

	private static final Namespace NAMESPACE = Namespace.create(SingletonExtension.class);

	public static class Resource<T> implements CloseableResource, Supplier<T> {

		private final T instance;

		public Resource(T instance) {
			this.instance = instance;
		}

		@Override
		public void close() throws Exception {
			if (instance instanceof AutoCloseable) {
				((AutoCloseable) instance).close();
			}
		}

		@Override
		public T get() {
			return instance;
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface New {
		Class<? extends Resource<?>> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface Singleton {
		Class<? extends Resource<?>> value();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.isAnnotated(Singleton.class) ^ parameterContext.isAnnotated(New.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		if (parameterContext.isAnnotated(Singleton.class)) {
			Singleton singleton = parameterContext.findAnnotation(Singleton.class).orElseThrow(AssertionError::new);
			ExtensionContext engineContext = extensionContext.getRoot();
			Store store = engineContext.getStore(NAMESPACE);
			Resource<?> resource = store.getOrComputeIfAbsent(singleton.value());
			return resource.get();
		}
		if (parameterContext.isAnnotated(New.class)) {
			New annotation = parameterContext.findAnnotation(New.class).orElseThrow(AssertionError::new);
			Store store = extensionContext.getStore(NAMESPACE);
			Resource<?> resource = store.getOrComputeIfAbsent(annotation.value());
			return resource.get();
		}
		throw new ParameterResolutionException("Expected annotation not present?!");
	}
}
