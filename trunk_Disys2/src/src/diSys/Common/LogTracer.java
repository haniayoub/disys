package diSys.Common;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LogTracer {
	private ConcurrentLinkedQueue<String> Lines;
	private int MaxSize;

	public LogTracer(int maxLines) {
		Lines = new ConcurrentLinkedQueue<String>();
		MaxSize = maxLines;
	}

	public void addLine(String line) {
		Lines.add(line);
		if (Lines.size() > MaxSize)
			Lines.poll();
	}

	@Override
	public String toString() {
		String $ = "";
		for (String line : Lines)
			$ = line + "\n" + $;
		return $;
	}
}
