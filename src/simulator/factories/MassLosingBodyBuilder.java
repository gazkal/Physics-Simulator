package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body> {
	
	public MassLosingBodyBuilder () {
		type = "mlb";
		desc = "A mass losing body";
	}

	@Override
	public Body createTheInstance(JSONObject ob)throws IllegalArgumentException{
		MassLossingBody mlb = new MassLossingBody();
		double[] dPos;
		double[] dVel;
		try {
			dPos = jsonArrayToDoubleArray(ob.getJSONObject("data").getJSONArray("pos"));
			dVel = jsonArrayToDoubleArray(ob.getJSONObject("data").getJSONArray("vel"));
			mlb.setId(ob.getJSONObject("data").getString("id"));
			mlb.setPosition(new Vector(dPos));
			mlb.setVel(new Vector(dVel));
			mlb.setMass(ob.getJSONObject("data").getDouble("mass"));
			mlb.setlossFrequency(ob.getJSONObject("data").getDouble("freq"));
			mlb.setlossFactor(ob.getJSONObject("data").getDouble("factor"));
		}catch(Exception e) {
			throw new IllegalArgumentException("Error encontrado en Data");
		}
		return mlb;
		
	}
	
	public JSONObject createData() {

		JSONObject massLosingBodyJSON_Data = new JSONObject();

		massLosingBodyJSON_Data.put("id", "Identifier");
		massLosingBodyJSON_Data.put("pos", "Position");
		massLosingBodyJSON_Data.put("vel", "Velocity");
		massLosingBodyJSON_Data.put("mass", "Mass");
		massLosingBodyJSON_Data.put("freq", "Frequency");
		massLosingBodyJSON_Data.put("factor", "Factor");

		return massLosingBodyJSON_Data;

	}

}
