package list;

import java.util.LinkedList;
import java.util.List;

public class ListMethod<T> {

	public LinkedList<LinkedList<List<T>>> partition(List<T> list, int part) {
		LinkedList<LinkedList<List<T>>> result = new LinkedList<LinkedList<List<T>>>();
		LinkedList<List<T>> tempPartition = new LinkedList<List<T>>();
		if (part <= 1) {
			tempPartition.add((List<T>) list);
			result.add(tempPartition);
			return result;
		} else {
			for (int i = 1; i <= list.size() - part + 1; ++i) {
				result.addAll(prependAll(partition(list.subList(i, list.size()), part - 1), list.subList(0, i)));

			}
		}
		return result;
	}

	public LinkedList<LinkedList<List<T>>> prependAll(LinkedList<LinkedList<List<T>>> listList, List<T> prepend) {
		for (LinkedList<List<T>> list : listList) {
			list.addFirst(prepend);
		}
		return listList;
	}

	public T findEqualElement(List<T> list, T o) {
		for (T t : list) {
			if (t.equals(o)) {
				return t;
			}
		}
		return null;
	}
}
