package grammar;

import java.io.IOException;
import java.util.ArrayList;

public class _GrammarFactoryTest {
	public static void main(String[] args) throws IOException, GrammarException {
		GrammarFactory g = new GrammarFactory();
		ArrayList<String> strList = g.getGrammarStringList(
				g.getGrammarFile("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/UngerParsingTestGrammar.txt"));
		for (String s : strList) {
			System.out.println(s);
		}
		Grammar grammar = g
				.getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/UngerParsingTestGrammar.txt");
		grammar.print();
		System.out.println();
		System.out.print(grammar.toString());
	}
}
