package parsar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Phrase;
import grammar.Rule;
import grammar.RuleList;
import grammar.RuleListComparatorByOutLength;

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

	private LinkedList<Rule> appliedRules;
	private LinkedList<Phrase> phraseList;
	private Grammar grammar;
	private RuleList ruleList;

	public UngersParsingMethod(Grammar grammar_) {
		appliedRules = new LinkedList<Rule>();
		phraseList = new LinkedList<Phrase>();
		grammar = grammar_;
		ruleList = grammar.getRuleListClone();
	}

	private boolean parse_AUX(Phrase phrase) throws GrammarException {
		Phrase ruleOut;
		int outLength = 0;
		boolean isValid = false;
		LinkedList<LinkedList<Phrase>> phrasePartitions = phrase.partition(1);
		phraseList.add(grammar.getPhrase(grammar.getStartSymbol()));
		Collections.sort(ruleList, new RuleListComparatorByOutLength());
		for (Rule rule : ruleList) {
			appliedRules.add(rule);
			ruleOut = rule.getOut();
			if (ruleOut.length() > outLength) {
				phrasePartitions = phrase.partition(outLength);
				outLength = ruleOut.length();
			}
			// deepening the tree to search for further
			outer: for (LinkedList<Phrase> partition : phrasePartitions) {
				if (worthDeepen(partition, rule)) {
					for (Phrase partPharse : partition) {
						if (!parse_AUX(partPharse)) {
							// one part parsing failed and try next partition;
							continue outer;
						}
					}
					return true; // one partition matches,done.
				}
			}
			// this rule fail, try next rule.

		}
		// all rule fail, no possible parsing
		return false;
	}

	private boolean worthDeepen(List<Phrase> partition, Rule rule) {
		// case not worthDeepen is too many terminate pattern
		// with non-terminal out
		// or too less or more terminal for terminal out

		return false;
	}

	private void initialise() {
		appliedRules = new LinkedList<Rule>();
		phraseList = new LinkedList<Phrase>();
	}

	@Override
	public LinkedList<Phrase> parse(Phrase phrase, Grammar grammar) {
		return null;
	}
}
