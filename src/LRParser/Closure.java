package LRParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Rule;
import grammar.RuleList;
import grammar.Symbol;

public class Closure extends RuleList {

	private Map<Symbol, Closure> nextClosure = new HashMap<>();

	public Closure() {
		super();
	}

	public Closure(List<ItemRule> ruleList) {
		super();
		for (Rule rule : ruleList) {
			add(rule);
		}
	}

	public boolean add(Rule rule) {
		if (rule instanceof ItemRule) {
			super.add(rule);
		} else {
			try {
				throw new Exception("Closure only contains ItemRules");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public Closure getClosure(Grammar grammar) throws GrammarException {
		RuleList ruleList = grammar.getRuleListClone();
		boolean moreRule;
		Closure closure = (Closure) this.clone();
		ArrayList<Symbol> ins;
		Symbol s;
		do {
			moreRule = false;
			ins = new ArrayList<>();
			for (Rule r : closure) {
				s = ((ItemRule) r).progressSymbol();
				if (s != null) {
					ins.add(s);
				}
			}
			for (Symbol in : ins) {
				for (Rule r : ruleList.getAllRuleByIn(grammar.getPhrase(in))) {
					ItemRule newItemRule = new ItemRule(r);
					if (!closure.contains(newItemRule)) {
						closure.add(newItemRule);
						moreRule = true;
					}
				}
			}
		} while (moreRule);

		return closure;
	}

	public static Closure getStartClosure(Grammar grammar) throws GrammarException {
		Symbol startSymbol = grammar.getStartSymbol();
		RuleList ruleList = grammar.getRuleListClone();
		Closure closure = new Closure();
		for (Rule rule : ruleList) {
			if (rule.getIn().equals(grammar.getPhrase(grammar.getStartSymbol()))) {
				closure.add(new ItemRule(rule));
			}
		}
		return closure.getClosure(grammar);

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Closure) {
			Closure o_ = (Closure) o;
			if (this.size() != o_.size()) {
				return false;
			}
			for (Rule rt : this) {
				if (!o_.contains(rt)) {
					return false;
				}
			}
			for (Rule rt : o_) {
				if (!o_.contains(rt)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
