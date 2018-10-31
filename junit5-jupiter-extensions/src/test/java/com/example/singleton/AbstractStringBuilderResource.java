package com.example.singleton;

import com.example.singleton.ResourceParameterResolver.Resource;

abstract class AbstractStringBuilderResource implements Resource<StringBuilder> {

	private final StringBuilder instance;

	AbstractStringBuilderResource(StringBuilder instance) {
		this.instance = instance;
		log(">> created");
	}

	@Override
	public void close() throws Exception {
		Resource.super.close();
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
