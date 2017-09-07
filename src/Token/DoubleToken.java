package Token;

import grammar.Symbol;

public class DoubleToken extends ValueToken<Double> {

	protected DoubleToken(Symbol symbol_, Double value_) {
		super(symbol_, value_);
	}

	public Double getValue() {
		return value;
	}

	public String toString() {
		return "" + value;
	}
}
