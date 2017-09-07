package Token;

import grammar.Symbol;

public class NoValueToken extends Token {

	protected NoValueToken(Symbol symbol_) {
		super(symbol_);
	}

	@Override
	public boolean hasValue() {
		return false;
	}

}
