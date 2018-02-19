package org.openjdk.text;

public class Padder {

	public static String leftPad(String s, int w) {
		StringBuilder sb = new StringBuilder();
		int n = w - s.length();
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		sb.append(s);
		return sb.toString();
	}

	static String packagePrivateWrap(String s) {
		return "package-" + s + "-private";
	}
}
