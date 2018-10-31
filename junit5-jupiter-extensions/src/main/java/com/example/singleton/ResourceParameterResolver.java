package com.example.singleton;

import static org.junit.platform.commons.support.ReflectionSupport.newInstance;

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

	private static final Namespace NAMESPACE = Namespace.create(ResourceParameterResolver.class);

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.isAnnotated(GlobalResource.class)
				^ parameterContext.isAnnotated(ClassResource.class)
				^ parameterContext.isAnnotated(MethodResource.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		Class<?> targetClass = parameterContext.getParameter().getType();
		Resource resource = getOrCreateResource(parameterContext, extensionContext);
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

	private Resource getOrCreateResource(ParameterContext parameterContext, ExtensionContext methodContext) {
		Optional<MethodResource> methodResource = parameterContext.findAnnotation(MethodResource.class);
		if (methodResource.isPresent()) {
			return createMethodResource(methodResource.get(), parameterContext.getIndex(), methodContext);
		}
		Optional<ClassResource> classResource = parameterContext.findAnnotation(ClassResource.class);
		if (classResource.isPresent()) {
			return getOrCreateClassResource(classResource.get(), methodContext);
		}
		GlobalResource global = parameterContext.findAnnotation(GlobalResource.class).orElseThrow(AssertionError::new);
		return getOrCreateEngineResource(global, methodContext);
	}

	private Resource createMethodResource(MethodResource methodResource, int index, ExtensionContext methodContext) {
		Class<? extends Resource> type = methodResource.value();
		Object key = type.getName() + '@' + index;
		Resource resource = newInstance(type);
		methodContext.getStore(NAMESPACE).put(key, resource);
		return resource;
	}

	private Resource getOrCreateClassResource(ClassResource classResource, ExtensionContext methodContext) {
		Class<? extends Resource> type = classResource.value();
		Object key = type.getName() + '/' + classResource.id();
		ExtensionContext context = methodContext.getParent().orElseThrow(AssertionError::new);
		return getOrCreateSharedResource(type, key, methodContext, context);
	}

	private Resource getOrCreateEngineResource(GlobalResource globalResource, ExtensionContext methodContext) {
		Class<? extends Resource> type = globalResource.value();
		Object key = type.getName() + '/' + globalResource.id();
		ExtensionContext context = methodContext.getRoot();
		return getOrCreateSharedResource(type, key, methodContext, context);
	}

	private Resource getOrCreateSharedResource(Class<? extends Resource> type, Object key, ExtensionContext methodContext, ExtensionContext storeContext) {
		// first, look up existing resource in current context's hierarchy
		Resource resource = methodContext.getStore(NAMESPACE).get(key, Resource.class);
		if (resource != null) {
			return resource;
		}
		// still here: create resource and store it in the current context
		return storeContext.getStore(NAMESPACE).getOrComputeIfAbsent(key, k -> newInstance(type), Resource.class);
	}

}
