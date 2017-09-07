package grammar;

import java.io.IOException;

public class _GrammarFactoryTest {
	public static void main(String[] args) throws IOException, GrammarException, ClassNotFoundException {
		GrammarFactory g = new GrammarFactory();
		// ArrayList<String> strList = g.getGrammarStringList(
		// g.getGrammarFile("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/UngerParsingTestGrammar.txt"));
		// for (String s : strList) {
		// System.out.println(s);
		// }
		Grammar grammar = GrammarFactory.getGrammar(
				"/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/LRParser/SummerWorkTest3.txt",
				"/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/summerWorkParserMethods.jar", "test.LexerMethod",
				"test.RuleMethod");
		grammar.print();
		System.out.println();
		System.out.print(grammar.toString());
	}
}
