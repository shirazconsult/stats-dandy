package com.shico.stats.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.shico.stats.ChartFragment;
import com.shico.stats.LiveUsageChartFragment;
import com.shico.stats.MainActivity;
import com.shico.stats.MovieRentChartFragment;
import com.shico.stats.ProgramChartFragment;
import com.shico.stats.WidgetShowChartFragment;
import com.shico.stats.settings.SettingsFragment;

public class ChartPagerAdapter extends FragmentPagerAdapter {
	private String name;
	private int id;

	public ChartPagerAdapter(FragmentManager fm, String name, int fragmentId) {
		super(fm);
		this.id = fragmentId;
		setName(name);
	}

	@Override
	public Fragment getItem(int page) {
		ChartFragment fragment = null;
		
		switch(id){
		case MainActivity.SETTINGS_FRAGMENT_ID:
			return new SettingsFragment();
		case MainActivity.ABOUT_FRAGMENT_ID:
		case MainActivity.HELP_FRAGMENT_ID:
			throw new IllegalStateException("Not implemented");
		case MainActivity.CHANNELS_FRAGMENT_ID:
			fragment = new LiveUsageChartFragment();
			break;
		case MainActivity.MOVIES_FRAGMENT_ID:
			fragment = new MovieRentChartFragment();
			break;
		case MainActivity.PROGRAMS_FRAGMENT_ID:
			fragment = new ProgramChartFragment();
			break;
		case MainActivity.WIDGETS_FRAGMENT_ID:
			fragment = new WidgetShowChartFragment();
			break;
		}	
		Bundle args = new Bundle();
		args.putString(MainActivity.ARG_MENU_CHART_ITEM_NAME, name);
		args.putInt(ChartFragment.ARG_CHART_VIEWPAGE, page);
		fragment.setArguments(args);			
		return fragment;		
	}
		
	@Override
	public int getCount() {
		switch(id){
		case MainActivity.CHANNELS_FRAGMENT_ID:
		case MainActivity.PROGRAMS_FRAGMENT_ID:
			return 4;
		case MainActivity.MOVIES_FRAGMENT_ID:
		case MainActivity.WIDGETS_FRAGMENT_ID:
			return 2;
		default:
			return 1;
		}	
	}

	@Override
	public long getItemId(int position) {
		return (id*100)+position;
	}	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
