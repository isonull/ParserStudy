package grammar;

import java.util.HashMap;
import java.util.Map;

public class Grammar {
	// TODO languageType should be more detailed
	// TODO Only Grammar and Phrase should be exposed.
	// TODO All get methods should return a copy.
	// TODO other states to describe behaviours of the Grammar
	// TODO consider precedence and associativity;
	// TODO left-recursion elimination and e-rule elimination
	private final byte languageType;
	// private final boolean containEmptyRule;
	private final boolean containEmptyRule;
	private final boolean isMonotonic;
	@Deprecated
	private final SymbolList nonTerminalSymbolList;
	@Deprecated
	private final SymbolList terminalSymbolList;
	private final SymbolList symbolList;
	private final Symbol startSymbol;
	private final Symbol emptySymbol;
	private final RuleList ruleList;

	private boolean canDeriveEmptySymbolGenerated = false;
	private final SymbolList canDeriveEmptySymbols = new SymbolList();

	private boolean firstFollowMapGenerated = false;
	private final Map<Symbol, SymbolList> firstMap = new HashMap<>();
	private final Map<Symbol, SymbolList> followMap = new HashMap<>();

	protected Grammar(byte languageType_, SymbolList nonTerminalSymbolList_, SymbolList terminalSymbolList_,
			SymbolList symbolList_, Symbol startSymbol_, Symbol emptySymbol_, RuleList ruleList_, boolean isMonotonic_,
			boolean containEmptyRule_, GrammarFactory factory_) {
		languageType = languageType_;
		nonTerminalSymbolList = nonTerminalSymbolList_;
		terminalSymbolList = terminalSymbolList_;
		symbolList = symbolList_;
		startSymbol = startSymbol_;
		emptySymbol = emptySymbol_;
		ruleList = ruleList_;
		isMonotonic = isMonotonic_;
		containEmptyRule = containEmptyRule_;
	}

	public boolean containEmptyRule() {
		return containEmptyRule;
	}

	public Symbol getEmptySymbol() {
		return emptySymbol;
	}

	public boolean isMonotonic() {
		return isMonotonic;
	}

	public int sizeOfSymbolList() {
		return nonTerminalSymbolList.size() + terminalSymbolList.size();
	}

	public Phrase getPhrase(String str) throws GrammarException {
		// TODO this is actually a lexer, implement a symbolTable more detailed.
		return GrammarFactory.getPhrase(str, nonTerminalSymbolList, terminalSymbolList);
	}

	public Phrase getPhrase(Symbol symbol) throws GrammarException {
		if (this.containSymbol(symbol)) {
			SymbolList symbolList = new SymbolList();
			symbolList.add(symbol);
			return new Phrase(symbolList);
		} else {
			throw new GrammarException("The symbol is not legal under this garmmar");
		}
	}

	public Phrase getPhrase(SymbolList symbolList) {
		return new Phrase(symbolList);
	}

	public SymbolList getSymbolList(String str) throws GrammarException {
		return GrammarFactory.getSymbolList(str, nonTerminalSymbolList, terminalSymbolList);
	}

	public Rule getRule(int index) {
		return ruleList.get(index);
	}

	public boolean containSymbol(Symbol symbol) {
		return nonTerminalSymbolList.contains(symbol) || terminalSymbolList.contains(symbol);
	}

	public byte getLanguageType() {
		return languageType;
	}

	public SymbolList getNonTerminalSymbolListClone() {
		return (SymbolList) nonTerminalSymbolList.clone();
	}

	public SymbolList getTerminalSymbolListClone() {
		return (SymbolList) terminalSymbolList.clone();
	}

	public SymbolList getSymbolListClone() {
		return (SymbolList) symbolList.clone();
	}

	public Symbol getStartSymbol() {
		return startSymbol;
	}

	public Symbol getSymbolByName(String name) {
		// TODO: consider two symbol with difference reference but the same
		// name.
		Symbol symbol = symbolList.getSymbolByName(name);
		return symbol;
	}

	// This top-down approach may end up with loop.

