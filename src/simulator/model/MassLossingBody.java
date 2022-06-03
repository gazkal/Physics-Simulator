package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body{
	private double lossFactor;
	private double lossFrequency;
	private double c=0.0;
	
	public MassLossingBody() {
		
	}
	
	public MassLossingBody(String Id, Vector pos, Vector vel, Vector acc, double mass, double lossFactor, double lossFrequency) {
		super(Id,pos,vel,acc,mass);
		this.lossFactor=lossFactor;
		this.lossFrequency=lossFrequency;
	}
	void move(double t) {	
		super.move(t);
		c+=t;
		if(c>=lossFrequency) {
			setMass(getMass()*(1-lossFactor));
			c=0.0;
		}
	}
	
	public void setlossFactor (double lossFactor) {
		this.lossFactor = lossFactor;
	}
	
	public void setlossFrequency (double lossFrequency) {
		this.lossFrequency = lossFrequency;
	}
}
