package com.shico.stats;

import java.util.List;

import org.achartengine.GraphicalView;

import com.shico.stats.chartengine.ChartTitles;
import com.shico.stats.chartengine.ChartUtil;
import com.shico.stats.loaders.ChartDataLoader;

public class LiveUsageChartFragment extends ChartFragment {	
	private static final String EVENT_TYPE = "LiveUsage";
	protected static final int FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS = 0;
	protected static final int SECOND_PAGE_WITH_COLUMN_CHART_DURATION = 1;
	protected static final int THIRD_PAGE_WITH_SIMPLE_COLUMN_CHART_VIEWERS = 2;
	protected static final int FOURTH_PAGE_WITH_PIE_CHART_VIEWERS = 3;	
	protected static final int FIFTH_PAGE_WITH_SIMPLE_COLUMN_CHART_DURATION = 4;
	protected static final int SIXTH_PAGE_WITH_PIE_CHART_DURATION = 5;	
	
	@Override
	protected void loadChartData() {
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			getChartDataLoader().getTopViewInBatch("/viewbatch/"+EVENT_TYPE, "2013-02", "2013-05", getLoadOptions());
			break;
		default:
			getChartDataLoader().getTopView("/view/"+EVENT_TYPE, "2013-02", "2013-05", getLoadOptions());
		}		
	}

	@Override
	public String getLoadOptions(){
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
			String options = currentChartOptions;
			if(currentChartOptions.startsWith("duration")){
				options = "viewers"+currentChartOptions.substring(currentChartOptions.indexOf(','));
			}
			return options;
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			options = currentChartOptions;
			if(currentChartOptions.startsWith("viewers")){
				options = "duration"+currentChartOptions.substring(currentChartOptions.indexOf(','));
			}
			return options;
		default:
			return currentChartOptions;
		}
	}
	
	@Override
	protected GraphicalView createChartView(List<List<String>> dataRows) {
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
			return ChartUtil.CreateGroupedBarChartView(getActivity(), dataRows, 
					ChartDataLoader.viewersIdx, new ChartTitles("Time", "Number of Viewers", "TV Channels"));
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return ChartUtil.CreateGroupedBarChartView(getActivity(), dataRows, 
					ChartDataLoader.durationIdx, new ChartTitles("Time", "Total Watched Hours", "TV Channels"));
		}
		return null;
	}  
	
}
