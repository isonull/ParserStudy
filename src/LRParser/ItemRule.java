package LRParser;

import java.util.ArrayList;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Rule;
import grammar.RuleList;
import grammar.Symbol;

public class ItemRule extends Rule {
	// e.g. progress 1 means A -> B·CD
	Rule rule;
	private int progress;

	protected ItemRule(Rule rule_) throws GrammarException {
		super(rule_.getIn(), rule_.getOut(), rule_.getPrecedence(), rule_.getAssociation());
		rule = rule_;
		progress = 0;
	}

	public void nextProgress() {
		++progress;
	}

	public Symbol progressSymbol() {
		if (progress < rule.outLength()) {
			return this.getOut().getSymbol(progress);
		} else {
			return null;
		}
	}

	public boolean isDone() {
		return progress == this.outLength();
	}

	public int getProgress() {
		return progress;
	}

	public Closure getClosure(Grammar grammar) throws GrammarException {
		RuleList ruleList = grammar.getRuleListClone();
		boolean moreRule;
		Closure closure = new Closure();
		closure.add(this);
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

	public boolean equals(Object o) {
		if (o instanceof ItemRule) {
			ItemRule o_ = (ItemRule) o;
			if (this.rule.equals(o_.rule) && this.progress == o_.progress) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String str = rule.getIn().toString() + " ->";
		for (int i = 0; i < rule.getOut().length(); ++i) {
			if (progress == i) {
				str += " ·";
			}
			str += " " + rule.getOut().getSymbol(i);
		}
		return str;
	}
}
