package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws {
	@Override
	public void apply(List<Body> bodies) {
		//un cuerpo aplica una fuerza al resto...todos e aplica fuerza a todos
		
		for(int i=0; i< bodies.size();i++) {
			
			Vector F_ij = new Vector(2);
			for (int j = 0; j < bodies.size(); j++) {

				if (bodies.get(i).getMass() == 0.0) {
					bodies.get(i).setAcc(new Vector(2));
					bodies.get(i).setVel(new Vector(2));
					
				} else if (i != j) {

					Vector p_i = bodies.get(i).getPos();
					Vector p_j = bodies.get(j).getPos();

					double massPorMass = bodies.get(i).getMass() * bodies.get(j).getMass();
					Vector resta = p_j.minus(p_i);
					double magnitud = resta.magnitude();

					double f_ij = (G * massPorMass) / (magnitud * magnitud);

					F_ij = F_ij.plus(((p_j.minus(p_i)).direction()).scale(f_ij));
				}
			}
			if (bodies.get(i).getMass() != 0.0)
					bodies.get(i).setAcc(F_ij.scale((1.0/bodies.get(i).getMass())));
		}
	}
	
	public String toString() {
		return "La ley de gravitacion universal de Newton";
	}
}
