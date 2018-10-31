package com.example.singleton;

import com.example.singleton.SingletonParameterResolver.Singleton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SingletonParameterResolver.class)
class BuilderWithContextAccessTests {

	@Test
	@DisplayName(";-)")
	void smileyAsDisplayName(@Singleton(BuilderWithContextAccess.class) StringBuilder builder) {
		Assertions.assertEquals(";-)", builder.toString());
	}

}
