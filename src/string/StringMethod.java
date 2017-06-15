package string;

import java.util.LinkedList;

public class StringMethod {

	public static LinkedList<LinkedList<String>> partition(String str, int part) {
		LinkedList<LinkedList<String>> result = new LinkedList();
		LinkedList<String> temp = new LinkedList();
		if (part <= 1) {
			temp.add(str);
			result.add(temp);
		} else {
			for (int i = 1; i <= str.length() - part + 1; ++i) {
				result.addAll(addAllFirst(partition(str.substring(i), part - 1), str.substring(0, i)));
			}
		}
		return result;
	}

	public static LinkedList<LinkedList<String>> addAllFirst(LinkedList<LinkedList<String>> strListList, String str) {
		for (LinkedList<String> strList : strListList) {
			strList.addFirst(str);
		}
		return strListList;
	}

	public static void print(LinkedList<LinkedList<String>> strListList) {
		for (LinkedList<String> strList : strListList) {
			for (String str : strList) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
	}

}
