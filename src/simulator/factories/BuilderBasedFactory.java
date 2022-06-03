package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory <T> implements Factory<T> {
	
	private List<Builder<T>>Builders;
	
	public BuilderBasedFactory(List<Builder<T>>Builders) {
		this.Builders = Builders;
	}

	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		T Object = null;
		int i = 0;
		while (Object == null && i < Builders.size()) {
			try {
				Object = Builders.get(i).createInstance(info);
				i++;
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
		if(Object == null) throw new IllegalArgumentException("Couldn't build object: " + info.toString());
		
		return Object;
	}

	@Override
	public List<JSONObject> getInfo() {
		List<JSONObject> infolist = new ArrayList<>();
		Builders.forEach(Builder->{
			infolist.add(Builder.getBuilderInfo());
		});
		return infolist;
	}

}
