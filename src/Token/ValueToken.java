package Token;

import grammar.Symbol;

public abstract class ValueToken<T> extends Token {

	protected T value;

	protected ValueToken(Symbol symbol_, T value_) {
		super(symbol_);
		value = value_;
	}

	public abstract T getValue();

	@Override
	public boolean hasValue() {
		return true;
	}

}
