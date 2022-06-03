package tmp;

import simulator.misc.Vector;

public class Test {

	
	public static void main(String[] args) {
		double[] data_1 = { 1.4e-3, 3.5};
		double[] data_2 = { 3.4, 1.5};
		
		Vector a = new Vector( data_1);
		Vector b = new Vector(data_2);
		
		System.out.println(a);
		System.out.println(a.direction());
		System.out.println(a.distanceTo(b));
		
	}
}
