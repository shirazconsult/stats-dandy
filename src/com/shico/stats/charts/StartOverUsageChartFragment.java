package com.shico.stats.charts;

import java.util.List;

import org.achartengine.GraphicalView;

import com.shico.stats.R;
import com.shico.stats.charts.chartengine.ChartTitles;
import com.shico.stats.charts.chartengine.ChartType;
import com.shico.stats.charts.chartengine.ChartUtil;
import com.shico.stats.loaders.ChartDataLoader;

public class StartOverUsageChartFragment extends ChartFragment {	
	private static final String EVENT_TYPE = "STARTOVERUsage";	
	protected static final int FIRST_PAGE_WITH_GROUPED_COLUMN_CHART = 0;
	protected static final int SECOND_PAGE_WITH_PIE_CHART = 1;	
			
	@Override
	protected void loadChartData() {
		switch(viewpage){
		case FIRST_PAGE_WITH_GROUPED_COLUMN_CHART:
			getChartDataLoader().getTopViewInBatch(EVENT_TYPE, currentFrom, currentTo, getLoadOptions());
			break;
		default:
			getChartDataLoader().getTopView(EVENT_TYPE, currentFrom, currentTo, getLoadOptions());
		}		
	}

	@Override
	protected GraphicalView createChartView(List<List<String>> dataRows) {
		StringBuilder titleBuilder = new StringBuilder().
				append(currentFrom).append(" / ").append(currentTo).append(" (").
				append(Character.toUpperCase(topBottomOption.charAt(0))+topBottomOption.substring(1)).
				append(" ").append(numberOption).append(")");
		switch(viewpage){
		case FIRST_PAGE_WITH_GROUPED_COLUMN_CHART:
			return ChartUtil.createGroupedBarChartViewForChannels(getActivity(), dataRows, 
					ChartDataLoader.viewersIdx, 
					new ChartTitles("", getString(R.string.startover_y_title), getString(R.string.startover_title)+"\n"+titleBuilder.toString()));
		case SECOND_PAGE_WITH_PIE_CHART:
			return ChartUtil.createPieChartView(getActivity(), dataRows, ChartDataLoader.viewersIdx, 
					"Start Over Usage\n"+titleBuilder.toString());
		}
		return null;
	}	
	
	@Override
	protected ChartType getChartType(int pos) {
		switch(pos){
		case FIRST_PAGE_WITH_GROUPED_COLUMN_CHART:
			return ChartType.COLUMN_CHART;
		default:
			return ChartType.PIE_CHART;
		}
	}

	@Override
	protected String getChartTitle() {
		return getString(R.string.startover_title);
	} 
	
	@Override
	public String[] getCellDisplayStrings() {
		return new String[]{"", getString(R.string.startover_cell_disp)};
	} 	
	
}
