package org.openjdk.hello;

import org.openjdk.text.Padder;

public class Main {

	public static void main(String... args) {
		String adj = args.length > 0 ? (" " + args[0]) : "";
		System.out.println(Padder.leftPad("Hello," + adj + " world!", 90));
	}
}
