package grammar;

import java.util.LinkedList;

public class PhraseList extends LinkedList<Phrase> {
	public void sortByLength() {
		this.sort(new PhraseComparatorByLength());
	}

}
