package parsar;

import java.util.LinkedList;

import grammar.Grammar;
import grammar.Phrase;

public interface ParsingMethod {
	public LinkedList<Phrase> parse(Phrase phrase, Grammar grammar);
}
