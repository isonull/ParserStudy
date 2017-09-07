package grammar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import Token.Token;

public class Symbol {
	// The equality of Symbol is based on reference equality.
	private final String name;
	private final Pattern lexPattern;
	private final boolean isTerminal;
	private final boolean hasValue;

	private final Method generateToken;

	protected Symbol(String name_, boolean isTerminal_, boolean hasValue_, Pattern lexPattern_, Method generateToken_)
			throws GrammarException {
		if (name_.compareTo("") == 0) {
			throw new GrammarException("The name of a symbol cannot be empty string");
		}
		name = name_;
		isTerminal = isTerminal_;
		hasValue = hasValue_;
		lexPattern = lexPattern_;
		generateToken = generateToken_;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public boolean hasValue() {
		return hasValue;
	}

	public String getName() {
		return name;
	}

	public boolean isLegal(Grammar grammar) {
		return grammar.containSymbol(this);
	}

	public int nameCompareTo(String str) {
		return name.compareTo(str);
	}

	public void print() {
		System.out.print(this.toString());
	}

	@Override
	public String toString() {
		return name + (hasValue ? "*" : "");
	}

	public Token getToken(String str)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, GrammarException {
		if (generateToken != null) {
			return (Token) generateToken.invoke(null, this, str);
		} else {
			throw new GrammarException(this.toString() + " does not have a generateToken method");
		}
	}

	public Pattern getLexPattern() {
		return lexPattern;
	}
}
