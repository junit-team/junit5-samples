package spock

import spock.lang.Specification

class SpockSpec extends Specification {
  def "assert that the sum of two numbers equals an expected result"() {
    expect:
    a + b == result

    where:
    a | b | result
    1 | 2 | 3
  }
}
