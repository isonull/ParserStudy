package Token;

import grammar.Symbol;

public class IntegerToken extends ValueToken<Integer> {

	protected IntegerToken(Symbol symbol_, Integer value_) {
		super(symbol_, value_);
	}

	public Integer getValue() {
		return value;
	}

	public String toString() {
		return "" + value;
	}

}
