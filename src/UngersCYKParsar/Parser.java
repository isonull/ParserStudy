package UngersCYKParsar;

import java.util.LinkedList;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Phrase;

public abstract class Parser {
	// TODO check whether the parsingMethod is capable for grammar.

	protected ParsingMethod parsingMethod;
	protected Grammar grammar;

	public Parser(Grammar grammar_, ParsingMethod parsingMethod_) {
		grammar = grammar_;
		parsingMethod = parsingMethod_;
	}

	public void setParsingMethod(ParsingMethod parsingMethod_) {
		parsingMethod = parsingMethod_;
	}

	public void setGrammar(Grammar grammar_) {
		grammar = grammar_;
	}

	public LinkedList<Phrase> parse(Phrase phrase) throws GrammarException {
		return parsingMethod.parse(phrase, grammar);
	}
}
