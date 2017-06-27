package LRParser;

import grammar.Grammar;
import grammar.GrammarFactory;
import grammar.RuleList;
import grammar.Symbol;
import grammar.SymbolList;

public class AugGrammar extends Grammar {

	protected AugGrammar(byte languageType_, SymbolList nonTerminalSymbolList_, SymbolList terminalSymbolList_,
			Symbol startSymbol_, Symbol emptySymbol_, RuleList ruleList_, boolean isMonotonic_,
			boolean containEmptyRule_, GrammarFactory factory_) {
		super(languageType_, nonTerminalSymbolList_, terminalSymbolList_, startSymbol_, emptySymbol_, ruleList_,
				isMonotonic_, containEmptyRule_, factory_);
	}

	// public static Grammar getAugmentGrammar(Grammar g) {
	// Symbol newStart = new Symbol("S",true,true);
	// SymbolList newNonTerminalSymbolList = (SymbolList)
	// g.getNonTerminalSymbolListClone();
	// newNonTerminalSymbolList.add(newStart);
	// RuleList newRuleList = g.getRuleListClone();
	// newRuleList.add(e)
	//
	// return new
	// grammar(g.getLanguageType(),newNonTerminalSymbolList,g.getTerminalSymbolList().clone(),newStart,g.getEmptySymbol(),);
	// }

}
