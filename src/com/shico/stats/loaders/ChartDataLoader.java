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
	private Callback callback;
	
	private List<List<String>> currentRows;

	public ChartDataLoader(Context context, Callback callback) {
		this.context = context;
		this.callback = callback;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		host = prefs.getString("host", "10.0.2.2");
		port = Integer.parseInt(prefs.getString("port", "9119"));
	}
	
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CHART_CACHE, (Serializable)getTemporaryCache());
	}
	public void onRestoreInstanceState(Bundle inState){
		setTemporaryCache((ConcurrentMap<String, List<List<String>>>) inState.get(CHART_CACHE));
	}
		
	public void getTopView(String restCmd, String from, String to, String options){
		String url = new StringBuilder(getBaseUrl()).
				append(restCmd).
				append("/").append(from).
				append("/").append(to).
				append("/").append(options).
				toString();
		
		List<List<String>> cached = getTemporaryCache().get(url);
		if(cached == null){
			Log.d("ChartDataLoader", "Loading data from server: "+url);
			loadChartTopViewData(url);
		}else{
			currentRows = getTemporaryCache().get(url);
			Log.d("ChartDataLoader", "Loading data from cache: "+url);
			callback.success(currentRows);
		}
	}

	public void getTopViewInBatch(String restCmd, String from, String to, String options){
		String url = new StringBuilder(getBaseUrl()).
				append(restCmd).
				append("/").append(from).
				append("/").append(to).
				append("/").append(options).
				toString();
		
		List<List<String>> cached = getTemporaryCache().get(url);
		if(cached == null){
			Log.d("ChartDataLoader", "Loading data from server: "+url);
			loadChartTopViewDataInBatch(url);
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
		});
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

}
