package entity;

import com.google.gson.Gson;

public class CommonConfEntity {
	
	public String toJson()
	{
		return new Gson().toJson(this);
	}

}
