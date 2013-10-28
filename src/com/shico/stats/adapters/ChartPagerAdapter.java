package com.shico.stats.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.shico.stats.MainActivity;
import com.shico.stats.charts.AddUsageChartFragment;
import com.shico.stats.charts.ChartFragment;
import com.shico.stats.charts.DVRecordingsChartFragment;
import com.shico.stats.charts.LiveUsageChartFragment;
import com.shico.stats.charts.MovieRentChartFragment;
import com.shico.stats.charts.ProgramChartFragment;
import com.shico.stats.charts.SelfcareSubChartFragment;
import com.shico.stats.charts.ShopViewChartFragment;
import com.shico.stats.charts.StartOverUsageChartFragment;
import com.shico.stats.charts.TimeshiftUsageChartFragment;
import com.shico.stats.charts.VODMovieChartFragment;
import com.shico.stats.charts.VODTrailerChartFragment;
import com.shico.stats.charts.WebTVLoginChartFragment;
import com.shico.stats.charts.WidgetShowChartFragment;

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
		case MainActivity.ADD_FRAGMENT_ID:
			fragment = new AddUsageChartFragment();
			break;
		case MainActivity.CHANNELS_FRAGMENT_ID:
			fragment = new LiveUsageChartFragment();
			break;
		case MainActivity.DVR_REC_FRAGMENT_ID:
			fragment = new DVRecordingsChartFragment();
			break;			
		case MainActivity.MOVIES_FRAGMENT_ID:
			fragment = new MovieRentChartFragment();
			break;
		case MainActivity.PROGRAMS_FRAGMENT_ID:
			fragment = new ProgramChartFragment();
			break;
		case MainActivity.SEFLCARE_SUB_FRAGMENT_ID:
			fragment = new SelfcareSubChartFragment();
			break;			
		case MainActivity.SHOPVIEWS_FRAGMENT_ID:
			fragment = new ShopViewChartFragment();
			break;						
		case MainActivity.STARTOVERS_FRAGMENT_ID:
			fragment = new StartOverUsageChartFragment();
			break;			
		case MainActivity.TIMESHIFT_FRAGMENT_ID:
			fragment = new TimeshiftUsageChartFragment();
			break;
		case MainActivity.VOD_MOVIE_FRAGMENT_ID:
			fragment = new VODMovieChartFragment();
			break;			
		case MainActivity.VOD_TRAILER_FRAGMENT_ID:
			fragment = new VODTrailerChartFragment();
			break;			
		case MainActivity.WEBTV_LOGIN_FRAGMENT_ID:
			fragment = new WebTVLoginChartFragment();
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
		case MainActivity.DVR_REC_FRAGMENT_ID:
		case MainActivity.PROGRAMS_FRAGMENT_ID:
		case MainActivity.TIMESHIFT_FRAGMENT_ID:
		case MainActivity.VOD_MOVIE_FRAGMENT_ID:
		case MainActivity.VOD_TRAILER_FRAGMENT_ID:
			return 4;
		case MainActivity.ADD_FRAGMENT_ID:
		case MainActivity.MOVIES_FRAGMENT_ID:
		case MainActivity.SEFLCARE_SUB_FRAGMENT_ID:
		case MainActivity.SHOPVIEWS_FRAGMENT_ID:
		case MainActivity.STARTOVERS_FRAGMENT_ID:
		case MainActivity.WEBTV_LOGIN_FRAGMENT_ID:
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
