package parsar;

import java.io.IOException;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.GrammarFactory;

public class _CYKParsingMethodTest {
	public static void main(String[] args) throws GrammarException, IOException {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = grammarFactory
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/CKYParsingTestGrammar.txt");
		grammar.print();
	}

}
