package util;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class LogUtil {
	static {
		DOMConfigurator.configure("./log4j.xml");
	}
	public static Logger getLogger(String logname)
	{
		return Logger.getLogger(logname);
	}
}
