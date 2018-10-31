package com.example.singleton;

import com.example.singleton.ResourceParameterResolver.ClassResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResourceParameterResolver.class)
class Builder123Tests {

	@Test
	void startsWith123(@ClassResource(Builder123.class) StringBuilder builder) {
		Assertions.assertEquals("123", builder.toString().substring(0, 3));
	}

}
