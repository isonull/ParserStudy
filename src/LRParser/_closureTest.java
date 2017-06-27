package LRParser;

import grammar.Grammar;
import grammar.GrammarFactory;
import test.Test;

public class _closureTest extends Test {

	public static void main(String[] args) throws Exception {
		GrammarFactory grammarFactory = new GrammarFactory();
		Grammar grammar = grammarFactory
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/LRParser/ClosureTest.txt");
		grammar.print();

		System.out.println("");
		System.out.println("");
		System.out.println("------------------");
		ItemRule itemRule = new ItemRule(grammar.getRuleListClone().get(0));
		itemRule.println();
		Closure closure = itemRule.getClosure(grammar);
		closure.println();

		printLine();

		closure = Closure.getStartClosure(grammar);
		closure.println();

	}
}
