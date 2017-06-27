package grammar;

public class Symbol {
	// The equality of Symbol is based on reference equality.
	private final String name;
	private final boolean isTerminal;
	private final boolean hasValue;

	protected Symbol(String name_, boolean isTerminal_, boolean hasValue_) throws GrammarException {
		if (name_.compareTo("") == 0) {
			throw new GrammarException("The name of a symbol cannot be empty string");
		}
		name = name_;
		isTerminal = isTerminal_;
		hasValue = hasValue_;
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
}
