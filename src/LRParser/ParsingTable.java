package LRParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grammar.Grammar;
import grammar.Phrase;
import grammar.Rule;
import grammar.RuleList;
import grammar.Symbol;
import grammar.SymbolList;

public class ParsingTable {
	// TODO: hashing is not override for Closure and Symbol, which is OK
	// because the generation of the closureSet guarantee the uniqueness
	// for each equality closure for closure i.e. equality => hashing equ
	// but not right thing to do

	// Treat null as the end of input symbol
	// TODO currently ignore the empty symbol
	// this should be done by grammar normalising process

	private final Map<Closure, Integer> closureMap = new HashMap<>();
	private final Map<Symbol, Integer> symbolMap = new HashMap<>();
	private final Map<Rule, Integer> ruleMap = new HashMap<>();
	private final ArrayList<Closure> closures = new ArrayList<>();
	private final ArrayList<Symbol> symbols = new ArrayList<>();
	private final ArrayList<Rule> rules = new ArrayList<>();
	private final Action[][] table;
	private final Grammar extendedGrammar;

	private final ItemRule finishItemRule;

	public ParsingTable(Grammar grammar) throws Exception {
		extendedGrammar = grammar.getExtendGrammar(grammar.getStartSymbol().getName() + "'");
		// TODO although the equality check make the start itemRule OK
		// but this is not a decent solution.
		finishItemRule = new ItemRule(extendedGrammar.getRule(0), 1);

		List<Closure> closureSet = Closure.getClosureSet(extendedGrammar);
		generateClosureMap(closureSet);
		generateSymbolMap(extendedGrammar);
		generateRuleMap(extendedGrammar);
		table = new Action[closureMap.size()][symbolMap.size()];
		SymbolList symbolList = extendedGrammar.getSymbolListClone();
		symbolList.add(null);

		RuleList ruleList = extendedGrammar.getRuleListClone();

		for (Closure closure : closureSet) {
			int closureIndex = closureMap.get(closure);
			for (Symbol symbol : symbolList) {
				int symbolIndex = symbolMap.get(symbol);

				/*
				 * Closure nextClosure;
				 * 
				 * int precedence = Integer.MIN_VALUE; List<ItemRule>
				 * shiftRules; ItemRule targetItemRule = null;
				 * 
				 * nextClosure = closure.getNextClosure(symbol);
				 * 
				 * boolean shiftPending = false;
				 * 
				 * if (nextClosure != null) { shiftPending = true; shiftRules =
				 * closure.getByNextSymbol(symbol); // There should only be one
				 * shift rule. for (ItemRule rule : shiftRules) { if
				 * (rule.getPrecedence() > precedence) { precedence =
				 * rule.getPrecedence(); targetItemRule = rule; } }
				 * table[closureIndex][symbolIndex] = new
				 * ShiftAction(nextClosure, closureMap.get(nextClosure)); }
				 * 
				 * for (ItemRule itemRule : closure.getDoneItemRules()) { if
				 * (extendedGrammar.follow(itemRule.getIn().getSymbol(0)).
				 * contains(symbol)) { // case A -> b and symbol in follow A if
				 * (itemRule.getPrecedence() > precedence) { precedence =
				 * itemRule.getPrecedence(); targetItemRule = itemRule;
				 * table[closureIndex][symbolIndex] = new
				 * ReduceAction(itemRule.getRule(),
				 * ruleMap.get(itemRule.getRule())); } else if
				 * (itemRule.getPrecedence() == precedence) { if (targetItemRule
				 * == null) { // this happens when the first ItemRule //
				 * satisfies the requirement has the Integer.MIN // as its
				 * precedence precedence = itemRule.getPrecedence();
				 * targetItemRule = itemRule; table[closureIndex][symbolIndex] =
				 * new ReduceAction(itemRule.getRule(),
				 * ruleMap.get(itemRule.getRule())); } else { if (shiftPending)
				 * { if (itemRule.getRule().equals(targetItemRule.getRule())) {
				 * if (itemRule.getRule().getAssociation() ==
				 * ItemRule.LEFT_ASSOCIATION) { shiftPending = false; precedence
				 * = itemRule.getPrecedence(); targetItemRule = itemRule;
				 * table[closureIndex][symbolIndex] = new
				 * ReduceAction(itemRule.getRule(),
				 * ruleMap.get(itemRule.getRule())); } } else { // undefined
				 * behaviour, unmatched // reduce, shift action have the same //
				 * precedence; } } else { // undefined behaviour, two reduce
				 * action // has the same precedence; } } } // else keep the
				 * origin action; } }
				 */
				// this version has difficulty to detect e.g. 1-1-1 = -1 | 1

				List<ItemRule> processingItemRule = closure.getByNextSymbol(symbol);
				List<ItemRule> finishedItemRule_ = closure.getDoneItemRules();
				List<ItemRule> finishedItemRule = new ArrayList<>();

				for (ItemRule rule : finishedItemRule_) {
					Phrase ruleIn = rule.getIn();
					if (extendedGrammar.follow(ruleIn.getSymbol(ruleIn.length() - 1)).contains(symbol)) {
						finishedItemRule.add(rule);
					}
				}

				List<ItemRule> refinedPIR = new ArrayList<>();
				int precedence = Integer.MIN_VALUE;

				for (ItemRule rule : processingItemRule) {
					if (rule.getPrecedence() > precedence) {
						refinedPIR = new ArrayList<>();
						precedence = rule.getPrecedence();
						refinedPIR.add(rule);
					} else if (rule.getPrecedence() == precedence) {
						precedence = rule.getPrecedence();
						refinedPIR.add(rule);
					} // else do nothing
				}
				int processingSize = refinedPIR.size();
				for (ItemRule rule : finishedItemRule) {
					if (rule.getPrecedence() > precedence) {
						refinedPIR = new ArrayList<>();
						processingSize = 0;
						precedence = rule.getPrecedence();
						refinedPIR.add(rule);
					} else if (rule.getPrecedence() == precedence) {
						precedence = rule.getPrecedence();
						refinedPIR.add(rule);
					} // else do nothing
				}
				// refinedPIR : processing | finished.
				// now refinedPIR contains ItemRules with the highest
				// precedence;
				ItemRule target;
				Closure nextClosure = closure.getNextClosure(symbol);
				if (refinedPIR.size() > 0) {
					if (refinedPIR.size() - processingSize > 1 || processingSize > 1) {
						System.out.println("WARNING: undefined beheviour from: \n" + closure.toString() + "\n"
								+ symbol.toString());
					}
					if (processingSize == 0) {
						// =0,>0
						target = refinedPIR.get(0);
						table[closureIndex][symbolIndex] = new ReduceAction(target.getRule(),
								ruleMap.get(target.getRule()));
					} else {
						if (refinedPIR.size() == processingSize) {
							// >0,=0
							target = refinedPIR.get(0);
							table[closureIndex][symbolIndex] = new ShiftAction(nextClosure,
									closureMap.get(nextClosure));
						} else {
							// >0,>0
							// the first finished itemrule
							target = refinedPIR.get(processingSize);
							if (target.getAssociation() == Rule.RIGHT_ASSOCIATION) {
								target = refinedPIR.get(processingSize);
								table[closureIndex][symbolIndex] = new ShiftAction(nextClosure,
										closureMap.get(nextClosure));
							} else { // left association in default
								table[closureIndex][symbolIndex] = new ReduceAction(target.getRule(),
										ruleMap.get(target.getRule()));
							}
						}
					}
				}

			}
			// TODO for null as the end of phrase marker;
			// this is pending, the when the stack only contains a start symbol
			// and the input stream is at the end, the accept is reached.

			if (closure.contains(finishItemRule)) {
				table[closureIndex][symbolMap.get(null)] = new AcceptAction();
			}

		}

	}

