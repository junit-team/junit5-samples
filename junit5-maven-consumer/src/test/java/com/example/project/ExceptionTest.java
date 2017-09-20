package com.example.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ExceptionTest {

    private Calculator calculator = new Calculator();

    @Test
    @DisplayName("Expect an Exception when dividing by 0")
    public void assertNumberFormatException () {
        Assertions.assertThrows(ArithmeticException.class, () -> calculator.divide(5, 0),
            "Expected to throw an  Arithmetic exception");
    }

    @Test
    public void assumeDenominatorIsNotZero() {
        int denominator = 0;
        Assumptions.assumeTrue(denominator != 0);
        Assertions.assertEquals(5 / denominator, calculator.divide(5, denominator),
            "Expected to throw an  Arithmetic exception");
    }

}
