package com.shico.stats;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shico.stats.adapters.ChartPagerAdapter;
import com.shico.stats.util.ChartType;

public class ChartContainerFragment extends Fragment {
	private final static String CURRENT_PAGE = "current.page";
	
	private View view;
	private int currentPage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.viewpager, container, false);
		if(savedInstanceState != null){
			currentPage = savedInstanceState.getInt(CURRENT_PAGE);
		}
		
		Bundle args = getArguments();
		if(args != null){
			newViewPager(
				args.getString(MainActivity.ARG_MENU_CHART_ITEM_NAME), 
				args.getInt(MainActivity.ARG_MENU_CHART_ITEM_IDX));
		}
		
		viewPager.setCurrentItem(currentPage);
		
		return view;
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(CURRENT_PAGE, viewPager.getCurrentItem());
		super.onSaveInstanceState(outState);
	}


	private ChartPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private void newViewPager(String chartName, int chartIdx){		
		if(viewPager == null){
			viewPager = (ViewPager)view.findViewById(R.id.chart_pager);
			pagerAdapter = new ChartPagerAdapter(getChildFragmentManager(), chartName, chartIdx);
			viewPager.setAdapter(pagerAdapter);
			viewPager.setOffscreenPageLimit(1);
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					Fragment item = pagerAdapter.getItem(position);
					if(item instanceof ChartFragment){
						ChartType ct = ((ChartFragment)item).getChartType(position);
						switch(ct){
						case COLUMN_CHART: 
							getActivity().getActionBar().setIcon(R.drawable.ic_barchart);
							break;
						case PIE_CHART:
							getActivity().getActionBar().setIcon(R.drawable.ic_piechart);
							break;							
						}
					}
				}				
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
				}				
				@Override
				public void onPageScrollStateChanged(int state) {					
				}
			});
		}else{
			pagerAdapter.setName(chartName);
			pagerAdapter.setId(chartIdx);
			pagerAdapter.notifyDataSetChanged();
		}
	}
}
