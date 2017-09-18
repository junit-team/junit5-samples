package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class CalculatorTests {

  @Test
  @DisplayName("JUnit 5 test!")
  void myFirstTest(TestInfo testInfo) {
    Calculator calculator = new Calculator();
    assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
    assertEquals("JUnit 5 test!", testInfo.getDisplayName(), () -> "TestInfo is injected");
  }
}
