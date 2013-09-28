package com.shico.stats.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.shico.stats.ChartFragment;
import com.shico.stats.LiveUsageChartFragment;
import com.shico.stats.MainActivity;
import com.shico.stats.MovieRentChartFragment;
import com.shico.stats.WidgetShowChartFragment;

public class ChartPagerAdapter extends FragmentPagerAdapter {
	private String chartName;
	private int chartId;

	public ChartPagerAdapter(FragmentManager fm, String chartName, int chartId) {
		super(fm);
		this.chartId = chartId;
		setChartName(chartName);
	}

	@Override
	public Fragment getItem(int page) {
		Bundle args = new Bundle();
		args.putString(MainActivity.ARG_MENU_CHART_ITEM_NAME, chartName);
		args.putInt(ChartFragment.ARG_CHART_VIEWPAGE, page);
		
		ChartFragment fragment = null;
		if(chartName.equalsIgnoreCase("channels")){				
			fragment = new LiveUsageChartFragment();
		}else if(chartName.equalsIgnoreCase("movies")){
			fragment = new MovieRentChartFragment();
		}else if(chartName.equalsIgnoreCase("programs")){
//			Toast.makeText(getActivity(), "No view for "+currentChartName+" is implemented yet.", Toast.LENGTH_LONG).show();				
		}else if(chartName.equalsIgnoreCase("widgets")){
			fragment = new WidgetShowChartFragment();
		}
		
		fragment.setArguments(args);
		
		return fragment;		
	}
		
	@Override
	public int getCount() {
		if(chartName.equalsIgnoreCase("channels")){
			return 2;
		}
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return (chartId*100)+position;
	}	

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	
	public void setChartId(int chartId) {
		this.chartId = chartId;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
