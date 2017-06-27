package grammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarFactory {

	// TODO The validity check should be done.
	// TODO Singleton pattern for symbol to avoid name duplication.

	private static final Pattern ruleLinePattern = Pattern.compile("\\(\\d+,(LEFT|RIGHT|NO)\\)");

	private static final String splitStr = "\\p{Space}\\|\\p{Space}";

	protected static SymbolList getTerminalSymbolList(String str) throws GrammarException {
		String[] strPart = str.split(splitStr);
		String[] strSplit = strPart[0].split(" ");
		SymbolList symbolList = new SymbolList();
		for (String s : strSplit) {
			if (s.compareTo("") != 0) {
				symbolList.add(new Symbol(s, true, false));
			}
		}
		strSplit = strPart[1].split(" ");
		for (String s : strSplit) {
			if (s.compareTo("") != 0) {
				symbolList.add(new Symbol(s, true, true));
			}
		}
		return symbolList;
	}

	protected static SymbolList getNonTerminalSymbolList(String str) throws GrammarException {
		String[] strPart = str.split(splitStr);
		String[] strSplit = strPart[0].split(" ");
		SymbolList symbolList = new SymbolList();
		for (String s : strSplit) {
			if (s.compareTo("") != 0) {
				symbolList.add(new Symbol(s, false, false));
			}
		}
		strSplit = strPart[1].split(" ");
		for (String s : strSplit) {
			if (s.compareTo("") != 0) {
				symbolList.add(new Symbol(s, false, true));
			}
		}
		return symbolList;
	}

	protected static Phrase getPhrase(String inputStr, SymbolList nonTermialSymbolList, SymbolList terminalSymbolList)
			throws GrammarException {
		// The method does not check the consistency of the input reference
		// SymbolList e.g duplication of name. Also the reference of symbol is
		// taken from the list.

		return new Phrase(getSymbolList(inputStr, nonTermialSymbolList, terminalSymbolList));
	}

	protected static SymbolList getSymbolList(String inputStr, SymbolList nonTermialSymbolList,
			SymbolList terminalSymbolList) throws GrammarException {
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
		return phrase;
	}

	protected static PhraseList getRuleOuts(String inputStr, String splitStr, SymbolList nonTermialSymbolList,
			SymbolList terminalSymbolList) throws GrammarException {
		String ss = " " + splitStr + " ";
		String[] strSplit = inputStr.split(ss);
		PhraseList phraseList = new PhraseList();
		for (String split : strSplit) {
			phraseList.add(getPhrase(split, nonTermialSymbolList, terminalSymbolList));
		}
		return phraseList;
	}

	protected static RuleList getRuleLine(String str, String splitStr, SymbolList nonTermnialSymbolList,
			SymbolList terminalSymbolList) throws GrammarException {
		RuleList ruleList = new RuleList();
		int precedence;
		int association;
		Matcher matcher = ruleLinePattern.matcher(str);
		if (!matcher.find()) {
			throw new GrammarException("A rule line should begin with " + ruleLinePattern.toString());
		}

		String head = str.substring(matcher.start() + 1, matcher.end() - 1);
		String[] headSplit = head.split(",");
		precedence = Integer.parseInt(headSplit[0]);
		switch (headSplit[1]) {
		case "RIGHT": {
			association = Rule.RIGHT_ASSICIATION;
			break;
		}
		case "LEFT": {
			association = Rule.LEFT_ASSOCIATION;
			break;
		}
		case "NO": {
			association = Rule.NO_ASSOCIATION;
			break;
		}
		default: {
			throw new GrammarException("The headSplit[1] for a ruleLine should be LEFT | RIGHT | NO");
		}
		}

		String ruleStr = str.substring(matcher.end() + 1);
		String[] ruleStrSplit = ruleStr.split(" -> ");
		String[] outsStr = ruleStrSplit[1].split(splitStr);
		Phrase in = getPhrase(ruleStrSplit[0], nonTermnialSymbolList, terminalSymbolList);
		PhraseList outs = new PhraseList();
		for (String outStr : outsStr) {
			outs.add(getPhrase(outStr, nonTermnialSymbolList, terminalSymbolList));
		}
		for (Phrase out : outs) {
			ruleList.add(new Rule(in, out, precedence, association));
		}

		return ruleList;
	}

	// protected static Rule getRule(String inputStr, SymbolList
	// nonTermialSymbolList, SymbolList terminalSymbolList)
	// throws GrammarException {
	// // Now this method just support a one to one rule e.g. "a -> b"
	// String[] strSplit = inputStr.split(" -> ");
	// if (strSplit.length != 2) {
	// throw new GrammarException("The rule must contain one and only one
	// arrow");
	// }
	//
	// Phrase in = getPhrase(strSplit[0], nonTermialSymbolList,
	// terminalSymbolList);
	// Phrase out = getPhrase(strSplit[1], nonTermialSymbolList,
	// terminalSymbolList);
	// return new Rule(in, out);
	// }

	// protected static RuleList getCasesRule(String inputStr, String splitStr,
	// SymbolList nonTermialSymbolList,
	// SymbolList terminalSymbolList) throws GrammarException {
	// String[] strSplit = inputStr.split(" -> ");
	// if (strSplit.length != 2) {
	// throw new GrammarException("The rule must contain one and only one arrow
	// (->)");
	// }
	// RuleList ruleList = new RuleList();
	// Phrase in = getPhrase(strSplit[0], nonTermialSymbolList,
	// terminalSymbolList);
	// PhraseList outs = getRuleOuts(strSplit[1], splitStr,
	// nonTermialSymbolList, terminalSymbolList);
	// for (Phrase out : outs) {
	// ruleList.add(new Rule(in, out));
	// }
	// return ruleList;
	// }

	protected static boolean getIsMonotonic(RuleList ruleList, Symbol emptySymbol) {
		for (Rule rule : ruleList) {
			if (rule.getIn().length() > rule.getOut().length() - rule.getOut().containNumberOfSymbol(emptySymbol)) {
				return false;
			}
		}
		return true;
	}

	protected static boolean getContainEmptyRule(RuleList ruleList, Symbol emptySymbol) {
		// TODO this check is rough
		SymbolList symbolList = new SymbolList();
		symbolList.add(emptySymbol);
		Phrase emptyPhrase = new Phrase(symbolList);
		for (Rule rule : ruleList) {
			if (rule.getOut().equals(emptyPhrase)) {
				return true;
			}
		}
		return false;
	}

	public static Grammar getGrammar(String path) throws GrammarException, IOException {
		// TODO check name duplication

		ArrayList<String> strList = getGrammarStringList(getGrammarFile(path));
		byte languageType = (byte) Integer.parseInt(strList.get(0));
		String splitStr2 = strList.get(2);
		SymbolList nonTerminalSymbolList = getNonTerminalSymbolList(strList.get(3));
		SymbolList terminalSymbolList = getTerminalSymbolList(strList.get(4));
		Symbol emptySymbol = terminalSymbolList.getSymbolByName(strList.get(1));
		if (emptySymbol == null) {
			throw new GrammarException("The empty symnal should be in the terninal symbol list");
		}
		Symbol startSymbol = nonTerminalSymbolList.getSymbolByName(strList.get(5));
		RuleList ruleList = new RuleList();
		if (startSymbol == null) {
			startSymbol = terminalSymbolList.getSymbolByName(strList.get(5));
		}
		for (int i = 6; i < strList.size(); ++i) {
			ruleList.addAll(getRuleLine(strList.get(i), splitStr, nonTerminalSymbolList, terminalSymbolList));
		}
		return new Grammar(languageType, nonTerminalSymbolList, terminalSymbolList, startSymbol, emptySymbol, ruleList,
				getIsMonotonic(ruleList, emptySymbol), getContainEmptyRule(ruleList, emptySymbol), null);
	}

	protected static File getGrammarFile(String path) throws IOException {
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

	protected static ArrayList<String> getGrammarStringList(File file) throws IOException {
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
