package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {

	public BasicBodyBuilder () {
		type = "basic";
		desc = "A basic body";
	}
	public Body createTheInstance(JSONObject ob) throws IllegalArgumentException{
		Body basic = new Body();
		double[] dPos;
		double[] dVel;
		try {
			dPos = jsonArrayToDoubleArray(ob.getJSONObject("data").getJSONArray("pos"));
			dVel = jsonArrayToDoubleArray(ob.getJSONObject("data").getJSONArray("vel"));
			basic.setId(ob.getJSONObject("data").getString("id"));
			basic.setPosition(new Vector(dPos));
			basic.setVel(new Vector(dVel));
			basic.setMass(ob.getJSONObject("data").getDouble("mass"));
		}catch(Exception e) {
			throw e;
		}
		return basic;
	}
	
	public JSONObject createData() {
		
		JSONObject basicBodyJSON_Data = new JSONObject();

		basicBodyJSON_Data.put("id", "Identifier");
		basicBodyJSON_Data.put("pos", "Position");
		basicBodyJSON_Data.put("vel", "Velocity");
		basicBodyJSON_Data.put("mass", "Mass");
		
		return basicBodyJSON_Data;
		
	}

}
