package com.shico.stats.loaders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shico.stats.settings.Timeunit;

public class ChartDataLoader {
	private static final String CHART_CACHE = "chart_cache";
	
	// data index constants
	public final static int typeIdx = 0; 
	public final static int nameIdx = 1; 
	public final static int titleIdx = 2;
	public final static int viewersIdx = 3;
	public final static int durationIdx = 4;
	public final static int fromIdx = 5;
	public final static int timeIdx = 5;
	public final static int toIdx = 6;
	public static String[] viewColumns = {"type", "name", "title", "viewers", "duration", "fromTS", "toTS"};
	public static String[] topViewColumnNames = {"type", "name", "title", "viewers", "duration", "time"};

	private static final String REST_PATH = "/statistics/rest/stats";
	private static AsyncHttpClient client = new AsyncHttpClient();
	protected static JSONArray topViewColumns;
	protected Context context;
	private String baseUrl;
	private String host;
	private int port;
	private int timeout;
	private Callback callback;
	
	private List<List<String>> currentRows;

	public ChartDataLoader(Context context, Callback callback) {
		this.context = context;
		this.callback = callback;
	}
	
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CHART_CACHE, (Serializable)getTemporaryCache());
	}
	public void onRestoreInstanceState(Bundle inState){
		setTemporaryCache((ConcurrentMap<String, List<List<String>>>) inState.get(CHART_CACHE));
	}
		
	public void getTopView(String eventType, String from, String to, String options){
		String url = new StringBuilder(getBaseUrl()).
				append(from.equals(to) ? "/viewontime/": "/view/").
				append(eventType).
				append("/").append(from).
				append(from.equals(to) ? "" : "/"+to).
				append("/").append(correctChartOptionsForTopviewInvokation(options)).
				toString();
		
		List<List<String>> cached = getTemporaryCache().get(url);
		if(cached == null){
			Log.d("ChartDataLoader", "Loading data from server: "+url);
			try{
				loadChartTopViewData(url);
			}catch(Throwable t){
				Log.e("ChartDataLoader", "****** Client error ****");				
			}
		}else{
			currentRows = getTemporaryCache().get(url);
			Log.d("ChartDataLoader", "Loading data from cache: "+url);
			callback.success(currentRows);
		}
	}

	public void getTopViewInBatch(String eventType, String from, String to, String options){
		String url = new StringBuilder(getBaseUrl()).
				append("/viewbatch/").
				append(eventType).
				append("/").append(from).
				append(from.equals(to) ? "" : "/"+to).
				append("/").append(options).
				toString();
		
		List<List<String>> cached = getTemporaryCache().get(url);
		if(cached == null){
			Log.d("ChartDataLoader", "Loading data from server: "+url);
			try{
				loadChartTopViewDataInBatch(url);
			}catch(Throwable t){
				Log.e("ChartDataLoader", "****** Client error ****");
			}
		}else{
			currentRows = getTemporaryCache().get(url);
			Log.d("ChartDataLoader", "Loading data from cache: "+url);
			callback.success(currentRows);
		}
	}

	public void loadChartTopViewDataInBatch(final String url){
		client.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject res) {
				try {
					ArrayList<List<String>> recs = new ArrayList<List<String>>();

					JSONArray rows = res.getJSONArray("result");
					for(int i=0; i< rows.length(); i++){
						JSONArray row = rows.getJSONObject(i).getJSONArray("rows");
						for (int j=0; j<row.length(); j++){
							JSONArray r = row.getJSONObject(j).getJSONArray("result");
							List<String> rec = new ArrayList<String>();
							for(int k=0; k<r.length(); k++){
								rec.add(r.getString(k));
							}
							recs.add(rec);
						}
					}
					currentRows = recs;
					getTemporaryCache().put(url, currentRows);
					callback.success(currentRows);
				} catch (JSONException e) {
					Log.e("JsonHttpResponseHandler", "Failed to deserialize response for "+url);
					callback.failure(new RuntimeException("Failed to deserialize response. "+e.getMessage()));
					return;
				}
			}

			@Override
			public void onFailure(Throwable t, JSONObject arg1) {
				Log.e("ChartDataLoader", "Failed to retrieve data for "+url+". "+(t != null ? t.getMessage() : ""));
				callback.failure(new RuntimeException("Failed to retrieve data. "+t.getMessage()));
			}
			
			@Override
			public void onFailure(Throwable t, String content) {
				String msg = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				Log.e("ChartDataLoader", "Connection Failure. "+msg);
				callback.failure(new RuntimeException("Connection Failure. "+msg));
			}			
			
		});		
	}
	
	public void loadChartTopViewData(final String url){
		client.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject res) {
				try {
					ArrayList<List<String>> recs = new ArrayList<List<String>>();

					JSONArray rows = res.getJSONArray("rows");					
					for(int i=0; i< rows.length(); i++){
						JSONArray row = rows.getJSONObject(i).getJSONArray("result");
						List<String> rec = new ArrayList<String>();
						for(int j=0; j<row.length(); j++){
							rec.add(row.getString(j));
						}
						recs.add(rec);
					}
					currentRows = recs;
					getTemporaryCache().put(url, currentRows);
					callback.success(currentRows);
				} catch (JSONException e) {
					callback.failure(new RuntimeException("Failed to deserialize response. "+e.getMessage()));
					return;
				}
			}

			@Override
			public void onFailure(Throwable t, JSONObject arg1) {
				Log.e("ChartDataLoader", "Failed to retrieve data for "+url+". "+(t != null ? t.getMessage() : ""));
				callback.failure(new RuntimeException("Failed to retrieve data. "+t.getMessage()));
			}
			
			@Override
			public void onFailure(Throwable t, String content) {
				String msg = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				Log.e("ChartDataLoader", "Connection Failure. "+msg);
				callback.failure(new RuntimeException("Connection Failure. "+msg));
			}			
		});
	}
		
	public String toXml(List<List<String>> result, String spec){
		if(result == null){
			return "<data spec=\""+spec+"\"></data>";
		}
		StringBuilder res = new StringBuilder().append("<data spec=\""+spec+"\">");
		for (List<String> row : result) {
			res.append("\n    <row>");
			int idx = 0;
			for (String elem : row) {
				String col = ChartDataLoader.topViewColumnNames[idx++];
				res.append("\n        <").append(col).append(">").
				append(elem == null ? "" : elem).
				append("</").append(col).append(">");
			}
			res.append("\n    </row>");
		}
		res.append("\n</data>");
		return res.toString();
	}
	
	private final String getBaseUrl() {
		if(baseUrl == null){
			baseUrl = new StringBuilder("http://").append(host).append(":").append(port).append(REST_PATH).toString();
		}
		return baseUrl;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}	
	
	private ConcurrentMap<String, List<List<String>>> temporaryCache;
	
	public void setTemporaryCache(ConcurrentMap<String, List<List<String>>> temporaryCache){
		this.temporaryCache = temporaryCache;
	}
	
	public Map<String, List<List<String>>> getTemporaryCache(){
		if(temporaryCache == null){
			temporaryCache = new ConcurrentHashMap<String, List<List<String>>>();
		}
		return temporaryCache;
	}

	public List<List<String>> getCurrentRows() {
		return currentRows;
	}	
	
	public interface Callback{
		public void success(List<List<String>> result);
		public void failure(Exception ex);
	}

	public void setupHostAndPort(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		host = prefs.getString("host", "10.0.2.2");
		port = Integer.parseInt(prefs.getString("port", "9119"));		
		timeout = Integer.parseInt(prefs.getString("connectionTimeout", "5"));
		
		baseUrl = new StringBuilder("http://").append(host).append(":").append(port).append(REST_PATH).toString();
		client.setTimeout(timeout * 1000);
	}
	
	// ChartSettings dialog sets the timeunit prefs. from the viewpoint of bar-charts in which we divide the entire
	// tiem period (form-to) into smaller intervals. For pie-charts where we show the totals, we need to use the 
	// timeunit for the entire date-interval (from-to), why we here modify the options.
	private String correctChartOptionsForTopviewInvokation(String options){
		String newOpts = options;
		if(options.contains(Timeunit.hourly.name())){
			newOpts = options.replace(Timeunit.hourly.name(), Timeunit.daily.name());
		}else if(options.contains(Timeunit.daily.name())){
			newOpts = options.replace(Timeunit.daily.name(), Timeunit.weekly.name());
		}else if(options.contains(Timeunit.weekly.name())){
			newOpts = options.replace(Timeunit.weekly.name(), Timeunit.monthly.name());
		}else if(options.contains(Timeunit.monthly.name())){
			newOpts = options.replace(Timeunit.monthly.name(), Timeunit.yearly.name());
		}
		return newOpts;
	}
}
