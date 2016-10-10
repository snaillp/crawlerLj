package dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.TimeUtil;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import entity.CommonEntity;
import entity.ServerConfEntity;

public abstract class CommonMongoDao {
	protected Mongo mgClient;
	protected DB db;
	protected DBCollection dbc;
	protected String mgHost = "127.0.0.1";
	protected int mgPort = 27017;
	protected String dbName = "ljdata";
	protected String table = "fangyuan";
	protected DBCollection coll;

	public void setDB(String dbName) {
		this.dbName = dbName;
	}

	public void setTable(String tableName) {
		this.table = tableName;
	}
	//子类必须重载此方法把自己的表名set进来
	abstract protected String getTableName(ServerConfEntity sce);

	public void init(ServerConfEntity sce)
	{
		mgHost = sce.getmongoHost();
		mgPort = sce.getmongoPort();
		dbName = sce.getmongoDBName();
		table = getTableName(sce);
		init();
	}
//	public void init(String conffile)
//	{
//		ServerConfEntity sce = new ServerConfEntity();
//		sce = (ServerConfEntity)ConfParse.setEntity(conffile, sce);
//		mgHost = sce.getmongoHost();
//		mgPort = sce.getmongoPort();
//		dbName = sce.getmongoDBName();
//		table = sce.getmongotableName();
//		init();
//	}

	public void init() {
		try {
			mgClient = new MongoClient(mgHost, mgPort);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		db = mgClient.getDB(dbName);
		coll = db.getCollection(table);
		if(null == db || null == coll){
			System.err.println("mongo connect error");
		}
		System.out.println("coll:"+coll.findOne().toString());
		System.out.println("db:"+db.getName());
	}

	public void destroy() {
		if (null != coll) {
			coll.drop();
			coll = null;
		}
		if (null != db) {
			db.dropDatabase();
			db = null;
		}
		if (null != mgClient) {
			mgClient.dropDatabase(dbName);
			mgClient = null;
		}
	}

	public DBObject convert2MongoObject(CommonEntity ce) {
		if(null == ce){
			return null;
		}
		String newsJson = ce.toJson();		
		DBObject dbObject = (DBObject) JSON.parse(newsJson);
		return dbObject;
	}

//	//只需要覆盖此函数，参考NewsDao.java
//	public CommonEntity convert2CommonEntity(DBObject dob) {
//		String newsJson = dob.toString();
//		return new Gson().fromJson(newsJson, CommonEntity.class);
//	}
	public Object convert2CommonEntity(DBObject dob, Class<?> T)
	{
		if(null == dob){
			return null;
		}
		String newsJson = dob.toString();
		return new Gson().fromJson(newsJson, T);
	}

	public void insert(CommonEntity ce) {
		if(null == ce){
			return;
		}
//		System.out.println("Dao insert:"+ce.toJson());
		coll.insert(convert2MongoObject(ce));
	}
	//更新整个对象，注意需要重载的2个接口
	public void update(CommonEntity ce) {
		if(null == ce){
			return;
		}
		if(ce.getSelfJsonUpdateCond()){
			System.out.println(TimeUtil.getCurrentTime()+" "+this.getClass().getSimpleName()+" update "+ce.toJson());
			coll.update(convert2MongoObject(ce.getJsonUpdateCond()), convert2MongoObject(ce), true, false);
		}
	}
	//更新字段，不存在时添加，注意CommonEntity中需要重载的3个接口
	public void setField(CommonEntity ce) {
		if(null == ce){
			return;
		}
		Map<String, Object> fieldMap = ce.getFieldUpate2Mongo();
		if(ce.getSelfJsonUpdateCond() && !fieldMap.isEmpty()){
			DBObject updatedValue=new BasicDBObject();
			for(Entry<String, Object> entity: fieldMap.entrySet()){
				updatedValue.put(entity.getKey(), entity.getValue());
			}
			DBObject updateSetValue=new BasicDBObject("$set",updatedValue);
			coll.update(convert2MongoObject(ce.getJsonUpdateCond()), updateSetValue, true, false);			
		}
	}
	//更新字段，更新的方式为追加
	public void appendField(CommonEntity ce) {
		if(null == ce){
			return;
		}
		String appendCond = ce.getJsonAppendCond();
		
//		String ceTypeName = ce.getClass().getSimpleName();
		Class<?> ceType = ce.getClass();
		boolean appendFlag = false;
		if(appendCond != null && !appendCond.isEmpty()){
			DBObject appendCondObj = convert2MongoObject(appendCond);
			//找到库里的数据
			CommonEntity dbObject = (CommonEntity)findOne(appendCondObj, ceType);
			if(null != dbObject){
				appendFlag = dbObject.appendList(ce);
				if(!appendFlag){
					return;
				}
			}else{
				dbObject = ce;
			}
			coll.update(appendCondObj, convert2MongoObject(dbObject), true, false);
		}
	}
	
	public DBObject convert2MongoObject(String jStr) {
		if(null == jStr){
			return null;
		}
		DBObject dbObject = (DBObject) JSON.parse(jStr);
		return dbObject;
	}
	
	public void insert(String jStr) {
		if(null == jStr){
			return;
		}
		coll.insert(convert2MongoObject(jStr));
	}
	public Object findOne(DBObject bDbOb,  Class<?> T) {
		
		DBObject dbb = coll.findOne(bDbOb);
		return convert2CommonEntity(dbb, T);
	}
	public Object findOne(Map<String, Object> queryMap,  Class<?> T) {
		BasicDBObject bDbOb = new BasicDBObject();
		for(String key: queryMap.keySet()){
			bDbOb.append(key, queryMap.get(key));
		}
		DBObject dbb = coll.findOne(bDbOb);
		return convert2CommonEntity(dbb, T);
	}
	public Object findOne(String id,  Class<?> T) {
		DBObject dbb = coll.findOne(new BasicDBObject("id", id));
		return convert2CommonEntity(dbb, T);
	}	
}
