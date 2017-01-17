package dao;

import entity.ServerConfEntity;

public class BizAvgStatDao extends CommonMongoDao{
	@Override
	protected String getTableName(ServerConfEntity sce) {
		return sce.getBizavgstatTableName();
	}
}
