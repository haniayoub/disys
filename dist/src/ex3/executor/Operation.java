package ex3.executor;

import java.util.Random;

public class Operation {
	static Random generator2 = new Random( 19580427 );
		public static String doOp(char op, int a, int b){
			try{
				generator2.setSeed(System.currentTimeMillis());
				 int r = generator2.nextInt(500);
				 System.out.println("Going to sleep :" + r);
				Thread.sleep(r);
			} catch (InterruptedException ie) {
		}
		switch (op) {
		case '+':
			return String.valueOf(a + b);
		case '-':
			return String.valueOf(a - b);
		case 'x':
			return String.valueOf(a * b);
		case '/':
			return String.valueOf(a / b);
		case 'a':
			return String.valueOf(a + b / a);
		case 'b':
			return String.valueOf(a / b + b);
		case 'c':
			return String.valueOf(a * b / (a + b));
		case 'd':
			return String.valueOf(2 * a - b);
		case 'e':
			return String.valueOf(10 * a * a * a / (b * b));
		case 'f':
			return String.valueOf(a - a * b / (b - b * a));
		default:
			throw new IllegalArgumentException();
		}
	}
}
