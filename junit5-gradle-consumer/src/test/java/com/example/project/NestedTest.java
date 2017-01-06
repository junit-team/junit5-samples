/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package com.example.project;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Nested test classes")
public class NestedTest {
    
    private Calculator calculator = new Calculator();
    
    @Nested
    @Tag("negative")
    class TestAddingNegativeNumber {
        
        @BeforeEach
        void setUp(TestInfo testInfo) {
            System.out.println("Executing: " + testInfo.getDisplayName());
        }
        
        @Test
        @DisplayName("Tests adding zero")
        void testAddingZero() {
            assertEquals(-2, calculator.add(-2, 0));
        }
        
        @Test
        @DisplayName("Tests adding two negative numbers")
        void testAddingNegative() {
            assertEquals(-3, calculator.add(-1, -2));
        }
        
    }
    
}