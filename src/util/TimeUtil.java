package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
	
	public static String getCurrentTime()
	{
		Date nowTime=new Date(); 
//		System.out.println(nowTime); 
		SimpleDateFormat timeFormater=new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]"); 
		return timeFormater.format(nowTime);
	}
	
	public static int compareDate(String date1, String date2)
	{
		String[] dateString1 = date1.split("-");
		String[] dateString2 = date2.split("-");
		for(int i=0; i<dateString1.length; ++i){
			int d1 = Integer.parseInt(dateString1[i]);
			int d2 = Integer.parseInt(dateString2[i]); 
			if(d1>d2){
				return 1;
			}else if(d1<d2){
				return -1;
			}
		}
		return 0;
	}
	
	public static String getCurrentDate()
	{
		Date nowTime=new Date(); 
		SimpleDateFormat timeFormater=new SimpleDateFormat("yyyy-MM-dd"); 
		return timeFormater.format(nowTime);
	}
	public static String getCurrentDate(String format)
	{
		Date nowTime=new Date(); 
		SimpleDateFormat timeFormater=new SimpleDateFormat(format); 
		return timeFormater.format(nowTime);
	}
	
	public static String changeFormat(String timeStr1, String format1, String format2)
	{
		DateFormat df = new SimpleDateFormat(format1);
    	try {
			Date dt = df.parse(timeStr1);
			SimpleDateFormat sdf = new SimpleDateFormat(format2);
			return sdf.format(dt);
		} catch (ParseException e) {
			//do nothing
//			e.printStackTrace();
		}
    	return "";
	}

	public static String second2Format(long second, String format)
	{
		Date dt = new Date(1000*second);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(dt);
	}
	
	public static int getCurYear()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	public static String yearInfer(String dateStr)
	{
		//时间串缺少年，返回完整的时间串，支持时间串的格式为 mm-dd xxxxx，要求必须以月开头，月和日之间短划线"-"分割，其他随便
		int monthEndindex = dateStr.indexOf("-");
		int month = Integer.parseInt(dateStr.substring(0, monthEndindex));
		int curMonth = getCurMonth();
		if(month>=curMonth-8){
			return getCurYear()+"-"+dateStr;
		}else{
			return (getCurYear()+1)+"-"+dateStr;
		}
	}
	
	public static int getCurMonth()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}
	public static int getCurDay()
	{
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);
	}
	
	public static long getCurSecond()
	{
		Date nowTime=new Date();
		return nowTime.getTime()/1000;
	}
	
    public static long getSecond(String dFormat, String timeStr)
    {
    	if(null == dFormat || null==timeStr){
    		return -1;
    	}
    	DateFormat df = new SimpleDateFormat(dFormat);
    	try {
			Date dt = df.parse(timeStr);
			return dt.getTime()/1000;
		} catch (ParseException e) {
			//do nothing
			e.printStackTrace();
		}
		return -1;
    }
    
    public static String getDayInc(String dFormat, int increment)
    {
    	String datetime = "";
    	Date nowTime = new Date();
    	DateFormat df = new SimpleDateFormat(dFormat);
    	try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(nowTime);
			calendar.add(Calendar.DAY_OF_MONTH, increment);
			datetime = df.format(calendar.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return datetime;
    }
    
    public static String addDay(String dFormat, String timeStr, int days)
    {
    	String datetime = "";
    	DateFormat df = new SimpleDateFormat(dFormat);
    	try {
			Date dt = df.parse(timeStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt);
			calendar.add(Calendar.DAY_OF_MONTH, days);
			datetime = df.format(calendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return datetime;
    }

    public static boolean filterLinkByTime(String link)
    {
    	String[] urlPieceList = link.split("/");
    	String newsDate = urlPieceList[urlPieceList.length - 2];
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss"); 
		try {
			Date dt = df.parse(newsDate);
			Calendar curr = Calendar.getInstance();
			curr.set(Calendar.MONTH, curr.get(Calendar.MONTH)-1);
			Date lastYear = curr.getTime();
			if(dt.compareTo(lastYear) <= 0){
				return false;
			}
		} catch (ParseException e) {
			//do nothing
//			e.printStackTrace();
		}
		return true;
    }
    public static List<String> filterLinklistByTime(List<String> linkList)
    {
    	List<String> newlinkList = new ArrayList();
    	for(String l: linkList){
    		if(filterLinkByTime(l)){
    			newlinkList.add(l);
    		}
    	}
    	return newlinkList;
    }

    public static void main(String[] args)
    {
    	System.out.println("开赛时间：11-22 12:30".substring(5));;
    	System.out.println(getSecond("日期：yyyy-MM-dd hh:mm", "日期：2015-04-12 17:18   来源:捷报 字号: ［大 <> 中 <> 小 <> ］ "));
    	System.out.println(second2Format(getSecond("日期：yyyy-MM-dd hh:mm", "日期：2015-04-12 17:18   来源:捷报 字号: ［大 <> 中 <> 小 <> ］ "), "yyyy-MM-dd"));
    	System.out.println(getSecond("yy-MM-dd hh:mm", "15-08-26 02:00"));
    	System.out.println(getSecond("yy-MM-dd hh:mm", "15-06-26 02:00"));
    	System.out.println(getCurDay());
    	System.out.println(Integer.parseInt("02"));
    	System.out.println(getCurSecond());
    	System.out.println(getDayInc("yyyy-MM-dd ", 8));
    	StringBuilder urlSb = new StringBuilder();
		//10天
		for(int i=-1; i<8; ++i){
			urlSb.append("http://odds.sports.sina.com.cn/liveodds/match_search.php?date="+TimeUtil.getDayInc("yyyy-MM-dd ", i));
		}
		System.out.println(urlSb.toString());
    }
}
