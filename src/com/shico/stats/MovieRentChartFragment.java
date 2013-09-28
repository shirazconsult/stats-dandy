package com.shico.stats;

import java.util.List;

import org.achartengine.GraphicalView;

import com.shico.stats.chartengine.ChartTitles;
import com.shico.stats.chartengine.ChartUtil;
import com.shico.stats.loaders.ChartDataLoader;

public class MovieRentChartFragment extends ChartFragment {	
	private static final String EVENT_TYPE = "movieRent";
	protected static final int FIRST_PAGE_WITH_GROUPED_COLUMN_CHART = 0;
	protected static final int SECOND_PAGE_WITH_PIE_CHART = 1;

	protected void loadChartData() {
		switch(viewpage){
		case FIRST_PAGE_WITH_GROUPED_COLUMN_CHART:
			getChartDataLoader().getTopViewInBatch("/viewbatch/"+EVENT_TYPE, "2013-02", "2013-05", getLoadOptions());
			break;
		default:
			getChartDataLoader().getTopView("/view/"+EVENT_TYPE, "2013-02", "2013-05", getLoadOptions());
		}		
	}

	@Override
	protected GraphicalView createChartView(List<List<String>> dataRows) {
		switch(viewpage){
		case FIRST_PAGE_WITH_GROUPED_COLUMN_CHART:
			return ChartUtil.createGroupedBarChartViewForChannels(getActivity(), dataRows, 
					ChartDataLoader.viewersIdx, new ChartTitles("Time", "Number of Rentals", "Movie Rentals"));
		case SECOND_PAGE_WITH_PIE_CHART:
			return ChartUtil.createPieChartView(getActivity(), dataRows, ChartDataLoader.viewersIdx, "Movie Rentals");
		}
		return null;
	}

}
