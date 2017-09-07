package LRParser;

import java.util.List;
import java.util.Stack;

import Token.Token;
import grammar.Grammar;
import grammar.Symbol;

public class Parser {

	private final ParsingTable parsingTable;
	private Stack<Token> input;
	private Stack<Token> output;
	private Stack<Integer> state;

	private String record;

	public Parser(Grammar grammar) throws Exception {
		parsingTable = new ParsingTable(grammar);
	}

	public Token parse(List<Token> tokens) throws Exception {
		// initialise states
		record = "";
		input = new Stack<>();
		for (int i = tokens.size() - 1; i > -1; --i) {
			input.push(tokens.get(i));
		}
		output = new Stack<>();
		state = new Stack<>();
		state.push(0);

		Action action;
		Token nextToken;
		Symbol nextSymbol;
		record += output.toString() + input.toString() + state.toString() + "\n";
		while (!input.isEmpty()) {
			nextToken = input.peek();
			nextSymbol = null;
			if (nextToken != null) {
				nextSymbol = nextToken.getSymbol();
			}
			action = parsingTable.get(state.peek(), nextSymbol);
			if (action instanceof ShiftAction) {
				ShiftAction shiftAction = (ShiftAction) action;
				state.push(shiftAction.index);
				output.push(input.pop());
			} else if (action instanceof ReduceAction) {
				ReduceAction reduceAction = (ReduceAction) action;
				Token[] reduceTokens = new Token[reduceAction.getRule().outLength()];
				for (int i = reduceAction.getRule().outLength() - 1; i > -1; --i) {
					reduceTokens[i] = output.pop();
					state.pop();
				}
				input.push(reduceAction.reduce(reduceTokens));
			} else if (action instanceof AcceptAction) {
				return output.peek();
			}

			record += output.toString() + input.toString() + state.toString() + "\n";
		}

		throw new Exception("syntax error");
	}

	public String getRecord() throws Exception {
		if (record == "") {
			throw new Exception("no parsing record");
		} else {
			return record;
		}
	}

}
