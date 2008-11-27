package Common;

public class SynchronizedCounter {
	private Integer counter;

	public SynchronizedCounter(int initialValue) {
		counter = initialValue;
	}

	synchronized public void add(int value) {
		counter += value;
	}

	synchronized public void substract(int value) {
		counter -= value;
	}

	synchronized public int value() {
		return counter.intValue();
	}
}
