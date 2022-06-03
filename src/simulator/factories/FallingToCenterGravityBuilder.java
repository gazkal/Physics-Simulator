package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder <GravityLaws>{

	public FallingToCenterGravityBuilder () {
		type = "ftcg";
		desc = "Falling to center gravity";
	}
	@Override
	public GravityLaws createTheInstance(JSONObject ob) {
		return new FallingToCenterGravity();
	}

	public JSONObject createData() {

		JSONObject ftcgJSON_Data = new JSONObject();


		return ftcgJSON_Data;

	}
	
}
