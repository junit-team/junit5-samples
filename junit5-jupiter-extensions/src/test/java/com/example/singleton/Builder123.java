package com.example.singleton;

import com.example.singleton.SingletonExtension.Resource;

public class Builder123 implements Resource<StringBuilder> {

	private final StringBuilder builder = new StringBuilder("123");

	@Override
	public StringBuilder get() {
		return builder;
	}
}
