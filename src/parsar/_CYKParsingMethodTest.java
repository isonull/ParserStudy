package parsar;

import java.io.IOException;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.GrammarFactory;
import grammar.Phrase;

public class _CYKParsingMethodTest {
	public static void main(String[] args) throws GrammarException, IOException {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = grammarFactory
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/CKYParsingTestGrammar.txt");
		Phrase phrase = grammar.getPhrase("( i + i ) * i");
		Parser parser = new ContextFreeLanguageParser(grammar, new UngersParsingMethod());

	}

}
