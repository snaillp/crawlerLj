package Pipeline;

import java.util.Map.Entry;

import dao.CommonMongoDao;
import entity.CommonEntity;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MongoAppendfieldPipeLine  implements Pipeline {
	private CommonMongoDao dao;
	
	public MongoAppendfieldPipeLine(CommonMongoDao dao)
	{
		this.dao = dao;
	}
	public void setDao(CommonMongoDao dao)
	{
		this.dao = dao;
	}
	public void process(ResultItems resultItems, Task task) {
		for (Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			if (entry.getValue() instanceof Iterable) {
				Iterable value = (Iterable) entry.getValue();
				for(Object o: value){
					dao.appendField((CommonEntity)o);
//					System.out.println(((CommonEntity)o).toJson());
				}
			}else{
//				System.out.println("MongoPileline:"+((CommonEntity) entry.getValue()).toJson());
				dao.appendField((CommonEntity) entry.getValue());
			}
		}
	}

}