	// public boolean canDeriveEmpty(Phrase phrase) throws GrammarException {
	// if (languageType != 2) {
	// throw new GrammarException("This function only works for CF grammar");
	// }
	//
	// // If the phrase is terminal and empty
	// if (phrase.isTerminal()) {
	// return phrase.onlyContainSymbol(emptySymbol);
	// }
	//
	// PhraseList outs = ruleList.getAllOutByIn(phrase);
	// Symbol symbol;
	// // If this derive empty directly by some rules
	// for (Phrase out : outs) {
	// canDeriveEmpty(out);
	// }
	//
	// // if this is the left of a set of productions
	// // if all rights contains at least one non-empty terminal
	// boolean allOutsContainTerminalandNonEmpty = true;
	// outer: for (Phrase out : outs) {
	// for (int i = 0; i < out.length(); ++i) {
	// symbol = out.getSymbol(i);
	// if (symbol.isTerminal() && !symbol.equals(emptySymbol)) {
	// continue outer;
	// }
	// }
	// allOutsContainTerminalandNonEmpty = false;
	// break;
	// }
	//
	// if (allOutsContainTerminalandNonEmpty) {
	// return false;
	// }
	//
	// for (int i = 0; i < phrase.length(); ++i) {
	// symbol = phrase.getSymbol(i);
	// if (!canDeriveEmpty(this.getPhrase(symbol))) {
	// return false;
	// }
	// }
	// return true;
	// }

	public boolean canDeriveEmpty(Symbol symbol) throws GrammarException {
		// This method just for CF grammar.
		if (languageType != 2) {
			throw new GrammarException("This function only works for CF grammar");
		}
		if (!canDeriveEmptySymbolGenerated) {
			generateCanDeriveEmptySymbols();
		}
		return canDeriveEmptySymbols.contains(symbol);

	}

	public boolean canDeriveEmpty(Phrase phrase) throws GrammarException {
		if (languageType != 2) {
			throw new GrammarException("This function only works for CF grammar");
		}
		if (!canDeriveEmptySymbolGenerated) {
			generateCanDeriveEmptySymbols();
		}
		for (int i = 0; i < phrase.length(); ++i) {
			if (!this.canDeriveEmpty(phrase.getSymbol(i))) {
				return false;
			}
		}
		return true;
	}

	public void generateCanDeriveEmptySymbols() {
		if (canDeriveEmptySymbolGenerated) {
			return;
		}
		canDeriveEmptySymbols.addUnique(emptySymbol);

		boolean more = false;

		do {
			more = false;
			for (Rule rule : ruleList) {
				if (rule.getOut().onlyContainSymbol(canDeriveEmptySymbols)) {
					if (rule.getIn().length() == 1) {
						more = canDeriveEmptySymbols.addUnique(rule.getIn().getSymbol(0));
					}
				}
			}
		} while (more);
	}

