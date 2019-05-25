/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package kotlintest

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class KotlinTest : StringSpec({
  "1 + 2 should be 3" {
    1 + 2 shouldBe 3
  }
})
