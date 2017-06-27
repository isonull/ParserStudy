package LRParser;

import java.io.IOException;

import UngersCYKParsar.ContextFreeLanguageParser;
import UngersCYKParsar.Parser;
import UngersCYKParsar.UngersParsingMethod;
import grammar.Grammar;
import grammar.GrammarException;
import grammar.GrammarFactory;

public class _grammarTest {
	public static void main(String[] args) throws GrammarException, IOException {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = grammarFactory
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/SummerWorkTest2.txt");
		grammar.print();
		Parser parser = new ContextFreeLanguageParser(grammar, new UngersParsingMethod());
		parser.parse(grammar.getPhrase("- INT + cos ( - ( + cos FLOAT ! ) * cos INT )"));

	}
}
