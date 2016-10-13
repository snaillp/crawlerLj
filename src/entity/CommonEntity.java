package entity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class CommonEntity {
	private Map<String, Object> fieldUpdate2Mongo = null;
	private Map<String, Object> fieldAppend2Mongo;
	public String toJson()
	{
		return new Gson().toJson(this);
	}
	
	/**
	 * 需要使用mongo update接口的需要重载此接口，返回更新条件
	 */
	public String getJsonUpdateCond()
	{
		return null;
	}
	//对象自身需满足的条件
	public boolean getSelfJsonUpdateCond()
	{
		return true;
	}
	/**
	 * 和getJsonUpdateCond，getSelfJsonUpdateCond配合使用，设置某些字段
	 */
	public Map<String, Object> getFieldUpate2Mongo()
	{
		return fieldUpdate2Mongo;
	}
	public void setFieldUpate2Mongo(Map<String, Object> fieldMap)
	{
		fieldUpdate2Mongo = fieldMap;
	}
	public void setFieldUpate2Mongo(String key, Object value)
	{
		if(null == fieldUpdate2Mongo){
			fieldUpdate2Mongo = new HashMap();
		}
		fieldUpdate2Mongo.put(key, value);
	}
	
	/**
	 * 需要使用mongo append接口的需要重载此接口，返回更新条件
	 */
	public String getJsonAppendCond()
	{
		return null;
	}
	//返回false标明未追加，true表示追加了新的元素
	public boolean appendList(CommonEntity ce)
	{
		return false;
	}
	// append end
	public Map<String, Object> getFieldAppend2Mongo() {
		return fieldAppend2Mongo;
	}

	public void setFieldAppend2Mongo(Map<String, Object> fieldAppend2Mongo) {
		this.fieldAppend2Mongo = fieldAppend2Mongo;
	}
	public void addFieldAppend2Mongo(String field, Object value)
	{
		if(null == fieldAppend2Mongo){
			fieldAppend2Mongo = new HashMap();
		}
		fieldAppend2Mongo.put(field, value);
	}
}
