/*
 * Copyright 2019 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

public class SuiteWithSubsuites extends TestCase {
    private final String arg;

    public SuiteWithSubsuites(String name, String arg) {
        super(name);
        this.arg = arg;
    }

    public void hello() {
        System.out.println("arg = " + arg);
        Assert.assertNotNull(arg);
    }

    public static TestSuite suite() {
        TestSuite root = new TestSuite("allTests");
        TestSuite case1 = new TestSuite("Case1");
        case1.addTest(new SuiteWithSubsuites("hello", "world"));
        root.addTest(case1);
        TestSuite case2 = new TestSuite("Case2");
        case2.addTest(new SuiteWithSubsuites("hello", "WORLD"));
        root.addTest(case2);
        return root;
    }
}
