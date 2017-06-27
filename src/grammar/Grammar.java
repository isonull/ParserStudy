package grammar;

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

	private final SymbolList nonTerminalSymbolList;
	private final SymbolList terminalSymbolList;
	private final Symbol startSymbol;
	private final Symbol emptySymbol;
	private final RuleList ruleList;

	protected Grammar(byte languageType_, SymbolList nonTerminalSymbolList_, SymbolList terminalSymbolList_,
			Symbol startSymbol_, Symbol emptySymbol_, RuleList ruleList_, boolean isMonotonic_,
			boolean containEmptyRule_, GrammarFactory factory_) {
		languageType = languageType_;
		nonTerminalSymbolList = nonTerminalSymbolList_;
		terminalSymbolList = terminalSymbolList_;
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

	public Symbol getStartSymbol() {
		return startSymbol;
	}

	public Symbol getSymbolByName(String name) {

		// TODO: consider two symbol with difference reference but the same
		// name.
		Symbol symbol;
		if ((symbol = terminalSymbolList.getSymbolByName(name)) == null) {
			symbol = nonTerminalSymbolList.getSymbolByName(name);
		}
		return symbol;
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
}
