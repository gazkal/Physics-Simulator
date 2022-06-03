package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder <GravityLaws>{
	public NoGravityBuilder () {
		type ="ng";
		desc = "No gravity";
	}

	@Override
	public GravityLaws createTheInstance(JSONObject ob) {
		return new NoGravity();
	}
	
	public JSONObject createData() {

		JSONObject ngJSON_Data = new JSONObject();

		return ngJSON_Data;

	}
}
