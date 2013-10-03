package com.shico.stats;

import java.util.List;

import org.achartengine.GraphicalView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.shico.stats.adapters.GrouppedDataListAdapter;
import com.shico.stats.event.ChartEvent;
import com.shico.stats.loaders.ChartDataLoader;
import com.shico.stats.settings.ChartSettings;
import com.shico.stats.util.MyGestureDetectorListener;

public abstract class ChartFragment extends Fragment implements OnSharedPreferenceChangeListener{
	public static final String ARG_CHART_ID = "chart_id";
	public static final String ARG_CHART_URL = "chart_url";
	public final static String ARG_CHART_VIEWPAGE = "chart.viewpage";
	public static final String CHART_NAME = "chart_name";
	private static final String CHART_OPTIONS = "chart_options";
	
	public static final String FROM_DATE_SUFFIX = ".from";
	public static final String TO_DATE_SUFFIX = ".to";
	public static final String DATE_SPEC_SUFFIX = ".dateSpec";
	public static final String NUMBER_SUFFIX = ".number";
	public static final String TOP_BOT_SUFFIX = ".topBot";
	
	protected ListView thisListView;
	protected LinearLayout chartview;
	protected String currentChartName;
	protected String currentChartOptions;
	protected String currentFrom;
	protected String currentTo;
	private ProgressDialog progressDiag;
	
	protected int viewpage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			currentChartName = savedInstanceState.getString(CHART_NAME);
			currentChartOptions = savedInstanceState.getString(CHART_OPTIONS);
			viewpage = savedInstanceState.getInt(ARG_CHART_VIEWPAGE);
			
			getChartDataLoader().onRestoreInstanceState(savedInstanceState);
		}		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(CHART_NAME, currentChartName);
		outState.putString(CHART_OPTIONS, currentChartOptions);
		outState.putInt(ARG_CHART_VIEWPAGE, viewpage);
		
		getChartDataLoader().onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.chartview, container, false);
		
		progressDiag = new ProgressDialog(getActivity(), ProgressDialog.STYLE_HORIZONTAL); 
		progressDiag.setMessage("Loading chart library ...");
		progressDiag.show();

		thisListView = (ListView) v.findViewById(R.id.groupped_data_list);
		chartview = (LinearLayout)v.findViewById(R.id.chartview);
		
//		setGestureListener(v, chartview);
		
		currentChartName = getArguments().getString(MainActivity.ARG_MENU_CHART_ITEM_NAME);
		viewpage = getArguments().getInt(ARG_CHART_VIEWPAGE);
		updateCurrentChartOptions(PreferenceManager.getDefaultSharedPreferences(getActivity()));
		if(currentChartName != null){
			loadChartView();
		}
		
		return v;
	}
	
	protected ChartDataLoader chartDataLoader; 
	protected ChartDataLoader getChartDataLoader() {
		if(chartDataLoader == null){
			chartDataLoader = new ChartDataLoader(getActivity(), new MyCallback());
		}
		return chartDataLoader;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// register preference change Listener
		PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		// unregister preference change Listener
		PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);

	}

	private ChartSettings chartSettingsDialog;
	private void showSettings(){
		if(chartSettingsDialog == null){
			chartSettingsDialog = new ChartSettings();
		}
		Bundle args = new Bundle();
		args.putString(CHART_NAME, currentChartName);
		chartSettingsDialog.setArguments(args);
		chartSettingsDialog.show(getFragmentManager(), "test");
	}
	
	private GrouppedDataListAdapter lvAdapter;
	private void displayDataList(Context context){
		lvAdapter = new GrouppedDataListAdapter(getActivity(), getChartDataLoader().getCurrentRows(), this);
		thisListView.setAdapter(lvAdapter);
	}
		
	private boolean isPortrait(){
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	public String getCurrentChartName() {
		return currentChartName;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(key.startsWith(currentChartName)){
			updateCurrentChartOptions(sharedPreferences);
			loadChartView();
			return;
		}
		if(key.equals("host") || key.equals("port")){
			String host = sharedPreferences.getString("host", "localhost");
			int port = sharedPreferences.getInt("port", 9119);
			if(getChartDataLoader() != null){
				getChartDataLoader().setHost(host);
				getChartDataLoader().setPort(port);
			}
		}
	}
	
	private String updateCurrentChartOptions(SharedPreferences prefs){
		String topOrBottom = prefs.getString(currentChartName+TOP_BOT_SUFFIX, "top");
		int num = prefs.getInt(currentChartName+NUMBER_SUFFIX, 5);
		
		currentChartOptions = new StringBuilder(topOrBottom.equals("bottom") ? "low" : "top").
				append(",").append(num).toString();
		
		String[] dates = ChartSettings.getDates(prefs, currentChartName);
		currentFrom = dates[0];
		currentTo = dates[1];
		Log.d("WebViewFragment", viewpage+": Updating current chart options to: "+
				currentChartOptions+" -- date-range is "+currentFrom+" / "+currentTo);
		return currentChartOptions;
	}
	
	private void loadChartView(){
		loadChartData();
	}
	
	// Methods for supporting ViewPager functionality
	// ----------------------------------------------
	protected abstract void loadChartData();
	protected abstract GraphicalView createChartView(List<List<String>> dataRows);
	
	protected String getLoadOptions(){
		return "viewers,"+currentChartOptions;
	}
	
	protected boolean match(Intent intent) {
		return (Integer)intent.getExtras().get(ChartEvent.DATA_LOADER_ID) == viewpage && 
			((String)intent.getExtras().get(ChartEvent.DATA_LOAD_OPTIONS)).equals(getLoadOptions());
	}

	// Gesture listener
	private void setGestureListener(View... views) {
		MyGestureDetectorListener.DownSwipe callback = new MyGestureDetectorListener.DownSwipe() {			
			@Override
			public void onDown() {
				showSettings();
			}
		};
	
		final GestureDetector gesture = new GestureDetector(getActivity(), new MyGestureDetectorListener(callback));

		for (int i = 0; i < views.length; i++) {			
			views[i].setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return gesture.onTouchEvent(event);
				}
			});	
		}
	}

	public class MyCallback implements ChartDataLoader.Callback {
		@Override
		public void success(List<List<String>> result) {
			try{
				if(progressDiag != null){
					progressDiag.setMessage("Drawing chart...");
				}

				// chart
				GraphicalView view = createChartView(result);
				chartview.removeAllViews();
				chartview.addView(view);
				setGestureListener(view);
				
				// table
				if(isPortrait()){
					displayDataList(getActivity());
				}
			}finally{
				progressDiag.dismiss();
			}		
			if(result.isEmpty()){
				Toast.makeText(getActivity(), "No data is returned from server.", Toast.LENGTH_LONG).show();
			}				
		}

		@Override
		public void failure(Exception ex) {
			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
		}	
	}

	public int getViewpage() {
		return viewpage;
	}
}
