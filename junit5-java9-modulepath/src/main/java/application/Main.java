package application;

import java.util.List;
import org.apache.commons.lang3.SystemUtils;

public class Main {

	public static void main(String... args) {
		System.out.println("Main.main(args = [" + List.of(args) + "])");
		System.out.println("  IS_JAVA_9: " + SystemUtils.IS_JAVA_9);
	}

}
