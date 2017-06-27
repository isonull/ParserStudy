package LRParser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import grammar.Grammar;
import grammar.Token;

public class Lexer {

	private static final Pattern pattern = Pattern.compile("\\+|-|\\*|cos|\\!|\\(|\\)|(\\d+(.\\d+)?)");
	public static Grammar grammar;

	private static LinkedList<Token> tokenStream;

	public Lexer(Grammar grammar_) {
		grammar = grammar_;
	}

	public static LinkedList<Token> lex(String input) throws Exception {
		tokenStream = new LinkedList<Token>();

		int begin;
		int end;
		int oldEnd;
		String lexeme;
		Token token;
		input = input.replaceAll("\\p{Space}+", " ");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			oldEnd = 0;
			begin = matcher.start();
			end = matcher.end();
			if (oldEnd != 0 && begin != oldEnd + (input.charAt(oldEnd) == ' ' ? 1 : 0)) {
				throw new Exception("Unknown pattern found." + input.substring(0, begin) + " is discarded.");
			}
			lexeme = input.substring(begin, end);
			switch (lexeme) {
			case "+": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName("+"), null));
				break;
			}
			case "-": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName("_"), null));
				break;
			}
			case "*": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName("*"), null));
				break;
			}
			case "cos": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName("cos"), null));
				break;
			}
			case "!": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName("!"), null));
				break;
			}
			case "(": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName("("), null));
				break;
			}
			case ")": {
				tokenStream.add(Token.getToken(grammar.getSymbolByName(")"), null));
				break;
			}
			default: {
				if (!lexeme.contains(".")) {
					tokenStream.add(Token.getToken(grammar.getSymbolByName("INT"), Integer.parseInt(lexeme)));
				} else {
					tokenStream.add(Token.getToken(grammar.getSymbolByName("FLOAT"), Float.parseFloat(lexeme)));
				}
				break;
			}
			}
		}
		return tokenStream;
	}

}
