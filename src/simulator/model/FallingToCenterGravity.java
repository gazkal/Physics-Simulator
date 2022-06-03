package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class FallingToCenterGravity implements GravityLaws{
	@Override
	public void apply(List<Body> bodies) {
			bodies.forEach(Body ->{
				Body.setAcc((Body.getPos().direction().minus(new Vector(2))).scale(-g));
			});
	}
	
	public String toString() {
		return "Ley donde los cuerpos caen al centro";
	}
}
