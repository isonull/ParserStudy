package grammar;

import java.util.Comparator;

public class PhraseComparatorByLength implements Comparator<Phrase> {

	@Override
	public int compare(Phrase o1, Phrase o2) {
		return o1.length() - o2.length();
	}

}
