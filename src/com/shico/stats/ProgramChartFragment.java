package com.shico.stats;

import java.util.List;

import org.achartengine.GraphicalView;

import com.shico.stats.chartengine.ChartTitles;
import com.shico.stats.chartengine.ChartUtil;
import com.shico.stats.loaders.ChartDataLoader;

public class ProgramChartFragment extends ChartFragment {	
	private static final String EVENT_TYPE = "LiveUsage";
	protected static final int FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS = 0;
	protected static final int SECOND_PAGE_WITH_COLUMN_CHART_DURATION = 1;
	protected static final int THIRD_PAGE_WITH_PIE_CHART_VIEWERS = 2;	
	protected static final int FOURTH_PAGE_WITH_PIE_CHART_DURATION = 3;	
	
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
			return options+",title";
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			options = currentChartOptions;
			if(currentChartOptions.startsWith("viewers")){
				options = "duration"+currentChartOptions.substring(currentChartOptions.indexOf(','));
			}
			return options+",title";
		default:
			return currentChartOptions+",title";
		}
	}
	
	@Override
	protected GraphicalView createChartView(List<List<String>> dataRows) {
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
			return ChartUtil.createGroupedBarChartViewForPrograms(getActivity(), dataRows, 
					ChartDataLoader.viewersIdx, new ChartTitles("Time", "Number of Viewers", "TV Programs"));
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return ChartUtil.createGroupedBarChartViewForPrograms(getActivity(), dataRows, 
					ChartDataLoader.durationIdx, new ChartTitles("Time", "Total Watched Hours", "TV Programs"));
		case THIRD_PAGE_WITH_PIE_CHART_VIEWERS:
			return ChartUtil.createPieChartViewForPrograms(getActivity(), dataRows, ChartDataLoader.viewersIdx, "TV Programs - Number of Viewers");
		case FOURTH_PAGE_WITH_PIE_CHART_DURATION:
			return ChartUtil.createPieChartViewForPrograms(getActivity(), dataRows, ChartDataLoader.durationIdx, "TV Programs - Total Watched Hours");
		}
		return null;
	}  
	
}
