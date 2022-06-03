package simulator.model;

import java.util.List;


public interface GravityLaws {
	public static final double G=6.67e-11;
	public static final double g = 9.81;
	public void apply(List<Body>bodies);
	public String toString();
}
