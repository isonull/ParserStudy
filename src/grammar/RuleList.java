package grammar;

import java.util.LinkedList;

public class RuleList extends LinkedList<Rule> {

	protected RuleList() {
		super();
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < this.size() - 1; ++i) {
			str += this.get(i).toString() + "\n";
		}
		str += this.get(this.size() - 1).toString();
		return str;
	}
}
