package grammar;

import java.io.IOException;
import java.util.LinkedList;

public class _GrammarTest {
	public static void main(String[] args) throws GrammarException, IOException {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = grammarFactory
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/UngerParsingTestGrammar.txt");
		Phrase phrase = grammar.getPhrase("( i + i ) * i");

		RuleList ruleList = grammar.getRuleListClone();

		phrase.print();

		System.out.println();

		System.out.println(grammar.toString());

		LinkedList<PhraseList> phrasePartition = phrase.partition(3);

		for (LinkedList<Phrase> phraseList : phrasePartition) {
			for (Phrase phrase1 : phraseList) {
				System.out.print(phrase1.toString() + "   ");
			}
			System.out.println();
		}

		phrase = grammar.getPhrase("Expr");
		phrase.println();
		ruleList.get(0).println();
		phrase = phrase.applyRule(ruleList.get(0), 0);
		phrase.println();
		phrase = phrase.applyRule(ruleList.get(0), 0);
		phrase.println();
		phrase = phrase.applyRule(ruleList.get(2), 2);
		phrase.println();
		System.out.println(phrase.containSymbolsInSequence(grammar.getSymbolList("+ + *")));
		System.out.println(phrase.containSymbolsInSequence(grammar.getSymbolList("+ * +")));

		LinkedList<Phrase> outList = ruleList.getAllOutByIn(grammar.getPhrase("Expr"));
		for (Phrase phr : outList) {
			phr.println();
		}
	}
}
