package dao;

import entity.ServerConfEntity;

public class HousestatDao  extends CommonMongoDao{
	
	@Override
	protected String getTableName(ServerConfEntity sce) {
		return sce.getHousestatTableName();
	}

}
