package parsar;

import java.util.LinkedList;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Phrase;
import grammar.PhraseList;
import grammar.Rule;
import grammar.RuleComparatorByOutLength;
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

	private RuleList appliedRules;
	private PhraseList parsingProcess;
	private Grammar grammar;
	private RuleList ruleList;

	private int level;
	private LinkedList<Integer> pending;

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

	// TODO consider methods to reconstruct the parsing process:
	// DFS will be cheaper than BFS in space
	// note another approach for reconstruction is use index to represent
	// partition, cheaper cost, but need rewrite lots of things.

	// private boolean parse_AUX(Phrase phrase, Phrase startPhrase) throws
	// GrammarException {
	// // This method may potentially hit the infinite loop
	// // for strict monotonic, solve by phrase length compare
	// // TODO: for non-strict monotonic, need a solution.
	// // TODO: think about a BFS version then BDFS .
	// if (phrase.equals(startPhrase)) {
	// return true;
	// }
	//
	// PhraseList outList = ruleList.getAllOutByIn(startPhrase);
	// outList.sort(new PhraseComparatorByLength());
	// int preOutLength = -1;
	// LinkedList<PhraseList> allPartition = new LinkedList<PhraseList>();
	// for (Phrase out : outList) {
	// // lazy generate allPartition
	// if (out.length() != preOutLength) {
	// preOutLength = out.length();
	// allPartition = phrase.partition(out.length());
	// }
	// // deepen the search
	// if (worthDeepen(phrase, out)) {
	// outer: for (PhraseList partition : allPartition) {
	// // break down the phrase to smaller part
	// for (int i = 0; i < out.length(); ++i) {
	// // if the subPhrase fail to parse, try next partition
	// if (!parse_AUX(partition.get(i), out.subPhrase(i, i + 1))) {
	// continue outer;
	// }
	// }
	// // All subPhrase parse successful
	// return true;
	// }
	// }
	// // if not worthDeepen try next rule
	// }
	// // all rule and partition pair fail, parse fails
	// return false;
	// }

	private boolean parse_AUX(Phrase phrase, Phrase startPhrase) throws GrammarException {
		// This method may potentially hit the infinite loop
		// for strict monotonic, solve by phrase length compare
		// TODO: for non-strict monotonic, need a solution.
		// TODO: think about a BFS version then BDFS .
		// TODO: implement dynamic programming for duplicated subproblem
		++level;
		if (pending.size() < level + 1) {
			pending.add(0);
		}

		if (phrase.equals(startPhrase)) {
			--level;
			return true;
		}

		RuleList possibleRules = ruleList.getAllRuleByIn(startPhrase);
		possibleRules.sort(new RuleComparatorByOutLength());
		int preOutLength = -1;
		LinkedList<PhraseList> allPartition = new LinkedList<PhraseList>();
		for (Rule rule : possibleRules) {
			// lazy generate allPartition
			Phrase out = rule.getOut();
			if (out.length() != preOutLength) {
				preOutLength = out.length();
				allPartition = phrase.partition(out.length());
			}
			// deepen the search
			if (worthDeepen(phrase, out)) {
				outer: for (PhraseList partition : allPartition) {
					// break down the phrase to smaller part
					for (int i = 0; i < out.length(); ++i) {
						// if the subPhrase fail to parse, try next partition
						if (!parse_AUX(partition.get(i), out.subPhrase(i, i + 1))) {

							// remove all the pending rules because one of the
							// same level parsing failed
							for (int j = 0; j < pending.get(level + 1); ++j) {
								appliedRules.removeFirst();
							}
							pending.set(level + 1, 0);
							continue outer;
						}
					}
					// All subPhrase parse successful
					// rule.println();
					appliedRules.addFirst(rule);

					// even if the deeper call is successful the added Rule
					// should still be pending until the shallower call is
					// successful .
					pending.set(level,
							pending.get(level) + 1 + (pending.size() > level + 1 ? pending.get(level + 1) : 0));
					pending.set(level + 1, 0);
					--level;
					return true;
				}
			}
			// if not worthDeepen try next rule
		}
		// all rule and partition pair fail, parse fails

		// next level dose not necessarily exists
		if (pending.size() > level + 1) {
			pending.set(level + 1, 0);
		}
		--level;

		return false;
	}

	private boolean worthDeepen(Phrase phrase, Phrase ruleOut) {
		// case not worthDeepen:
		// too many terminate pattern by non-terminal out
		// too less or more terminal by terminal out
		return phrase.containSymbolsInSequence(ruleOut.getTerminalSymbols());
	}

	private void initialise(Grammar grammar_) {
		appliedRules = new RuleList();
		parsingProcess = new PhraseList();
		grammar = grammar_;
		ruleList = grammar.getRuleListClone();
		level = -1;
		pending = new LinkedList<Integer>();
	}

	private void ConstructParsingProcess() throws GrammarException {
		// by considering the tree of calls
		parsingProcess.add(grammar.getPhrase(grammar.getStartSymbol()));
		int lastNonTerminalSymbolIndex;
		Phrase phrase;
		Rule rule;
		for (int i = 0; i < appliedRules.size(); ++i) {
			phrase = parsingProcess.get(i);
			rule = appliedRules.get(i);
			lastNonTerminalSymbolIndex = phrase.getLastNonTerminalIndex();
			parsingProcess.add(phrase.applyRule(rule, lastNonTerminalSymbolIndex));
		}
	}

	@Override
	public PhraseList parse(Phrase phrase, Grammar grammar) throws GrammarException {
		initialise(grammar);
		boolean isMatch = parse_AUX(phrase, grammar.getPhrase(grammar.getStartSymbol()));
		System.out.println("matched? " + isMatch);
		appliedRules.println();
		ConstructParsingProcess();
		parsingProcess.println();
		return parsingProcess;
	}

	public PhraseList getParsingProcess() {
		return parsingProcess;
	}

	public RuleList getAppliedRules() {
		return appliedRules;
	}
}
