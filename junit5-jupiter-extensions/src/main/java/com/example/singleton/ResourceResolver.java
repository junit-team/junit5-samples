package com.example.singleton;

import static org.junit.platform.commons.support.ReflectionSupport.newInstance;

import com.example.singleton.ResourceParameterResolver.*;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

class ResourceResolver {

	private static final Namespace NAMESPACE = Namespace.create(ResourceResolver.class);

	private final ParameterContext parameterContext;
	private final ExtensionContext methodContext;

	ResourceResolver(ParameterContext parameterContext, ExtensionContext methodContext) {
		this.parameterContext = parameterContext;
		this.methodContext = methodContext;
	}

	Resource resolveResource() {
		Optional<MethodResource> methodResource = parameterContext.findAnnotation(MethodResource.class);
		if (methodResource.isPresent()) {
			return createMethodResource(methodResource.get(), parameterContext.getIndex());
		}
		Optional<ClassResource> classResource = parameterContext.findAnnotation(ClassResource.class);
		if (classResource.isPresent()) {
			return getOrCreateClassResource(classResource.get());
		}
		Optional<GlobalResource> globalResource = parameterContext.findAnnotation(GlobalResource.class);
		if (globalResource.isPresent()) {
			return getOrCreateGlobalResource(globalResource.get());
		}
		throw new ParameterResolutionException("Can't resolve resource for: " + parameterContext);
	}

	private Resource createMethodResource(MethodResource methodResource, int index) {
		Class<? extends Resource> type = methodResource.value();
		Object key = type.getName() + '@' + index;
		Resource resource = newInstance(type);
		methodContext.getStore(NAMESPACE).put(key, resource);
		return resource;
	}

	private Resource getOrCreateClassResource(ClassResource classResource) {
		Class<? extends Resource> type = classResource.value();
		Object key = type.getName() + '/' + classResource.id();
		ExtensionContext context = methodContext.getParent().orElseThrow(AssertionError::new);
		return getOrCreateResource(type, key, context);
	}

	private Resource getOrCreateGlobalResource(GlobalResource globalResource) {
		Class<? extends Resource> type = globalResource.value();
		Object key = type.getName() + '/' + globalResource.id();
		ExtensionContext context = methodContext.getRoot();
		return getOrCreateResource(type, key, context);
	}

	private Resource getOrCreateResource(Class<? extends Resource> type, Object key, ExtensionContext storeContext) {
		// first, look up existing resource in current method context's hierarchy
		Resource resource = methodContext.getStore(NAMESPACE).get(key, Resource.class);
		if (resource != null) {
			return resource;
		}
		// still here: create resource and store it in the specified store context
		return storeContext.getStore(NAMESPACE).getOrComputeIfAbsent(key, k -> newInstance(type), Resource.class);
	}

}