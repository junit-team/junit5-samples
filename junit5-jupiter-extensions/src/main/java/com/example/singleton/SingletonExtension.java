package com.example.singleton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SingletonExtension implements ParameterResolver {

	public interface Resource<T> extends CloseableResource, Supplier<T> {

		@Override
		default void close() {}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface Singleton {
		Class<? extends Resource<?>> value();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.isAnnotated(Singleton.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Singleton singleton = parameterContext.findAnnotation(Singleton.class).orElseThrow(AssertionError::new);
		ExtensionContext engineContext = extensionContext.getRoot();
		Store store = engineContext.getStore(ExtensionContext.Namespace.GLOBAL);
		Resource<?> resource = store.getOrComputeIfAbsent(singleton.value());
		return resource.get();
	}
}
