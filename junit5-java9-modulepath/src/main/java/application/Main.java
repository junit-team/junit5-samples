/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package application;

import java.util.List;
import org.apache.commons.lang3.SystemUtils;

public class Main {

	public static void main(String... args) {
		System.out.println("Main.main(args = [" + List.of(args) + "])");
		System.out.println("  IS_JAVA_9: " + SystemUtils.IS_JAVA_9);
	}

}
