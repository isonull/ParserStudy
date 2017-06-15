package grammar;

import java.util.Comparator;

public class RuleListComparatorByOutLength implements Comparator<Rule> {

	@Override
	public int compare(Rule o1, Rule o2) {
		return o1.outLength() - o2.outLength();
	}

}
