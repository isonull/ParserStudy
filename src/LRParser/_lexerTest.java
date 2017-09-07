package LRParser;

import java.util.List;

import Token.Token;
import grammar.Grammar;
import grammar.GrammarFactory;

public class _lexerTest {

	public static void println(List<Token> tokenList) {
		for (Token token : tokenList) {
			System.out.print(token != null ? token.toString() + " " : "E_M");
		}
		System.out.println();
	}

	public static void main(String[] args) throws Exception {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = GrammarFactory.getGrammar(
				"/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/LRParser/SummerWorkTest3.txt",
				"/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/summerWorkParserMethods.jar", "test.LexerMethod",
				"test.RuleMethod");

		ParsingTable parsingTable = new ParsingTable(grammar);
		parsingTable.print();
		System.out.println("");
		Lexer lexer = new Lexer(grammar);
		List<Token> ts = lexer.lex("   12.2 *   ( 1.123 + cos ( cos   321.44 + 32!)  )    ");
		_lexerTest.println(ts);
	}
}
