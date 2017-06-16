package parsar;

import java.util.Collections;
import java.util.LinkedList;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Phrase;
import grammar.PhraseComparatorByLength;
import grammar.PhraseList;
import grammar.Rule;
import grammar.RuleList;

public class UngersParsingMethod implements ParsingMethod {
	// TODO: Phrase and grammar consistency check.

	// This is an algorithm for context-free (CF) monotonic language.
	// This is a top-down DFS search for space and time efficiency.

	// UNGER’S PARSING METHOD
	// Exprs -> Expr + Term | Term
	// Term -> Term × Factor | Factor
	// Factor -> ( Expr ) | i

	// ruleList and phraseList store which rules get applied and what phrases
	// get derived from that;

	// TODO: Deal with potential infinite loop because of DFS strategy

	private LinkedList<Rule> appliedRules;
	private LinkedList<Phrase> phraseList;
	private Grammar grammar;
	private RuleList ruleList;

	public UngersParsingMethod() {
	}

	// private boolean parse_AUX(Phrase phrase, Phrase startPhrase) throws
	// GrammarException {
	//
	// if ((phrase.containSymbol(startSymbol) && phrase.length() == 1)) {
	// return true;
	// }
	// Phrase ruleOut;
	// int outLength = 0;
	// boolean isValid = false;
	// LinkedList<LinkedList<Phrase>> phrasePartitions = phrase.partition(1);
	// phraseList.add(grammar.getPhrase(grammar.getStartSymbol()));
	// Collections.sort(ruleList, new RuleListComparatorByOutLength());
	// for (Rule rule : ruleList) {
	// appliedRules.add(rule);
	// ruleOut = rule.getOut();
	//
	// if (ruleOut.length() > outLength) {
	// phrasePartitions = phrase.partition(outLength);
	// outLength = ruleOut.length();
	// }
	// // deepening the tree to search for further
	// outer: for (LinkedList<Phrase> partition : phrasePartitions) {
	//
	// if (worthDeepen(partition, ruleOut)) {
	// for (Phrase partPharse : partition) {
	// if (!parse_AUX(partPharse)) {
	// // one part parsing failed and try next partition;
	// continue outer;
	// }
	// }
	// return true; // one partition matches,done.
	// }
	//
	// }
	// // this rule fail, try next rule.
	//
	// }
	// // all rule fail, no possible parsing
	// return false;
	// }

	private boolean parse_AUX(Phrase phrase, Phrase startPhrase) throws GrammarException {
		// This method may potentially hit the infinite loop
		// TODO: think about a BFS version then BDFS .
		if (phrase.equals(startPhrase)) {
			return true;
		}

		PhraseList outList = ruleList.getAllOutByIn(startPhrase);
		Collections.sort(outList, new PhraseComparatorByLength());
		int preOutLength = -1;
		LinkedList<LinkedList<Phrase>> allPartition = new LinkedList<LinkedList<Phrase>>();
		for (Phrase out : outList) {
			// lazy generate allPartition
			if (out.length() != preOutLength) {
				preOutLength = out.length();
				allPartition = phrase.partition(out.length());
			}
			// deepen the search
			if (worthDeepen(phrase, out)) {
				outer: for (LinkedList<Phrase> partition : allPartition) {
					// break down the phrase to smaller part
					for (int i = 0; i < out.length(); ++i) {
						// if the subPhrase fail to parse, try next partition
						if (!parse_AUX(partition.get(i), out.subPhrase(i, i + 1))) {
							continue outer;
						}
					}
					// All subPhrase parse successful
					return true;
				}
			}
			// if not worthDeepen try next rule
		}
		// all rule and partition pair fail, parse fails
		return false;
	}

	private boolean worthDeepen(Phrase phrase, Phrase ruleOut) {
		// case not worthDeepen:
		// too many terminate pattern by non-terminal out
		// too less or more terminal by terminal out
		return phrase.containSymbolsInSequence(ruleOut.getTerminalSymbols());
	}

	private void initialise(Grammar grammar_) {
		appliedRules = new LinkedList<Rule>();
		phraseList = new LinkedList<Phrase>();
		grammar = grammar_;
		ruleList = grammar.getRuleListClone();
	}

	@Override
	public LinkedList<Phrase> parse(Phrase phrase, Grammar grammar) throws GrammarException {
		initialise(grammar);
		parse_AUX(phrase, grammar.getPhrase(grammar.getStartSymbol()));
		return phraseList;
	}
}
