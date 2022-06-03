package simulator.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.*;

public class Controller {
	
	private PhysicsSimulator simulator;
	private Factory<Body> bodyfactory;
	Factory<GravityLaws> gLawfactory;
	
	public Controller(PhysicsSimulator simulator, Factory<Body> bodyfactory,Factory<GravityLaws> gLawfactory) {
		this.simulator = simulator;
		this.bodyfactory = bodyfactory;
		this.gLawfactory = gLawfactory;
	}
	public void loadBodies(File in) throws FileNotFoundException, IllegalArgumentException{
		FileInputStream input =null;
		try {
			input = new FileInputStream(in);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException ("File does not exist or it was moved");
		}
		JSONObject jsonInput = new JSONObject(new JSONTokener(input));
		JSONArray jsonlist = jsonInput.getJSONArray("bodies");
		for(int i=0;i<jsonlist.length();i++) {
			JSONObject body = jsonlist.getJSONObject(i);
			try {
				simulator.addBody(bodyfactory.createInstance(body));
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}

	}
	public void run(int n, OutputStream out) throws IOException {
		StringBuilder statelist = new StringBuilder("{ \"states\": [");
		statelist.append(simulator + ", ");
		for(int i=0;i<n;i++) {
			simulator.advance();
			statelist.append(simulator);
			if(i+1 != n) {
				statelist.append(", ");
			}
		}
		statelist.append(" ] }");	
		out.write(statelist.toString().getBytes());
	}
	public void reset() {
		simulator.reset();
	}
	public void setDeltaTime(double dt) throws IllegalArgumentException{
		try {
		simulator.setDeltaTime(dt);
		}catch(IllegalArgumentException e) { throw e;}
	}
	public void addObserver(SimulatorObserver o) {
		simulator.addObserver(o);
	}
	public void run(int n) {
		for(int i=0;i<n;i++) {
			simulator.advance();
		}
	}
	public Factory<GravityLaws> getGravityLawsFactory(){
		return this.gLawfactory;
	}
	public void setGravityLaws(JSONObject info) throws IllegalArgumentException  {
		try {
		this.simulator.setGravityLaws(gLawfactory.createInstance(info));
		}catch(IllegalArgumentException e) {
			throw e;
		}
	}
	public boolean bodyListisEmpty() {
		return simulator.bodyListisEmpty();
	}
}
