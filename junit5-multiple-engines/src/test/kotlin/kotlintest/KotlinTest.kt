package kotlintest

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class KotlinTest : StringSpec({
  "1 + 2 should be 3" {
    1 + 2 shouldBe 3
  }
})
