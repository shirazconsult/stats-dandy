package com.shico.stats.chartengine;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;

public class GroupedData {
	private double maxValue;
	
	SortedSet<String> allTitles = new TreeSet<String>(new Comparator<String>() {
		@Override
		public int compare(String lhs, String rhs) {
			return lhs.compareTo(rhs);
		}		
	});
	SortedSet<String> allXLabels = new TreeSet<String>(new Comparator<String>() {
		@Override
		public int compare(String lhs, String rhs) {
			return lhs.compareTo(rhs);
		}		
	});
	Map<String, Map<String, Double>> valueMap = new HashMap<String, Map<String,Double>>();
	int valueIdx;
	
	public GroupedData(int valueIdx) {
		super();
		this.valueIdx = valueIdx;
	}
	
	public void addValue(String xLabel, String title, double val){
		if(!valueMap.containsKey(xLabel)){
			valueMap.put(xLabel, new HashMap<String, Double>());
		}
		valueMap.get(xLabel).put(title, val);
		maxValue = Math.max(maxValue, val);
	}
	
	public void addXLabel(String xLabel){
		allXLabels.add(xLabel);
	}
	
	public void addTitle(String title){
		allTitles.add(title);
	}
	
	public double getValue(String xLabel, String title){
		Map<String, Double> map = valueMap.get(xLabel);
		if(map != null){
			Double res = map.get(title);
			if(res != null){
				return res;
			}
		}
		return 0;
	}
	
	public double getMaxValue(){
		return maxValue;
	}
	
	public int[] getColors(Context ctx){
		int size = allTitles.size();
		int[] res = new int[size];
		for (int i=0; i<size; i++) {
			res[i] = ChartUtil.getColorScheme(ctx)[i];
		}
		return res;
	}
}
