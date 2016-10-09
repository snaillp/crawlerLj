package crawler.factory;

import java.util.HashMap;
import java.util.Map;

import processor.FangyuanProcessor;
import processor.ProcessorBaseInterface;


public class CommonProcessorFactory {

	private static Map<String, ProcessorBaseInterface> factory = new HashMap();
	static {
		factory.put("FangyuanProcessor", new FangyuanProcessor());
	}
	
	public static ProcessorBaseInterface getProcessor(String p)
	{
		return  factory.get(p);
	}
}
