package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

abstract public class Builder <T> {
	protected String type;
	protected String desc;

	
	public Builder() {
		this.type = "";
		this.desc = "";
	}
	
	public T createInstance (JSONObject info)throws IllegalArgumentException {
		T instance = null;
		if(type.equals(info.getString("type"))) {
			try {
				instance = createTheInstance(info);
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
		return instance;
	}
	
	public JSONObject getBuilderInfo() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type",type);
		jsonObj.put("desc",desc);
		jsonObj.put("data",createData());
		return jsonObj;
	}
	
	public double[] jsonArrayToDoubleArray (JSONArray jr) {
		double[] darr = new double[jr.length()];
		for (int i = 0; i < jr.length(); i++)
		{
			darr[i] = jr.getDouble(i);
		}
		return darr;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	//ABSTRACTS
	abstract public T createTheInstance(JSONObject ob)throws IllegalArgumentException;
	abstract public JSONObject createData();
	//create data will be the data created in the builders
}
