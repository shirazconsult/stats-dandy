package com.shico.stats.adapters;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shico.stats.R;
import com.shico.stats.charts.ChartFragment;
import com.shico.stats.loaders.ChartDataLoader;

public class GrouppedDataListAdapter extends ArrayAdapter<List<String>> {
	private static final NumberFormat numberFormatter = NumberFormat.getInstance();
	
	private Context context;
	private List<List<String>> data;
	private ChartFragment chartFragment;
	
	public GrouppedDataListAdapter(Context context,	List<List<String>> rows, ChartFragment chartFragment) {
		super(context, R.layout.liveusage_groupped_data_list, rows);
		this.context = context;
		this.data = rows;
		this.chartFragment = chartFragment;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		String chart = chartFragment.getCurrentChartName();
		
		View rowView = null;
		TextView duration = null;
		if(chart.equalsIgnoreCase("channels") || 
			chart.equalsIgnoreCase("programs")){
			rowView = inflater.inflate(R.layout.liveusage_groupped_data_list, parent, false);
			duration = (TextView)rowView.findViewById(R.id.duration);
			long hours = Long.parseLong(data.get(position).get(ChartDataLoader.durationIdx))/3600000;
			duration.setText(numberFormatter.format(hours)+" hours of total time");
		}else{
			rowView = inflater.inflate(R.layout.groupped_data_list, parent, false);
		}
		
		TextView evName = (TextView)rowView.findViewById(R.id.event_name);
		TextView evTime = (TextView)rowView.findViewById(R.id.event_time);
		TextView viewers = (TextView)rowView.findViewById(R.id.viewers);
		
		if(chart.equalsIgnoreCase("programs")){
			evName.setText((String)data.get(position).get(ChartDataLoader.titleIdx));
		}else{			
			evName.setText((String)data.get(position).get(ChartDataLoader.nameIdx));
		}
		evTime.setText((String)data.get(position).get(ChartDataLoader.timeIdx));
		int numOfViewers = Integer.parseInt(data.get(position).get(ChartDataLoader.viewersIdx));
		viewers.setText(numberFormatter.format(numOfViewers)+" viewers");
		
		if(position % 2 == 0){
			rowView.setBackgroundColor(context.getResources().getColor(R.color.DarkGray));
		}
		if(chart.equalsIgnoreCase("channels")){
			if(chartFragment.getViewpage() == 0){
				viewers.setTypeface(viewers.getTypeface(), Typeface.BOLD);
			}else if(chartFragment.getViewpage() == 1){
				duration.setTypeface(viewers.getTypeface(), Typeface.BOLD);
			}
		}else{
			viewers.setTypeface(viewers.getTypeface(), Typeface.BOLD);
		}
		return rowView;
	}
}
