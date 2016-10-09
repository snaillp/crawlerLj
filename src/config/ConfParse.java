package config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.gson.Gson;

import entity.WebConfEntity;


public class ConfParse {

	public static Object setEntity(String configFile, Class<?> T)
	{
		StringBuilder sb = new StringBuilder();	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "utf-8"));
			String confLine;
			while((confLine = br.readLine())!=null){
				confLine = confLine.replaceAll("[ 	]+", "");
				if(confLine.startsWith("#")){
					continue;
				}
				sb.append(confLine);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(sb.toString());
		Gson gn = new Gson();
		return gn.fromJson(sb.toString(), T);
	}
	
	public static void main(String[] args)
	{
		 WebConfEntity mce = (WebConfEntity) ConfParse.setEntity("./config\\matchnews1.conf", WebConfEntity.class);
		System.out.println(mce.toJson());
	}
}
