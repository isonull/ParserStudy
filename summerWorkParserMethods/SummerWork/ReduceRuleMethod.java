package SummerWork;

public class ReduceRuleMethod {

	public static Object add(Object a, Object b) {
		if (a instanceof Integer && b instanceof Integer) {
			return (Integer) ((Integer) a) + ((Integer) b);
		} else {
			return (Double) ((Double) a) + ((Double) b);
		}
	}

	public static Object min(Object a, Object b) {
		if (a instanceof Integer && b instanceof Integer) {
			return (Integer) ((Integer) a) + ((Integer) b);
		} else {
			return (Double) ((Double) a) + ((Double) b);
		}
	}

	public static Object prod(Object a, Object b) {
		if (a instanceof Integer && b instanceof Integer) {
			return (Integer) ((Integer) a) + ((Integer) b);
		} else {
			return (Double) ((Double) a) + ((Double) b);
		}
	}

	public static Double cos(Double a) {
		return Math.cos(a);
	}

	public static Integer fact(Integer a) throws Exception {
		if (a < 0) {
			throw new MethodException(a + "! is an invalid operation");
		}
		int r = 1;
		for (; a > 0; --a) {
			r *= a;
		}
		return r;
	}

	public static Object conzt(Object a) {
		return a;
	}

}
