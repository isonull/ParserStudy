package string;

public class StringMethodTester {
	public static void main(String[] args) {
		String testStr1 = "a";
		String testStr2 = "(i+i)×i";
		String testStr3 = "abcd";
		String testStr4 = "qwertyuiopasdfghjklzxcvbnm";
		StringMethod.print(StringMethod.partition(testStr2, 3));
	}
}
