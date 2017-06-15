package grammar;

public class Rule {
	private final Phrase in;
	private final Phrase out;

	protected Rule(Phrase in_, Phrase out_) {
		in = in_;
		out = out_;
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	public int inLength() {
		return in.length();
	}

	public int outLength() {
		return out.length();
	}

	public Phrase getOut() {
		return out;
	}

	public Phrase getIn() {
		return in;
	}

	@Override
	public String toString() {
		return in.toString() + " -> " + out.toString();
	}
}
