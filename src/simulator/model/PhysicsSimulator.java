package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {
	private double StepTime;
	private double ActualTime;
	private GravityLaws GLaw;
	private List<Body> Bodies;
	private List<SimulatorObserver> Observers;
	
	 public PhysicsSimulator (GravityLaws GLaw, double  StepTime)throws IllegalArgumentException {
		 if(GLaw.equals(null))
			 throw new IllegalArgumentException("GravityLaws are not valid");
		 else if(StepTime <= 0 )
			 throw new IllegalArgumentException("StepTime is not valid");
		 else {
			 this.Bodies= new ArrayList<Body>();
			 this.Observers = new ArrayList<SimulatorObserver>();
			 this.ActualTime=0.0;
			 this.GLaw = GLaw;
			 this.StepTime = StepTime;
		 }
			 
	 }
	 public void advance() {
		 
		 GLaw.apply(Bodies);
		 Bodies.forEach(Body->{
			 Body.move(StepTime);
		 });
		 this.ActualTime += this.StepTime;
		 this.Observers.forEach((observer)->{
			 observer.onAdvance(Bodies, ActualTime);;
		 });
	 }
	 public void addBody(Body b) throws IllegalArgumentException{
		 if(b == null)
			 throw new IllegalArgumentException ("Couldn't process Body in input file");
		 else if(Bodies.contains(b))
				 throw new IllegalArgumentException ("Body already in Simulation");
		 Bodies.add(b);
		 this.Observers.forEach((observer)->{
			 observer.onBodyAdded(Bodies, b);
		 });
	 }
	 public String toString() {
		StringBuilder state = new StringBuilder();
		state.append("{ \"time\": "+ ActualTime + ", \"bodies\": " + Bodies + "}\n");
		return state.toString(); 
	 }
	 public void reset() {
		 this.Bodies.clear();
		 this.ActualTime=0.0;
		 this.Observers.forEach((observer)->{
			 observer.onReset(Bodies, ActualTime, StepTime, GLaw.toString());
		 });
	 }
	 public void setDeltaTime(double dt) throws IllegalArgumentException{
		 if(dt > 0) {
			 try {
			 this.StepTime=dt;
			 }catch(Exception e){throw new IllegalArgumentException("Invalid DeltaTime"); }
			 this.Observers.forEach((observer)->{
				 observer.onDeltaTimeChanged(StepTime);
			 });
		 }else
			 throw new IllegalArgumentException("Invalid DeltaTime");
	 }
	 public void setGravityLaws(GravityLaws gravityLaws) throws IllegalArgumentException {
		 if(gravityLaws != null)
			 GLaw = gravityLaws;
		 else
			 throw new IllegalArgumentException("Invalid Gravity Laws");
		 this.Observers.forEach((observer)->{
			 observer.onGravityLawChanged(gravityLaws.toString());
		 });
	 }
	 public void addObserver(SimulatorObserver o) {
		 if(!this.Observers.contains(o)) {
			 this.Observers.add(o);
			 o.onRegister(Bodies, ActualTime, StepTime, GLaw.toString());
		 }
	 }
	public boolean bodyListisEmpty() {
		return Bodies.isEmpty();
	}
}
