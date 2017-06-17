package grammar;

import java.util.LinkedList;

public class PhraseList extends LinkedList<Phrase> {
	public void sortByLength() {
		this.sort(new PhraseComparatorByLength());
	}

	public Phrase concatenateAll() {
		Phrase result = new Phrase(new SymbolList());
		for (Phrase phrase : this) {
			result = result.concatenate(phrase);
		}
		return result;
	}

	public void print() {
		System.out.print(this.toString());
	}

	public void println() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < this.size() - 1; ++i) {
			str += this.get(i).toString() + "\n";
		}
		str += this.get(this.size() - 1).toString();
		return str;
	}

}
