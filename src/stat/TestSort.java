package stat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import statentity.BizStat;

import com.google.gson.Gson;

public class TestSort {
	private static <K, V> List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map,
			final int sort) {
		List<Map.Entry<K, V>> orderList = new ArrayList<Map.Entry<K, V>>(
				map.entrySet());
//		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(orderList, new Comparator<Map.Entry<K, V>>() {
			// @Override
			@SuppressWarnings("unchecked")
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (((Comparable<V>) o1.getValue()).compareTo(o2.getValue()))
						* sort;
			}
		});
		return orderList;
	}
	public void test(){
		Map<String, BizStat> avgMap = new HashMap();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream("xiaoqustat.txt"), "utf-8"));
			String line;
			Gson parser = new Gson();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				BizStat xiaoquAvg = parser.fromJson(line, BizStat.class);
				avgMap.put(xiaoquAvg.getXiaoqu(), xiaoquAvg);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map.Entry<String, BizStat>> bizList = sortMapByValue(avgMap, 1);
		for(Map.Entry<String, BizStat> bizEntity: bizList){
			System.out.println(bizEntity.getValue().toJson());
		}
	}
	public static void main(String[] args){
		new TestSort().test();
	}
}
