package list;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import grammar.GrammarException;

public class _ListMethodTest {
	public static void main(String[] args) throws GrammarException, IOException {
		LinkedList<Integer> list = new LinkedList();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		ListMethod<Integer> lm = new ListMethod<Integer>();
		LinkedList<LinkedList<List<Integer>>> lss = lm.partition(list, 1);

	}
}
