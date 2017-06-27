package LRParser;

import java.util.List;

import grammar.Token;

public class Main {
	public static void println(List<Token> tokenList) {
		for (Token token : tokenList) {
			System.out.print(token.toString());
		}
		System.out.println();
	}
}
