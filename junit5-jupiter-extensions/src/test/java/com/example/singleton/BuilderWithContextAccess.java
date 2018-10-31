package com.example.singleton;

import org.junit.jupiter.api.extension.ExtensionContext;

public class BuilderWithContextAccess extends AbstractStringBuilderResource {

	public BuilderWithContextAccess(ExtensionContext context) {
		super(new StringBuilder(context.getDisplayName()));
	}

}
