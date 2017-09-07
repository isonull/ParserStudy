package LRParser;

public class ShiftAction implements Action {
	public final Closure nextClosure;
	public final int index;

	public ShiftAction(Closure nextClosure_, int index_) {
		nextClosure = nextClosure_;
		index = index_;
	}

	public Closure getNextClosure() {
		return nextClosure;
	}

	public int getClosureIndex() {
		return index;
	}

	public String toString() {
		return "s" + index;
	}

}
