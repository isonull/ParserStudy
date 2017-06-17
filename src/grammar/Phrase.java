package grammar;

import java.util.LinkedList;
import java.util.List;

import list.ListMethod;

public class Phrase {
	// The reference of coreSymbol must be contained in phrase.
	// All Symbol in the Phrase must be created from existed reference.
	// private final Symbol coreSymbol;
	private final SymbolList symbolList;
	private final boolean isTerminal;

	protected Phrase(SymbolList symbolList_) {
		// coreSymbol = coreSymbol_;
		symbolList = symbolList_;
		isTerminal = symbolList.isAllTerminal();
	}

	// protected Phrase(List<Symbol> symbolList_) {
	// symbolList = new SymbolList(symbolList_);
	// isTerminal = symbolList.isAllTerminal();
	// }

	public boolean isTerminal() {
		return isTerminal;
	}

	public int length() {
		return symbolList.size();
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return symbolList.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Phrase) {
			Phrase oPhrase = (Phrase) o;
			return symbolList.equals(oPhrase.symbolList);
		}
		return false;
	}

	public boolean containSymbol(Symbol symbol) {
		return symbolList.contains(symbol);
	}

	public boolean containSymbolsInSequence(SymbolList symbols) {
		return symbolList.containSymbolsInSequence(symbols);
	}

	public Phrase subPhrase(int base, int limit) {
		// include base, exclude limit.
		return new Phrase(new SymbolList(symbolList.subList(base, limit)));
	}

	public Symbol getSymbol(int index) {
		return symbolList.get(index);
	}

	public SymbolList getTerminalSymbols() {
		return symbolList.getTerminalSymbols();
	}

	public SymbolList getNonTerminalSymbols() {
		return symbolList.getNonTerminalSymbols();
	}

	public Phrase concatenate(Phrase phrase2) {
		SymbolList newSymbolList = (SymbolList) symbolList.clone();
		newSymbolList.addAll(phrase2.symbolList);
		return new Phrase(newSymbolList);
	}

	public LinkedList<PhraseList> partition(int part) throws GrammarException {
		ListMethod<Symbol> listMethod = new ListMethod<Symbol>();
		LinkedList<LinkedList<List<Symbol>>> symbolListListList = listMethod.partition(symbolList, part);

		LinkedList<PhraseList> castedSymbolListListList = new LinkedList<PhraseList>();
		PhraseList castedTempSymbolListList;
		for (LinkedList<List<Symbol>> symbolListList : symbolListListList) {
			castedTempSymbolListList = new PhraseList();
			for (List<Symbol> symbolList : symbolListList) {
				castedTempSymbolListList.add(new Phrase(new SymbolList(symbolList)));
			}
			castedSymbolListListList.add(castedTempSymbolListList);
		}
		return castedSymbolListListList;
	}

	// TODO: This method now just supports context-free grammar.
	public Phrase applyRule(Rule rule, int position) throws GrammarException {
		// assert (rule.getIn().length() == 1);
		Phrase ruleIn = rule.getIn();
		Phrase ruleOut = rule.getOut();
		int ruleInLength = ruleIn.length();
		if (!this.subPhrase(position, position + ruleInLength).equals(ruleIn)) {
			throw new GrammarException("the subphrase at the position dose not arree with the rule");
		}
		SymbolList result = (SymbolList) symbolList.clone();
		if (this.subPhrase(position, position + ruleInLength).equals(ruleIn)) {
			result.remove(position, position + ruleInLength);
			result.addAll(position, ruleOut.symbolList);
		}
		return new Phrase(result);
	}

	public int getFirstNonTerminalIndex() {
		if (this.isTerminal) {
			return -1;
		}
		for (int i = 0; i < this.length(); ++i) {
			if (!symbolList.get(i).isTerminal()) {
				return i;
			}
		}
		return -1; // this never happens;
	}

	public int getLastNonTerminalIndex() {
		if (this.isTerminal) {
			return -1;
		}
		for (int i = this.length() - 1; i > -1; --i) {
			if (!symbolList.get(i).isTerminal()) {
				return i;
			}
		}
		return -1; // this never happens;
	}

}
