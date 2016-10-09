package dao;

import entity.ServerConfEntity;

public class FangyuanDao extends CommonMongoDao{
	
	@Override
	protected String getTableName(ServerConfEntity sce) {
		return sce.getFangyuanTableName();
	}
	
	public static void main(String[] args){
		
		
	}
	
	
}
