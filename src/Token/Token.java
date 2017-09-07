package Token;

import grammar.Symbol;

public abstract class Token {

	private final Symbol symbol;

	protected Token(Symbol symbol_) {
		symbol = symbol_;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public String toString() {
		return symbol.toString();
	}

	public abstract boolean hasValue();

}
