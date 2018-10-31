package com.example.singleton;

import com.example.singleton.SingletonParameterResolver.Resource;

public class Builder123 implements Resource<StringBuilder> {

	private final StringBuilder instance;

	public Builder123(/*org.junit.jupiter.api.extension.ExtensionContext context*/) {
		this.instance = new StringBuilder("123");
		log(">> created");
	}

	@Override
	public void close() {
		// Resource.super.close();
		log("<< closed");
	}

	@Override
	public StringBuilder getInstance() {
		return instance;
	}

	private void log(String message) {
		StringBuilder builder = getInstance();
		String identity = "0x" + Integer.toHexString(System.identityHashCode(builder)).toUpperCase();
		System.out.println(identity + ": " + message);
	}
}
