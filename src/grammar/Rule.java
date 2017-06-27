package grammar;

public class Rule {
	public static final int NO_ASSOCIATION = 0;
	public static final int LEFT_ASSOCIATION = 1;
	public static final int RIGHT_ASSICIATION = 2;

	private final Phrase in;
	private final Phrase out;
	private final int precedence;
	private final int association;

	protected Rule(Phrase in_, Phrase out_, int precedence_, int association_) throws GrammarException {
		if (!(association_ == NO_ASSOCIATION || association_ == LEFT_ASSOCIATION
				|| association_ == RIGHT_ASSICIATION)) {
			throw new GrammarException("The association is not correctly setted.");
		}
		in = in_;
		out = out_;
		precedence = precedence_;
		association = association_;
	}

	public int inLength() {
		return in.length();
	}

	public int outLength() {
		return out.length();
	}

	public Phrase getIn() {
		return in;
	}

	public Phrase getOut() {
		return out;
	}

	public int getPrecedence() {
		return precedence;
	}

	public int getAssociation() {
		return association;
	}

	@Override
	public String toString() {
		return "(" + precedence + "," + association + ") " + in.toString() + " -> " + out.toString();
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	public boolean equals(Object o) {
		if (o instanceof Rule) {
			Rule o_ = (Rule) o;
			if (this.in.equals(o_.in) && this.out.equals(o_.out)) {
				return true;
			}
		}
		return false;
	}
}
