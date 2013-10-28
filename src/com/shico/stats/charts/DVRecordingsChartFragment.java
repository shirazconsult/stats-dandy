package com.shico.stats.charts;

import java.util.List;

import org.achartengine.GraphicalView;

import com.shico.stats.R;
import com.shico.stats.charts.chartengine.ChartTitles;
import com.shico.stats.charts.chartengine.ChartType;
import com.shico.stats.charts.chartengine.ChartUtil;
import com.shico.stats.loaders.ChartDataLoader;

public class DVRecordingsChartFragment extends ChartFragment {	
	private static final String EVENT_TYPE = "DvrUsage";
	protected static final int FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS = 0;
	protected static final int SECOND_PAGE_WITH_COLUMN_CHART_DURATION = 1;
	protected static final int THIRD_PAGE_WITH_PIE_CHART_VIEWERS = 2;	
	protected static final int FOURTH_PAGE_WITH_PIE_CHART_DURATION = 3;	
	
	@Override
	protected void loadChartData() {
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			getChartDataLoader().getTopViewInBatch(EVENT_TYPE, currentFrom, currentTo, getLoadOptions());
			break;
		default:
			getChartDataLoader().getTopView(EVENT_TYPE, currentFrom, currentTo, getLoadOptions());
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
		StringBuilder titleBuilder = new StringBuilder().
				append(getChartTitle()).append("\n").
				append(currentFrom).append(" / ").append(currentTo).append(" (").
				append(Character.toUpperCase(topBottomOption.charAt(0))+topBottomOption.substring(1)).
				append(" ").append(numberOption).append(")");
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
			return ChartUtil.createGroupedBarChartViewForChannels(getActivity(), dataRows, 
					ChartDataLoader.viewersIdx, 
					new ChartTitles("", getString(R.string.dvr_y_title1), titleBuilder.toString()));
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return ChartUtil.createGroupedBarChartViewForChannels(getActivity(), dataRows, 
					ChartDataLoader.durationIdx, 
					new ChartTitles("", getString(R.string.dvr_y_title2), titleBuilder.toString()));
		case THIRD_PAGE_WITH_PIE_CHART_VIEWERS:
			return ChartUtil.createPieChartView(getActivity(), dataRows, ChartDataLoader.viewersIdx, 
					titleBuilder.toString());
		case FOURTH_PAGE_WITH_PIE_CHART_DURATION:
			return ChartUtil.createPieChartView(getActivity(), dataRows, ChartDataLoader.durationIdx, 
					titleBuilder.toString());
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

	@Override
	protected String getChartTitle() {
		switch(viewpage){
		case FIRST_PAGE_WITH_COLUMN_CHART_VIEWERS:
		case SECOND_PAGE_WITH_COLUMN_CHART_DURATION:
			return getString(R.string.dvr_title_column_chart);
		case THIRD_PAGE_WITH_PIE_CHART_VIEWERS:
			return getString(R.string.dvr_title_pie_chart1);
		case FOURTH_PAGE_WITH_PIE_CHART_DURATION:
			return getString(R.string.dvr_title_pie_chart2);
		}
		return "";
	}  	

	@Override
	public String[] getCellDisplayStrings() {
		return new String[]{"", getString(R.string.dvr_cell_disp)};
	} 
	
}
