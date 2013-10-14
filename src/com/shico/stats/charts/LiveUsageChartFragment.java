package com.shico.stats.charts;

import java.util.List;

import org.achartengine.GraphicalView;

import com.shico.stats.charts.chartengine.ChartTitles;
import com.shico.stats.charts.chartengine.ChartType;
import com.shico.stats.charts.chartengine.ChartUtil;
import com.shico.stats.loaders.ChartDataLoader;

public class LiveUsageChartFragment extends ChartFragment {	
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
			getChartDataLoader().getTopViewInBatch("/viewbatch/"+EVENT_TYPE, currentFrom, currentTo, getLoadOptions());
			break;
		default:
			getChartDataLoader().getTopView("/view/"+EVENT_TYPE, currentFrom, currentTo, getLoadOptions());
		}		
	}

	@Override
	public String getLoadOptions(){
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
			return "viewers,"+currentChartOptions;
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return "duration,"+currentChartOptions;
		default:
			return currentChartOptions;
		}
	}
	
	@Override
	protected GraphicalView createChartView(List<List<String>> dataRows) {
		String dateLegend = currentFrom+" / "+currentTo;
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
			return ChartUtil.createGroupedBarChartViewForChannels(getActivity(), dataRows, 
					ChartDataLoader.viewersIdx, new ChartTitles("", "Number of Viewers", "TV Channels\n"+dateLegend));
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return ChartUtil.createGroupedBarChartViewForChannels(getActivity(), dataRows, 
					ChartDataLoader.durationIdx, new ChartTitles("", "Total Watched Hours", "TV Channels\n"+dateLegend));
		case THIRD_PAGE_WITH_PIE_CHART_VIEWERS:
			return ChartUtil.createPieChartView(getActivity(), dataRows, ChartDataLoader.viewersIdx, "TV Channels - Number of Viewers\n"+dateLegend);
		case FOURTH_PAGE_WITH_PIE_CHART_DURATION:
			return ChartUtil.createPieChartView(getActivity(), dataRows, ChartDataLoader.durationIdx, "TV Channels - Total Watched Hours\n"+dateLegend);
		}
		return null;
	}

	@Override
	protected ChartType getChartType(int pos) {
		switch(pos){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return ChartType.COLUMN_CHART;
		default:
			return ChartType.PIE_CHART;
		}
	}  	
		
}
