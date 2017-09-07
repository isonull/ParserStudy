package LRParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import grammar.Grammar;
import grammar.GrammarException;
import grammar.Rule;
import grammar.RuleList;
import grammar.Symbol;
import list.ListMethod;

public class Closure extends RuleList {

	// The closure in the parsing table should be singleton for each equality
	// closure. Such limitation is left for the class Parsing Table.

	private Map<Symbol, Closure> nextClosureMap = new HashMap<>();
	private boolean nextClosureMapGenerated = false;

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

	public Closure getNextClosure(Symbol symbol) throws Exception {
		if (!nextClosureMapGenerated) {
			throw new Exception("The next closure map has not generated yet.");
		}
		return nextClosureMap.get(symbol);
	}

	public ItemRule get(int index) {
		return (ItemRule) super.get(index);
	}

	public List<ItemRule> getByNextSymbol(Symbol symbol) {
		List<ItemRule> itemRules = new ArrayList<>();
		for (Rule itemRule : this) {
			ItemRule itemRule_ = (ItemRule) itemRule;
			if (!itemRule_.isDone()) {
				if (itemRule_.getOut().getSymbol(itemRule_.getProgress()).equals(symbol)) {
					itemRules.add(itemRule_);
				}
			}
		}
		return itemRules;
	}

	public List<ItemRule> getDoneItemRules() {
		List<ItemRule> ruleList = new LinkedList<>();
		ItemRule rule_;
		for (Rule rule : this) {
			rule_ = (ItemRule) rule;
			if (rule_.isDone()) {
				ruleList.add(rule_);
			}
		}
		return ruleList;
	}

	private void getClosure(Grammar grammar) throws GrammarException {
		RuleList ruleList = grammar.getRuleListClone();
		boolean moreRule;
		// Closure this_ = (Closure) this.clone();
		ArrayList<Symbol> ins;
		Symbol s;
		do {
			moreRule = false;
			ins = new ArrayList<>();
			for (Rule r : this) {
				s = ((ItemRule) r).progressSymbol();
				if (s != null) {
					ins.add(s);
				}
			}
			for (Symbol in : ins) {
				for (Rule r : ruleList.getAllRuleByIn(grammar.getPhrase(in))) {
					ItemRule newItemRule = new ItemRule(r);
					if (!this.contains(newItemRule)) {
						this.add(newItemRule);
						moreRule = true;
					}
				}
			}
		} while (moreRule);
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
		closure.getClosure(grammar);
		return closure;
	}

	public void generateNextClosureMap(Grammar grammar) throws GrammarException {
		if (nextClosureMapGenerated) {
			return;
		}
		LinkedList<Closure> closureList = new LinkedList<>();
		LinkedList<Symbol> nextSymbols = new LinkedList<>();
		for (Rule rule : this) {
			Symbol s;
			ItemRule rule_ = (ItemRule) rule;
			if ((s = rule_.progressSymbol()) != null) {
				nextSymbols.add(s);
			}
		}

		for (Symbol nextSymbol : nextSymbols) {
			Closure closure = new Closure();
			for (Rule rule : this) {
				ItemRule rule_ = (ItemRule) rule;
				ItemRule temp;
				if (nextSymbol.equals(rule_.progressSymbol())) {
					if ((temp = rule_.getNextProgress()) != null)
						closure.add(temp);
				}
			}
			closure.getClosure(grammar);
			nextClosureMap.put(nextSymbol, closure);
		}

		nextClosureMapGenerated = true;
	}

	public static List<Closure> getClosureSet(Grammar grammar) throws GrammarException {
		List<Closure> existedClosures = new LinkedList<>();
		Closure startClosure = getStartClosure(grammar);
		existedClosures.add(startClosure);
		boolean moreClosure;

		ListMethod<Closure> listMethod = new ListMethod<>();

		do {
			moreClosure = false;

			for (int i = 0; i < existedClosures.size(); ++i) {
				Closure existedClosure = existedClosures.get(i);
				if (existedClosure.nextClosureMapGenerated) {
					continue;
				}
				existedClosure.generateNextClosureMap(grammar);
				Set<Entry<Symbol, Closure>> entrySet = existedClosure.nextClosureMap.entrySet();
				for (Entry<Symbol, Closure> entry : entrySet) {
					Symbol key = entry.getKey();
					Closure value = entry.getValue();
					if (existedClosures.contains(value)) {
						// eliminate the duplication
						existedClosure.nextClosureMap.replace(key, listMethod.findEqualElement(existedClosures, value));
					} else {
						existedClosures.add(value);
					}
				}
				moreClosure = true;
			}

		} while (moreClosure);

		return existedClosures;
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
