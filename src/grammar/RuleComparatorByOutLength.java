package grammar;

import java.util.Comparator;

public class RuleComparatorByOutLength implements Comparator<Rule> {

	@Override
	public int compare(Rule o1, Rule o2) {
		return o1.getOut().length() - o2.getOut().length();
	}

}
