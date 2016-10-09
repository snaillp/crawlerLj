package Pipeline;

import java.util.Map.Entry;

import dao.CommonMongoDao;
import entity.CommonEntity;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MongoPipeLine implements Pipeline{

	private CommonMongoDao nd;
	
	public MongoPipeLine(CommonMongoDao nd)
	{
		this.nd = nd;
	}
	public void setDao(CommonMongoDao nd)
	{
		this.nd = nd;
	}
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		for (Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			if (entry.getValue() instanceof Iterable) {
				Iterable value = (Iterable) entry.getValue();
				for(Object o: value){
					if(entry.getValue() instanceof String){
						nd.insert((String)entry.getValue());
						continue;
					}
					nd.insert((CommonEntity)o);
					System.out.println(((CommonEntity)o).toJson());
				}
			}else{
				if(entry.getValue() instanceof String){
//					System.out.println("String insert to mongo");
					nd.insert((String)entry.getValue());
					return;
				}
//				System.out.println("MongoPileline:"+((CommonEntity) entry.getValue()).toJson());
				nd.insert((CommonEntity) entry.getValue());
				
			}
		}
	}

}
