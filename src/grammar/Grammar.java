package grammar;

public class Grammar {
	// TODO languageType should be more detailed
	// TODO Only Grammar and Phrase should be exposed.
	// TODO All get methods should return a copy.
	// TODO other states to describe behaviours of the Grammar
	private final byte languageType;
	// private final boolean containEmptyRule;

	private final SymbolList nonTerminalSymbolList;
	private final SymbolList terminalSymbolList;
	private final Symbol startSymbol;
	private final RuleList ruleList;
	private final GrammarFactory factory;

	protected Grammar(byte languageType_, SymbolList nonTerminalSymbolList_, SymbolList terminalSymbolList_,
			Symbol startSymbol_, RuleList ruleList_, GrammarFactory factory_) {
		languageType = languageType_;
		nonTerminalSymbolList = nonTerminalSymbolList_;
		terminalSymbolList = terminalSymbolList_;
		startSymbol = startSymbol_;
		ruleList = ruleList_;
		factory = factory_;
	}

	public Phrase getPhrase(String str) throws GrammarException {
		return factory.getPhrase(str, nonTerminalSymbolList, terminalSymbolList);
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
		return factory.getSymbolList(str, nonTerminalSymbolList, terminalSymbolList);
	}

	public boolean containSymbol(Symbol symbol) {
		return nonTerminalSymbolList.contains(symbol) || terminalSymbolList.contains(symbol);
	}

	public byte getLanguageType() {
		return languageType;
	}

	public SymbolList getNonTerminalSymbolList() {
		return nonTerminalSymbolList;
	}

	public SymbolList getTerminalSymbolList() {
		return terminalSymbolList;
	}

	public Symbol getStartSymbol() {
		return startSymbol;
	}

	public RuleList getRuleListClone() {
		return (RuleList) ruleList.clone();
	}

	public void print() {
		System.out.print(this.toString());
	}

	@Override
	public String toString() {
		return "GRAMMAR INFORMATION\n" + "Language type: " + languageType + "\n" + "Non-Terminal Symbols:\n"
				+ nonTerminalSymbolList.toString() + "\n" + "Terminal Symbols:\n" + terminalSymbolList.toString() + "\n"
				+ "Start Symbol:\n" + startSymbol.toString() + "\n" + "Rules:\n" + ruleList.toString();
	}
}
