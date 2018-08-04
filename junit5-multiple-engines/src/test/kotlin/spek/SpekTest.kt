package spek

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class SpekTest : Spek({
    describe("a simple test") {
        it("asserts that 1 + 2 equals 3") {
            assert(1 + 2 == 3)
        }
    }
})
