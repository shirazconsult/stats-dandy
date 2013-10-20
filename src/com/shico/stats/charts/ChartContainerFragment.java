package com.shico.stats.charts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shico.stats.MainActivity;
import com.shico.stats.R;
import com.shico.stats.adapters.ChartPagerAdapter;
import com.shico.stats.charts.chartengine.ChartType;

public class ChartContainerFragment extends Fragment {
	private final static String CURRENT_PAGE = "current.page";

	private View view;
	private int currentPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.chart_viewpager, container, false);

		Bundle args = getArguments();
		if (args != null) {
			newViewPager(args.getString(MainActivity.ARG_MENU_CHART_ITEM_NAME),
					args.getInt(MainActivity.ARG_MENU_CHART_ITEM_IDX));
		}

		if (savedInstanceState != null) {
			currentPage = savedInstanceState.getInt(CURRENT_PAGE);
			viewPager.setCurrentItem(currentPage);
			getActivity().getActionBar().setTitle(
					savedInstanceState.getCharSequence("actionbar.title"));
			setIcon(currentPage);
		} else {
			getActivity().getActionBar().setTitle(
					args.getString(MainActivity.ARG_MENU_CHART_ITEM_NAME));
			setIcon(0);
		}

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(CURRENT_PAGE, viewPager.getCurrentItem());
		outState.putCharSequence("actionbar.title", getActivity()
				.getActionBar().getTitle());

		super.onSaveInstanceState(outState);
	}

	private void setIcon(int page) {
		if (pagerAdapter != null) {
			Fragment item = pagerAdapter.getItem(page);
			if (item instanceof ChartFragment) {
				ChartType ct = ((ChartFragment) item).getChartType(page);
				switch (ct) {
				case COLUMN_CHART:
					getActivity().getActionBar()
							.setIcon(R.drawable.ic_barchart);
					break;
				case PIE_CHART:
					getActivity().getActionBar()
							.setIcon(R.drawable.ic_piechart);
					break;
				}
			}
		}
	}

	private ChartPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	private void newViewPager(String chartName, int chartIdx) {
		if (viewPager == null) {
			viewPager = (ViewPager) view.findViewById(R.id.chart_pager);
			viewPager.setPageTransformer(false, new DepthPageTransformer());
			pagerAdapter = new ChartPagerAdapter(getChildFragmentManager(),
					chartName, chartIdx);
			viewPager.setAdapter(pagerAdapter);
			viewPager.setOffscreenPageLimit(1);
			viewPager
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							setIcon(position);
						}

						@Override
						public void onPageScrolled(int position,
								float positionOffset, int positionOffsetPixels) {
						}

						@Override
						public void onPageScrollStateChanged(int state) {
						}
					});
		} else {
			pagerAdapter.setName(chartName);
			pagerAdapter.setId(chartIdx);
			pagerAdapter.notifyDataSetChanged();
		}
	}

	public void onBackPressed() {
		int page = viewPager.getCurrentItem();
		if (page >= 1) {
			viewPager.setCurrentItem(page - 1);
		}
	}
	
	public ChartFragment getCurrentChartFragment(){
		return viewPager != null && pagerAdapter != null ? (ChartFragment)pagerAdapter.getItem(viewPager.getCurrentItem()) : null;
	}
	
	public static class DepthPageTransformer implements ViewPager.PageTransformer {
	    private static float MIN_SCALE = 0.75f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 0) { // [-1,0]
	            // Use the default slide transition when moving to the left page
	            view.setAlpha(1);
	            view.setTranslationX(0);
	            view.setScaleX(1);
	            view.setScaleY(1);

	        } else if (position <= 1) { // (0,1]
	            // Fade the page out.
	            view.setAlpha(1 - position);

	            // Counteract the default slide transition
	            view.setTranslationX(pageWidth * -position);

	            // Scale the page down (between MIN_SCALE and 1)
	            float scaleFactor = MIN_SCALE
	                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
}
