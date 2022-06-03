package simulator.model;

import simulator.misc.*;

public class Body {
	private String Id;
	private Vector pos, vel, acc;
	private double mass;
	
	public Body() {
		this.acc = new Vector(2);
	}
	public Body(String Id, Vector pos, Vector vel, Vector acc, double mass) {
		this.Id = Id;
		this.mass = mass;
		this.pos = pos;
		this.vel = vel;
	}
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		this.Id = id;
	}
	
	public Vector getPos() {
		return pos;
	}
	public Vector getVel() {
		return vel;
	}
	public void setVel(Vector vel) {
		this.vel = vel;
	}
	public Vector getAcc() {
		return acc;
	}
	void setAcc(Vector acc) {
		this.acc = acc;
	}
	public double getMass() {
		return mass;
	}
	public void setMass(double m) throws IllegalArgumentException {
		if(m>0)
			this.mass=m;
		else throw new IllegalArgumentException("Mass in body " + this.Id + " is zero or negative");
	}
	public String toString() {
		return "{" + "\"id\": \""+ this.Id + "\", " + "\"pos\": "+ this.pos + ", " + "\"vel\": "+ this.vel+ ", " + "\"acc\": "+ this.acc + ", " + "\"mass\": "+ this.mass +"}";
	}
	@Override
	public boolean equals (Object b) {
		@SuppressWarnings("unused")
		boolean ok;
		if(!(b instanceof Body)) ok=false;
		else if(Id.equals(((Body) b).getId()))
			ok=true;
		else
			ok=false;
		
		return false;
	}
	public void setPosition(Vector p) {
		Vector aux = new Vector(p);
		this.pos=aux;
	}
	void move(double t) {
		Vector vPorT = vel.scale(t);
		Vector AccPorTSqrd= acc.scale(0.5);
		Vector mitadAccPorTSqrd = AccPorTSqrd.scale(t*t);
		Vector suma1 = pos.plus(vPorT);
		Vector suma2 = suma1.plus(mitadAccPorTSqrd);
		pos = (suma2);
		
		Vector accPorT = acc.scale(t);
		vel = vel.plus(accPorT);
		
	}
	public void copyToBody(Body b) {
		b.acc = this.acc;
		b.Id = this.Id;
		b.mass = this.mass;
		b.pos= this.pos;
		b.vel= this.vel;
		
	}
	
}
