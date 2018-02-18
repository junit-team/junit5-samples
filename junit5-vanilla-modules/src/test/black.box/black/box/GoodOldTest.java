package black.box;

import com.example.tool.*;
import org.junit.*;

public class GoodOldTest {

	@Test
	public void eightteenEqualsNineAndNine() {
		Assert.assertEquals(18, new Calculator().add(9, 9));
	}
}
