package diSys.Executor;

import diSys.Common.Common;

public class DummyBenchmarkClass {
	@SuppressWarnings("unused")
	private int i;
	@SuppressWarnings("unused")
	private String str;
	@SuppressWarnings("unused")
	private long l;

	public DummyBenchmarkClass() {
		i = rand();
		str = new Integer(rand()).toString();
		l = new Long(rand());
	}
	
	private int rand()
	{
		return Common.getRandom(Integer.MIN_VALUE+1, Integer.MAX_VALUE-1);
	}
}
