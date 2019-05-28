package com.example.project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CalculatorJUnit4Tests {

	private int a, b, result;

	public CalculatorJUnit4Tests(int a, int b, int result) {
		this.a = a;
		this.b = b;
		this.result = result;
	}

	@Parameters(name = "{index}: add({0}, {1}) = {2}")
	public static Iterable<Integer[]> data() {
		return asList(
				new Integer[][] {
						{ 0, 1, 1 },
						{ 1, 1, 2 },
						{ 1, 2, 3 },
						{ 49, 51, 100 },
						{ 1, 100, 101 },
						{ -1, 200, 199 },
				}
		);
	}

	@Test
	public void add() {
		Calculator calculator = new Calculator();
		final String message = format("%d + %d = %d", a, b, result);
		assertEquals(message, result, calculator.add(a, b));
	}
}
