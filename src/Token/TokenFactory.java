package Token;

import java.util.HashMap;
import java.util.Map;

import grammar.Symbol;

public class TokenFactory {
	private final static Map<Symbol, Token> noValueTokens = new HashMap<Symbol, Token>();

	public static Token getToken(Symbol symbol) throws Exception {
		Token token;
		if (!symbol.hasValue()) {
			if ((token = noValueTokens.get(symbol)) == null) {
				token = new NoValueToken(symbol);
				noValueTokens.put(symbol, token);
			}
		} else {
			throw new TokenException("Symbol " + symbol.toString() + " need no value to construct.");
		}
		return token;
	}

	public static IntegerToken getToken(Symbol symbol, Integer value) throws TokenException {
		if (symbol.hasValue()) {
			return new IntegerToken(symbol, value);
		} else {
			throw new TokenException("Symbol " + symbol.toString() + " need a value to construct.");
		}
	}

	public static DoubleToken getToken(Symbol symbol, Double value) throws TokenException {
		if (symbol.hasValue()) {
			return new DoubleToken(symbol, value);
		} else {
			throw new TokenException("Symbol " + symbol.toString() + " need a value to construct.");
		}
	}

	public static Token getToken(Symbol symbol, Object value) throws TokenException {
		if (symbol.hasValue()) {
			if (value instanceof Double) {
				return new DoubleToken(symbol, (Double) value);
			} else if (value instanceof Integer) {
				return new IntegerToken(symbol, (Integer) value);
			} else {
				throw new TokenException("No token for this value");
			}
		} else {
			throw new TokenException("Symbol " + symbol.toString() + " need a value to construct.");
		}
	}
}
