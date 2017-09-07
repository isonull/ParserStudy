package LRParser;

import java.util.List;

import Token.Token;
import grammar.Grammar;
import grammar.GrammarFactory;
import test.Test;

public class _parserTest extends Test {
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

		grammar.println();
		ParsingTable parsingTable = new ParsingTable(grammar);
		parsingTable.print();
		System.out.println("");
		Lexer lexer = new Lexer(grammar);
		String sampleInput = "-   12.2 *   ( 1.123 + cos (- -     - cos   321.44 + 3!)  )    ";
		System.out.println("This is sample input" + sampleInput);
		List<Token> ts = Lexer.lex(sampleInput);
		// ts = Lexer.lex(" 1 - 1 + ( 0 + 1.1 ) ! ");
		_lexerTest.println(ts);
		Parser parser = new Parser(grammar);
		System.out.println(parser.parse(ts));
		System.out.println(parser.getRecord());

		sampleInput = "- 1 ! ";
		System.out.println("This is sample input" + sampleInput);
		ts = Lexer.lex(sampleInput);
		// ts = Lexer.lex(" 1 - 1 + ( 0 + 1.1 ) ! ");
		_lexerTest.println(ts);
		System.out.println(parser.parse(ts));
		System.out.println(parser.getRecord());
	}
}