	public void generateFirstFollowMap() throws GrammarException {
		// Generate first map;
		for (Symbol symbol : terminalSymbolList) {
			SymbolList firstSymbols = new SymbolList();
			firstSymbols.add(symbol);
			firstMap.put(symbol, firstSymbols);
		}

		boolean more = false;

		do {
			more = false;
			for (Symbol symbol : nonTerminalSymbolList) {
				SymbolList firstSymbols;
				if ((firstSymbols = firstMap.get(symbol)) == null) {
					firstSymbols = new SymbolList();
				}
				PhraseList outs = ruleList.getAllOutByIn(this.getPhrase(symbol));
				SymbolList tempSymbolList;
				Symbol tempSymbol;
				for (Phrase out : outs) {
					// Find the first non-empty symbol
					tempSymbol = out.getSymbol(0);
					more = firstSymbols.addUnique(tempSymbol) ? true : more;
					if ((tempSymbolList = firstMap.get(tempSymbol)) != null) {
						more = firstSymbols.addAllUnique(tempSymbolList) ? true : more;
					}
					for (int i = 0; i < out.length() - 1; ++i) {
						tempSymbol = out.getSymbol(i);
						if (this.canDeriveEmpty(this.getPhrase(tempSymbol))) {
							more = firstSymbols.addUnique(out.getSymbol(i + 1)) ? true : more;
							if ((tempSymbolList = firstMap.get(out.getSymbol(i + 1))) != null) {
								more = firstSymbols.addAllUnique(tempSymbolList) ? true : more;
							}
						} else {
							break;
						}
					}
				}
				firstMap.put(symbol, firstSymbols);
			}
		} while (more);

		// generate follow map
		// we make null as the phrase end marker
		SymbolList tempSymbolList = new SymbolList();
		tempSymbolList.addUnique(null);
		followMap.put(this.getStartSymbol(), tempSymbolList);
		SymbolList allSymbols = this.getSymbolListClone();
		do {
			more = false;

			for (Symbol symbolKey : allSymbols) {
				SymbolList followSymbols;
				if ((followSymbols = followMap.get(symbolKey)) == null) {
					followSymbols = new SymbolList();
				}

				for (Rule rule : ruleList) {
					Phrase out = rule.getOut();
					Phrase in = rule.getIn();

					// case1 A->iBj first j in follow B
					for (int i = 0; i < out.length() - 1; ++i) {
						if (out.getSymbol(i).equals(symbolKey)) {
							// TODO emptySymbol can be added, remove later.
							more = followSymbols.addAllUnique(firstMap.get(out.getSymbol(i + 1))) ? true : more;

							// case2 A->iBj , j derive empty then follow A in
							// follow B;
							if (this.canDeriveEmpty(out.subPhrase(i + 1, out.length()))) {
								more = followSymbols.addAllUnique(followMap.get(in.getSymbol(in.length() - 1))) ? true
										: more;
							}
						}
					}

					// case2 A->iB follow A in follow B
					if (out.length() > 0) {
						if (out.getSymbol(out.length() - 1).equals(symbolKey)) {
							more = followSymbols.addAllUnique(followMap.get(rule.getIn().getSymbol(in.length() - 1)))
									? true : more;
						}
					}
					// case2 A->iBj , j derive empty follow A in follow B
					// (ABOVE);
				}

				followMap.put(symbolKey, followSymbols);
			}

		} while (more);

	}

	public SymbolList first(Symbol symbol) throws GrammarException {
		if (!firstFollowMapGenerated) {
			generateFirstFollowMap();
		}
		return firstMap.get(symbol);
	}

	public SymbolList follow(Symbol symbol) throws GrammarException {
		if (!firstFollowMapGenerated) {
			generateFirstFollowMap();
		}
		return followMap.get(symbol);
	}

	public RuleList getRuleListClone() {
		return (RuleList) ruleList.clone();
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return "GRAMMAR INFORMATION\n" + "Language type: " + languageType + "\n" + "Non-Terminal Symbols:\n"
				+ nonTerminalSymbolList.toString() + "\n" + "Terminal Symbols:\n" + terminalSymbolList.toString() + "\n"
				+ "Start Symbol:\n" + startSymbol.toString() + "\n" + "Rules:\n" + ruleList.toString();
	}

	public Grammar getExtendGrammar(String name) throws GrammarException {
		// This method is just for type 2 grammar
		// The new start rule is always at index 0;
		if (languageType != 2) {
			throw new GrammarException("extendGrammar method is just for type 2 grammar");
		}

		// TODO: this lexeme should be all possible derivation;
		Symbol newStartSymbol = new Symbol(name, false, startSymbol.hasValue(), null, null);

		SymbolList newNonTerminalSymbolList = (SymbolList) nonTerminalSymbolList.clone();
		newNonTerminalSymbolList.add(newStartSymbol);
		SymbolList newTerminalSymbolList = (SymbolList) terminalSymbolList.clone();
		SymbolList newSymbolList = (SymbolList) symbolList.clone();
		newSymbolList.add(newStartSymbol);
		RuleList newRuleList = (RuleList) ruleList.clone();
		// Be careful with the mutability
		// TODO Phrase can be cloned when constructor is caller
		SymbolList tempSymbolListIn = new SymbolList();
		tempSymbolListIn.add(newStartSymbol);
		Phrase in = new Phrase(tempSymbolListIn);
		SymbolList tempSymbolListOut = new SymbolList();
		tempSymbolListOut.add(startSymbol);
		Phrase out = new Phrase(tempSymbolListOut);
		newRuleList.addFirst(new Rule(in, out, Integer.MAX_VALUE, Rule.NO_ASSOCIATION, null));

		Grammar extendedGrammar = new Grammar(languageType, newNonTerminalSymbolList, newTerminalSymbolList,
				newSymbolList, newStartSymbol, emptySymbol, newRuleList, isMonotonic, containEmptyRule, null);

		return extendedGrammar;
	}
}
