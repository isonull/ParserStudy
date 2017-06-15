package grammar;

public class Symbol {
	// The equality of Symbol is based on reference equality.
	private final boolean isTerminal;
	private final String name;

	protected Symbol(String symbol_, boolean isTerminal_) {
		name = symbol_;
		isTerminal = isTerminal_;
	}

	public boolean isTerminal() {
		return isTerminal;
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
		return name;
	}
}
