// package LRParser;
//
// import java.util.List;
//
// import grammar.Grammar;
// import grammar.GrammarFactory;
// import test.Test;
//
// public class _closureTest extends Test {
//
// public static void main(String[] args) throws Exception {
// GrammarFactory grammarFactory = new GrammarFactory();
// Grammar grammar = grammarFactory
// .getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/LRParser/ClosureTest.txt");
// grammar.print();
//
// System.out.println("");
// System.out.println("");
// System.out.println("------------------");
// ItemRule itemRule = new ItemRule(grammar.getRuleListClone().get(0));
// itemRule.println();
// Closure closure = itemRule.getClosure(grammar);
// closure.println();
//
// printLine();
// closure = Closure.getStartClosure(grammar);
// closure.println();
//
// printLine();
// closure.generateNextClosureMap(grammar);
// closure.println();
// printLine();
// closure.getNextClosure(grammar.getSymbolByName("INT")).println();
// printLine();
// closure.getNextClosure(grammar.getSymbolByName("E")).println();
// printLine();
// closure.getNextClosure(grammar.getSymbolByName("(")).println();
//
// println(closure.getNextClosure(grammar.getSymbolByName("E"))
// .equals(closure.getNextClosure(grammar.getSymbolByName("E")).clone()));
//
// println(closure.getNextClosure(grammar.getSymbolByName("(")).contains(closure.get(3).getNextProgress()));
//
// //
// closure.getNextClosure(grammar.getSymbolByName("(")).get(4).getNextProgress().getNextProgress().print();
//
// List<Closure> closureList = Closure.getClosureSet(grammar);
// int i = 0;
// for (Closure c : closureList) {
// printLine();
// System.out.println(i++);
// c.println();
// }
//
// printLine();
// grammar = grammarFactory
// .getGrammar("/Users/zijunyan/Desktop/JAVAWORKDIR/ParserStudy/file/LRParser/SummerWorkTest2.txt");
// grammar.println();
// closureList = Closure.getClosureSet(grammar);
// i = 0;
// for (Closure c : closureList) {
// printLine();
// System.out.println(i++);
// c.println();
// }
//
// printLine();
// for (ItemRule itemRules :
// closureList.get(13).getByNextSymbol(grammar.getSymbolByName("+"))) {
// itemRules.println();
// }
// }
// }