	public Action get(Closure closure, Symbol symbol) {
		// if (symbol == null) {
		// return table[closureMap.get(closure)][symbolMap.get(null)];
		// }
		return table[closureMap.get(closure)][symbolMap.get(symbol)];
	}

	public Action get(int closureIndex, int symbolIndex) {
		return table[closureIndex][symbolIndex];
	}

	public Action get(int closureIndex, Symbol symbol) {
		return table[closureIndex][symbolMap.get(symbol)];
	}

	private void generateClosureMap(List<Closure> closureSet) {
		for (int i = 0; i < closureSet.size(); ++i) {
			closureMap.put(closureSet.get(i), i);
			closures.add(closureSet.get(i));
		}

	}

	private void generateSymbolMap(Grammar grammar) {
		SymbolList temp = grammar.getTerminalSymbolListClone();
		int terminalSymbolListSize = temp.size();

		for (int i = 0; i < temp.size(); ++i) {
			symbolMap.put(temp.get(i), i);
			symbols.add(temp.get(i));
		}

		temp = grammar.getNonTerminalSymbolListClone();

		for (int i = 0; i < temp.size(); ++i) {
			symbolMap.put(temp.get(i), i + terminalSymbolListSize);
			symbols.add(temp.get(i));
		}

		symbolMap.put(null, grammar.sizeOfSymbolList());
		symbols.add(null);
	}

	private void generateRuleMap(Grammar grammar) {
		RuleList ruleList = grammar.getRuleListClone();
		for (int i = 0; i < ruleList.size(); ++i) {
			rules.add(ruleList.get(i));
			ruleMap.put(ruleList.get(i), i);
		}
	}

	public String toString() {
		// TODO lazy generation and store the result
		String str = "";
		int symbolMapSize = symbolMap.size();
		int closureMapSize = closureMap.size();
		Action action;

		str += "CLOSURE_MAP: \n";
		for (int i = 0; i < closures.size(); ++i) {
			str += i + "\n";
			str += closures.get(i).toString() + "\n";
			str += "---------------------- \n";
		}

		str += "SYMBOL_MAP: \n";

		for (int i = 0; i < symbols.size(); ++i) {
			Symbol symbol = symbols.get(i);
			str += i + " ";
			str += ((symbol == null) ? "null" : symbol.toString()) + " ";
		}

		str += "\n" + "---------------------- \n";
		str += "RULE_MAP: \n";
		for (int i = 0; i < rules.size(); ++i) {
			str += i + " " + rules.get(i).toString() + "\n";
		}
		str += "---------------------- \n";

		for (int i = 0; i < closureMapSize; ++i) {
			for (int j = 0; j < symbolMapSize; ++j) {
				action = table[i][j];
				str += " " + (action == null ? "  " : action.toString());
			}
			str += "\n";
		}
		return str;

	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}
}
