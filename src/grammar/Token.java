package grammar;

import java.util.HashMap;
import java.util.Map;

public class Token {

	private final Symbol symbol;
	private final Object value;
	private final static Map<Symbol, Token> noValueTokens = new HashMap<Symbol, Token>();

	private Token(Symbol symbol_, Object value_) throws GrammarException {
		symbol = symbol_;
		value = value_;
	}

	public static Token getToken(Symbol symbol, Object value) throws Exception {
		Token token;
		if (!symbol.hasValue()) {
			if (value != null) {
				throw new Exception("For a terminal syntax token, the value must be null.");
			}
			if ((token = noValueTokens.get(symbol)) == null) {
				token = new Token(symbol, null);
				noValueTokens.put(symbol, token);
			}
		} else {
			token = new Token(symbol, value);
		}
		return token;
	}

	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return symbol.toString();
		}
	}

}
