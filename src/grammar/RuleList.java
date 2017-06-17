package grammar;

import java.util.LinkedList;

public class RuleList extends LinkedList<Rule> {

	public RuleList() {
		super();
	}

	public PhraseList getAllOutByIn(Phrase in) {
		PhraseList outList = new PhraseList();
		for (Rule rule : this) {
			if (rule.inEqual(in)) {
				outList.add(rule.getOut());
			}
		}
		return outList;
	}

	public RuleList getAllRuleByIn(Phrase in) {
		RuleList ruleList = new RuleList();
		for (Rule rule : this) {
			if (rule.inEqual(in)) {
				ruleList.add(rule);
			}
		}
		return ruleList;
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
