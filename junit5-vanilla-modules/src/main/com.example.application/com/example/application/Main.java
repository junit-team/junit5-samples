package com.example.application;

import com.example.tool.Calculator;

class Main {

	public static void main(String... args) {
		int a = args.length > 0 ? (Integer.valueOf(args[0])) : 1;
		int b = args.length > 1 ? (Integer.valueOf(args[1])) : 2;
		System.out.printf("%d + %d = %d%n", a, b, new Calculator().add(a, b));
	}

}
