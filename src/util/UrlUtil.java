package util;

import java.net.MalformedURLException;

public class UrlUtil {

	public static String getSite(String url)
	{
		try {
			return new java.net.URL(url).getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String getFile(String url)
	{
		String[] urlPartList = url.split("/");
		return urlPartList[urlPartList.length-1];
	}
	
	public static boolean isValidStr(String str)
	{
		return null!=str && !str.isEmpty();
	}
	
	public static boolean isInvalidStr(String str)
	{
		return !isValidStr(str);
	}

	public static String getSuffix(String filename)
	{
		//返回文件的后缀名，例如a.png，返回png
		String[]  filepart = filename.split("\\.");
    	return filepart[filepart.length-1];
	}
	public static String getFilebase(String filename)
	{
		//返回去掉后缀的文件名部分
		String[]  filepart = filename.split("\\.");
    	return filepart[0];
	}
	
    public static String rmArrow(String str)
    {
    	return str.replace("↑", "").replace("↓", "").trim();
    }
    
	public static void main(String[] args)
	{
		System.out.println(getFile("http://zx.aicai.com/563/2-1psgix00f03bi1db2djtomq6ns.html"));
	}
}
