package grammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrammarFactory {

	// TODO The validity check should be done.
	// TODO Singleton pattern for symbol to avoid name duplication.

	protected SymbolList getTerminalSymbolList(String str) {
		String[] strSplit = str.split(" ");
		SymbolList symbolList = new SymbolList();
		for (String s : strSplit) {
			symbolList.add(new Symbol(s, true));
		}
		return symbolList;
	}

	protected SymbolList getNonTerminalSymbolList(String str) {
		String[] strSplit = str.split(" ");
		SymbolList symbolList = new SymbolList();
		for (String s : strSplit) {
			symbolList.add(new Symbol(s, false));
		}
		return symbolList;
	}

	protected Phrase getPhrase(String inputStr, SymbolList nonTermialSymbolList, SymbolList terminalSymbolList)
			throws GrammarException {
		// The method does not check the consistency of the input reference
		// SymbolList e.g duplication of name. Also the reference of symbol is
		// taken from the list.
		String[] strSplit = inputStr.split(" ");
		Symbol tempSymbol = null;
		SymbolList phrase = new SymbolList();
		for (String s : strSplit) {
			if ((tempSymbol = nonTermialSymbolList.getSymbolByName(s)) != null) {
				phrase.add(tempSymbol);
			} else if ((tempSymbol = terminalSymbolList.getSymbolByName(s)) != null) {
				phrase.add(tempSymbol);
			} else {
				throw new GrammarException(
						"Symbol name " + s + " in the string cannot be found in the ref SymbolList.");
			}
		}
		return new Phrase(phrase);
	}

	protected Phrase getPhrase(List<Symbol> symbolList, SymbolList nonTermialSymbolList, SymbolList terminalSymbolList)
			throws GrammarException {
		return new Phrase(new SymbolList(symbolList));
	}

	protected Rule getRule(String inputStr, SymbolList nonTermialSymbolList, SymbolList terminalSymbolList)
			throws GrammarException {
		// Now this method just support a one to one rule e.g. "a -> b"
		String[] strSplit = inputStr.split(" -> ");
		if (strSplit.length != 2) {
			throw new GrammarException("The rule must contain one and only one arrow");
		}

		Phrase in = getPhrase(strSplit[0], nonTermialSymbolList, terminalSymbolList);
		Phrase out = getPhrase(strSplit[1], nonTermialSymbolList, terminalSymbolList);
		return new Rule(in, out);
	}

	public Grammar getGrammar(String path) throws GrammarException, IOException {
		// TODO check name duplication

		ArrayList<String> strList = getGrammarStringList(getGrammarFile(path));
		byte languageType = (byte) Integer.parseInt(strList.get(0));
		SymbolList nonTerminalSymbolList = getNonTerminalSymbolList(strList.get(1));
		SymbolList terminalSymbolList = getTerminalSymbolList(strList.get(2));
		Symbol startSymbol = nonTerminalSymbolList.getSymbolByName(strList.get(3));
		RuleList ruleList = new RuleList();
		if (startSymbol == null) {
			startSymbol = terminalSymbolList.getSymbolByName(strList.get(3));
		}
		for (int i = 4; i < strList.size(); ++i) {
			ruleList.add(getRule(strList.get(i), nonTerminalSymbolList, terminalSymbolList));
		}
		return new Grammar(languageType, nonTerminalSymbolList, terminalSymbolList, startSymbol, ruleList, this);
	}

	protected File getGrammarFile(String path) throws IOException {
		// TODO format check
		File file = new File(path);
		if (!file.exists()) {
			throw new IOException("The file by given path does not exist.");
		} else if (!file.canRead()) {
			throw new IOException("The file by given path is not readable.");
		} else if (!file.isFile()) {
			throw new IOException("The file by given path is not a file but a Directory.");
		}
		return file;
	}

	protected ArrayList<String> getGrammarStringList(File file) throws IOException {
		BufferedReader fileBufferedReader = new BufferedReader(new FileReader(file));
		ArrayList<String> strList = new ArrayList<String>();
		String str;
		while ((str = fileBufferedReader.readLine()) != null) {
			strList.add(str);
		}
		fileBufferedReader.close();
		return strList;
	}
}
