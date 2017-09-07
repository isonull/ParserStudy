package grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SymbolList extends ArrayList<Symbol> {

	// Decorate new functions for the ArrayList<Symbol>
	// null is treated as the phrase end marker

	protected SymbolList() {
		super();
	}

	protected SymbolList(List<Symbol> symbols) {
		super(symbols);
	}

	public boolean addUnique(Symbol symbol) {
		if (!this.contains(symbol)) {
			add(symbol);
			return true;
		}
		return false;
	}

	public boolean addAllUnique(Collection<Symbol> symbols) {
		boolean added = false;
		if (symbols != null) {
			for (Symbol symbol : symbols) {
				if (this.addUnique(symbol)) {
					added = true;
				}
			}
		}
		return added;
	}

	public boolean containSymbolName(String name) {
		for (Symbol symbol : this) {
			if (symbol.nameCompareTo(name) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean containSymbolAfterIndex(Symbol symbol, int index) {
		// index is included
		for (int i = index; i < this.size(); ++i) {
			if (this.get(i).equals(symbol)) {
				return true;
			}
		}
		return false;
	}

	public boolean containSymbolsInSequence(SymbolList symbolList) {
		Iterator<Symbol> iterator = symbolList.iterator();
		Symbol s2;
		if (iterator.hasNext()) {
			s2 = iterator.next();
		} else {
			return true;
		}
		for (Symbol s1 : this) {
			if (s1.equals(s2)) {
				if (iterator.hasNext()) {
					s2 = iterator.next();
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public Symbol getSymbolByName(String name) {
		for (Symbol symbol : this) {
			if (symbol.nameCompareTo(name) == 0) {
				return symbol;
			}
		}
		return null;
	}

	public SymbolList getTerminalSymbols() {
		// With duplication.
		SymbolList symbolList = new SymbolList();
		for (Symbol symbol : this) {
			if (symbol.isTerminal()) {
				symbolList.add(symbol);
			}
		}
		return symbolList;
	}

	public SymbolList getNonTerminalSymbols() {
		SymbolList symbolList = new SymbolList();
		for (Symbol symbol : this) {
			if (!symbol.isTerminal()) {
				symbolList.add(symbol);
			}
		}
		return symbolList;
	}

	public boolean isAllTerminal() {
		for (Symbol symbol : this) {
			if (!symbol.isTerminal()) {
				return false;
			}
		}
		return true;
	}

	public int containNumberOfSymbol(Symbol symbol) {
		int i = 0;
		for (Symbol s : this) {
			if (s.equals(symbol)) {
				++i;
			}
		}
		return i;
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < this.size() - 1; ++i) {
			if (this.get(i) == null) {
				str += "null ";
			} else {
				str += this.get(i).toString() + " ";
			}
		}
		str += this.get(this.size() - 1).toString();
		return str;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SymbolList) {
			SymbolList oSymbolList = (SymbolList) o;
			if (this.size() == oSymbolList.size()) {
				for (int i = 0; i < this.size(); ++i) {
					if (!this.get(i).equals(oSymbolList.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public void remove(int base, int limit) {
		for (int i = 0; i < limit - base; ++i) {
			this.remove(base);
		}
	}

	public boolean onlyContain(Symbol symbol) {
		// symbol in the list and only symbol in the list
		boolean atLeastOne = false;
		for (Symbol s : this) {
			if (!s.equals(symbol)) {
				return false;
			} else {
				atLeastOne = true;
			}
		}
		return atLeastOne;
	}

	public boolean onlyContain(SymbolList symbols) {
		for (Symbol symbol : this) {
			if (!symbols.contains(symbol)) {
				return false;
			}
		}
		return true;
	}
}
