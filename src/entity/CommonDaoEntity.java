package entity;

import dao.CommonMongoDao;


public class CommonDaoEntity extends CommonEntity{
	private CommonMongoDao dao;
	private CommonEntity entity;
	
	public CommonMongoDao getDao() {
		return dao;
	}
	public void setDao(CommonMongoDao dao) {
		this.dao = dao;
	}
	public CommonEntity getEntity() {
		return entity;
	}
	public void setEntity(CommonEntity entity) {
		this.entity = entity;
	}
}
