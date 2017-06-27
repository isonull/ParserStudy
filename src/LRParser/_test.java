package LRParser;

import java.util.List;

import grammar.Grammar;
import grammar.GrammarFactory;
import grammar.Token;

public class _test {
	public static void main(String[] args) throws Exception {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = grammarFactory
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/SummerWorkTest2.txt");
		grammar.print();
		System.out.println("");
		Lexer lexer = new Lexer(grammar);
		List<Token> ts = lexer.lex("   12.2 *   ( 1.123 + cos ( cos   321.44 + 32!)  )    ");
		Main.println(ts);
	}
}
