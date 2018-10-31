package com.example.singleton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ResourceParameterResolver implements ParameterResolver {

	public interface Resource<T> extends AutoCloseable, CloseableResource {

		T getInstance();

		@Override
		default void close() throws Exception {
			T instance = getInstance();
			if (instance instanceof AutoCloseable) {
				((AutoCloseable) instance).close();
			}
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface GlobalResource {
		Class<? extends Resource> value();
		String id() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface ClassResource {
		Class<? extends Resource> value();
		String id() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface MethodResource {
		Class<? extends Resource> value();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.isAnnotated(GlobalResource.class)
				^ parameterContext.isAnnotated(ClassResource.class)
				^ parameterContext.isAnnotated(MethodResource.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Resource resource = new ResourceResolver(parameterContext, extensionContext).resolveResource();
		Class<?> targetClass = parameterContext.getParameter().getType();
		if (Resource.class.isAssignableFrom(targetClass)) {
			return resource;
		}
		Object instance = resource.getInstance();
		if (instance.getClass().isAssignableFrom(targetClass)) {
			return instance;
		}
		throw new ParameterResolutionException("Parameter type isn't compatible: " + targetClass
				+ " cannot be assigned to " + Resource.class + " nor " + instance.getClass());
	}

}
