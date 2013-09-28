package com.shico.stats.chartengine;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.text.style.TypefaceSpan;

import com.shico.stats.loaders.ChartDataLoader;

public class ChartUtil {
		
	public static GraphicalView CreateGroupedBarChartView(Context context, List<List<String>> rows, 
			int valueIdx, ChartTitles titles){
		GroupedData data = transform(rows, valueIdx);
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(data.getColors());
	    setChartSettings(renderer, titles.title, titles.xTitle, titles.yTitle, 0d,
	        data.allXLabels.size()+1, 0d, data.getMaxValue()+(data.getMaxValue()/3), Color.GRAY, Color.LTGRAY);
	    renderer.setDisplayValues(false);
	    renderer.setXLabels(data.allXLabels.size()+1);
	    renderer.setYLabels(Math.min(10, data.allTitles.size()));
	    double i=1;				
	    for(String label : data.allXLabels){
	    	renderer.addXTextLabel(i++, label);
	    }
	    renderer.setXLabelsAlign(Align.LEFT);
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setXLabelsAngle(45f);
	    renderer.setXLabelsPadding(10f);
	    renderer.setXLabels(0);
	    renderer.setMargins(new int[]{45,45,45,25});
	    renderer.setLegendHeight(100);
	    renderer.setZoomRate(1.1f);
	    // TODO: bar width and spacing must be a function of numOfTitles and numOfXLabels
	    renderer.setBarSpacing(0.2f);
	    renderer.setBarWidth(10f);
	    
	    return ChartFactory.getBarChartView(context, buildBarDataset(data), renderer, Type.DEFAULT);								
	}
	
	private static XYMultipleSeriesDataset buildBarDataset(GroupedData groupedData) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (String title : groupedData.allTitles) {
			CategorySeries series = new CategorySeries(title);
			for (String xLabel : groupedData.allXLabels) {
				series.add(groupedData.getValue(xLabel, title));
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	private static GroupedData transform(List<List<String>> rows, int valueIdx){
		GroupedData groupedData = new GroupedData(valueIdx);
		for (List<String> row : rows) {
			String time = row.get(ChartDataLoader.timeIdx);
			String name = row.get(ChartDataLoader.nameIdx);
			groupedData.addXLabel(time);
			groupedData.addTitle(name);
			groupedData.addValue(time, name, Double.valueOf(row.get(ChartDataLoader.viewersIdx)));
		}	
		return groupedData;
	}

	/**
	 * Builds a bar multiple series renderer to use the provided colors.
	 * 
	 * @param colors the series renderers colors
	 * @return the bar multiple series renderer
	 */
	private static XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		renderer.setAxisTitleTextSize(20);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	private static void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	
}
