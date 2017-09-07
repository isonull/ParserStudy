package LRParser;

import java.util.ArrayList;
import java.util.List;

import Token.Token;
import Token.TokenFactory;
import Token.ValueToken;
import grammar.Rule;

public class ReduceAction implements Action {
	private final Rule rule;
	private final int index;
	// private final Method method;

	public ReduceAction(Rule rule_, int index_) {
		rule = rule_;
		index = index_;
	}

	public Rule getRule() {
		return rule;
	}

	public String toString() {
		return "r" + index;
	}

	public Token reduce(Token... tokens) throws Exception {
		List<Object> objs = new ArrayList<>();
		if (tokens.length != rule.outLength()) {
			throw new Exception("tokens Length is not matched");
		}
		for (int i = 0; i < tokens.length; ++i) {
			if (tokens[i].getSymbol().equals(rule.getOut().getSymbol(i))) {
				if (tokens[i].hasValue()) {
					objs.add(((ValueToken) tokens[i]).getValue());
				}
			} else {
				throw new Exception("tokens is not matched with rule out");
			}
		}

		return TokenFactory.getToken(rule.getIn().getSymbol(0), rule.getMethod().invoke(null, objs.toArray()));
	}
}
