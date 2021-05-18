package spock

import spock.lang.Specification

class SpockSpec extends Specification {
  def "length of Spock's names"() {
    expect:
    name.size() == length

    where:
    name     | length
    "Spock"  | 5
  }
}
