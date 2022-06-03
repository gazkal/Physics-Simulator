package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder <GravityLaws>{
	public NewtonUniversalGravitationBuilder () {
		type = "nlug";
		desc = "Newton's Univeral law of gravitation";
	}

	@Override
	public GravityLaws createTheInstance(JSONObject ob) {
		return new NewtonUniversalGravitation();
	}
	
	public JSONObject createData() {

		JSONObject nlugJSON_Data = new JSONObject();

		return nlugJSON_Data;

	}
	
}
