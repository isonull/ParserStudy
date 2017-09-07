package LRParser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Token.Token;
import grammar.Grammar;
import grammar.GrammarException;
import grammar.SymbolList;

public class Lexer {

	// private static final Pattern pattern =
	// Pattern.compile("\\+|-|\\*|cos|\\!|\\(|\\)|(\\d+(.\\d+)?)");

	public static Grammar grammar;

	// private static LinkedList<Token> tokenStream;

	public Lexer(Grammar grammar_) {
		grammar = grammar_;
	}

	// public static LinkedList<Token> lex(String input) throws Exception {
	// tokenStream = new LinkedList<Token>();
	//
	// int begin;
	// int end;
	// int oldEnd;
	// String lexeme;
	// Token token;
	// input = input.replaceAll("\\p{Space}+", " ");
	// Matcher matcher = pattern.matcher(input);
	//
	// while (matcher.find()) {
	// oldEnd = 0;
	// begin = matcher.start();
	// end = matcher.end();
	// if (oldEnd != 0 && begin != oldEnd + (input.charAt(oldEnd) == ' ' ? 1 :
	// 0)) {
	// throw new Exception("Unknown pattern found." + input.substring(0, begin)
	// + " is discarded.");
	// }
	// lexeme = input.substring(begin, end);
	// switch (lexeme) {
	// case "+": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("+")));
	// break;
	// }
	// case "-": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("-")));
	// break;
	// }
	// case "*": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("*")));
	// break;
	// }
	// case "cos": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("cos")));
	// break;
	// }
	// case "!": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("!")));
	// break;
	// }
	// case "(": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("(")));
	// break;
	// }
	// case ")": {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName(")")));
	// break;
	// }
	// default: {
	// if (!lexeme.contains(".")) {
	// tokenStream.add(TokenFactory.getToken(grammar.getSymbolByName("INT"),
	// Integer.parseInt(lexeme)));
	// } else {
	// tokenStream
	// .add(TokenFactory.getToken(grammar.getSymbolByName("FLOAT"),
	// Double.parseDouble(lexeme)));
	// }
	// break;
	// }
	// }
	// }
	// // null as the end of stream marker;
	// tokenStream.add(null);
	// return tokenStream;
	// }

	public static List<Token> lex(String input) throws LexerException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, GrammarException {

		List<Token> tokens = new ArrayList<>();
		if (input.length() == 0) {
			return tokens;
		}
		int begin = 0;
		int end = 0;
		int oldEnd = 0;
		Pattern lexemePattern = null;

		Token token;
		input = input.replaceAll("\\p{Space}+", " ");
		SymbolList symbolList = grammar.getSymbolListClone();
		Matcher[] matcher = new Matcher[symbolList.size()];
		for (int i = 0; i < symbolList.size(); ++i) {
			if ((lexemePattern = symbolList.get(i).getLexPattern()) != null) {
				matcher[i] = lexemePattern.matcher(input);
			} else {
				matcher[i] = null;
			}
		}

		int targetIndex;

		while (true) {
			targetIndex = -1;
			begin = end + ((input.charAt(end) == ' ') ? 1 : 0);
			end = begin;

			if (begin == input.length()) {
				break;
			}

			for (int i = 0; i < matcher.length; ++i) {
				if (matcher[i] != null) {
					if (matcher[i].find(begin)) {
						if (matcher[i].start() == begin) {
							if (matcher[i].end() > end) {
								end = matcher[i].end();
								targetIndex = i;
							}
						}
					}
				}
			}

			if (targetIndex == -1) {
				throw new LexerException("At position " + begin + " of the input, lexer cannot match any token");
			}

			tokens.add(symbolList.get(targetIndex).getToken(input.substring(begin, end)));

		}

		// null as the end of input marker;

		tokens.add(null);

		return tokens;
	}

}
