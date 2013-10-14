package com.shico.stats.settings;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shico.stats.R;
import com.shico.stats.charts.ChartFragment;

public class ChartSettings extends DialogFragment {
	public final static Boolean UNIFIED_CHART_SETTINGS = true;
	public final static String DEFAULT_DATE_SPEC = "chooseDate";
	
	private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	
	private View view;
	private LayoutInflater inflater;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogSlideAnim);
		inflater = getActivity().getLayoutInflater();
		
		view = inflater.inflate(R.layout.chart_settings, null);
		
		Spinner optSpinner = (Spinner)view.findViewById(R.id.dataload_options_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dataload_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		optSpinner.setAdapter(adapter);

		Spinner chooseDateSpinner = (Spinner)view.findViewById(R.id.choose_date_spinner);
		ArrayAdapter<CharSequence> chooseDateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.choose_date, android.R.layout.simple_spinner_item);
		chooseDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chooseDateSpinner.setAdapter(chooseDateAdapter);
		
		setupMonthChooser(view);

		chooseDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				switch(position){
				case 0:
					setEnabledMonthChooser(true);
					break;
				case 1:
					setEnabledMonthChooser(false);
					// current day
					break;
				case 2:
					setEnabledMonthChooser(false);
					// current week
					break;
				case 3: 
					setEnabledMonthChooser(false);
					// current month
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Nothing
			}
		});


		builder.setView(view).
			setPositiveButton(R.string.fetch, new OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Bundle arguments = getArguments();
					String chartName = arguments.getString(ChartFragment.CHART_NAME);					
					SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
					
					View thisView = ChartSettings.this.view;
					
					// pref keys
					String numPref = UNIFIED_CHART_SETTINGS ? ChartPref.number.name() : chartName + "." + ChartPref.number.name();
					String posPref = UNIFIED_CHART_SETTINGS ? ChartPref.position.name() : chartName + "." + ChartPref.position.name();
					String dateSpecPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_spec.name() : chartName + "." + ChartPref.date_spec.name();
					String dateFromPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_from.name() : chartName + "." + ChartPref.date_from.name();
					String dateToPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_to.name() : chartName + "." + ChartPref.date_to.name();
					
					Spinner optSpinner = (Spinner)thisView.findViewById(R.id.dataload_options_spinner);
					int selected = optSpinner.getSelectedItemPosition();
					int num = 3;
					String score = "top";
					switch(selected){
					case 1: 
						num = 5;
						break;
					case 2:
						num = 10;
						break;
					case 3:
						score = "bottom";
						break;
					case 4:
						num = 5;
						score = "bottom";
						break;
					case 5:
						num = 10;
						score = "bottom";
						break;
					}
					editor.putInt(numPref, num);
					editor.putString(posPref, score);
					
					Spinner chooser = (Spinner)thisView.findViewById(R.id.choose_date_spinner);
					int selectedIdx = chooser.getSelectedItemPosition();
					switch(selectedIdx){
					case 0:
						// Take the date from month-chooser
						Spinner fromMonth = (Spinner)thisView.findViewById(R.id.from_month_spinner);
						Spinner fromYear = (Spinner)thisView.findViewById(R.id.from_year_spinner);
						editor.putString(dateFromPref, fromYear.getSelectedItem()+"-"+(fromMonth.getSelectedItemPosition()+1));

						Spinner toMonth = (Spinner)thisView.findViewById(R.id.to_month_spinner);
						Spinner toYear = (Spinner)thisView.findViewById(R.id.to_year_spinner);
						editor.putString(dateToPref, toYear.getSelectedItem()+"-"+(toMonth.getSelectedItemPosition()+1));

						editor.putString(dateSpecPref, DEFAULT_DATE_SPEC);
						break;
					case 1:
						// current day
						editor.putString(dateSpecPref, "currentDay");
						break;
					case 2:
						// current week
						editor.putString(dateSpecPref, "currentWeek");
						break;
					case 3:
						// current month
						editor.putString(dateSpecPref, "currentMonth");
						break;
					case 4:
						// current month
						editor.putString(dateSpecPref, "currentYear");
						break;						
					}
					
					editor.commit();
				}
			}).
			setNegativeButton(R.string.cancel, new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		
		AlertDialog dialog = builder.create();
		LayoutParams params = dialog.getWindow().getAttributes();
		params.gravity = Gravity.TOP | Gravity.CENTER;
		
		return dialog;
	}

	private void setEnabledMonthChooser(boolean enabled) {
		Spinner fromYearSpinner = (Spinner)view.findViewById(R.id.from_year_spinner);
		Spinner fromMonthSpinner = (Spinner)view.findViewById(R.id.from_month_spinner);
		Spinner toYearSpinner = (Spinner)view.findViewById(R.id.to_year_spinner);
		Spinner toMonthSpinner = (Spinner)view.findViewById(R.id.to_month_spinner);			
	
		fromYearSpinner.setEnabled(enabled);
		fromMonthSpinner.setEnabled(enabled);
		toYearSpinner.setEnabled(enabled);
		toMonthSpinner.setEnabled(enabled);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle arguments = getArguments();
		String chartName = arguments.getString(ChartFragment.CHART_NAME);
		readChartPreferences(view, chartName);
	}


	private void readChartPreferences(View view, String chartName){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// pref keys
		String numPref = UNIFIED_CHART_SETTINGS ? ChartPref.number.name() : chartName + "." + ChartPref.number.name();
		String posPref = UNIFIED_CHART_SETTINGS ? ChartPref.position.name() : chartName + "." + ChartPref.position.name();
		String dateSpecPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_spec.name() : chartName + "." + ChartPref.date_spec.name();

		Spinner optSpinner = (Spinner)view.findViewById(R.id.dataload_options_spinner);
		int num = prefs.getInt(numPref, 3);
		String topBot = prefs.getString(posPref, "top");		
		switch(num){
		case 3:
			if(topBot.equals("top")){
				optSpinner.setSelection(0);
			}else{
				optSpinner.setSelection(3);
			}
			break;
		case 5:
			if(topBot.equals("top")){
				optSpinner.setSelection(1);
			}else{
				optSpinner.setSelection(4);
			}
			break;
		case 10:
			if(topBot.equals("top")){
				optSpinner.setSelection(2);
			}else{
				optSpinner.setSelection(5);
			}
			break;
		}
		
		// Initialize date spinners		
		String dateSpec = prefs.getString(dateSpecPref, DEFAULT_DATE_SPEC);
		Spinner chooser = (Spinner)view.findViewById(R.id.choose_date_spinner);
		if(dateSpec.equals(DEFAULT_DATE_SPEC)){
			chooser.setSelection(0);
			setChooserDateSpinners(prefs, view, chartName, true);
			setChooserDateSpinners(prefs, view, chartName, false);
		}else{
			if(dateSpec.equals("currentWeek")){
				chooser.setSelection(2);
			}else if(dateSpec.equals("currentMonth")){
				chooser.setSelection(3);
			}else if(dateSpec.equals("currentYear")){
				chooser.setSelection(4);
			}else{
				// currentDay
				chooser.setSelection(1);
			}
		}
	}
	
	private void setChooserDateSpinners(SharedPreferences prefs, View view, String chartName, boolean from){
		int year = calendar.get(Calendar.YEAR);
		
		String dateFromPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_from.name() : chartName + "." + ChartPref.date_from.name();
		String dateToPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_to.name() : chartName + "." + ChartPref.date_to.name();

		String date;
		Spinner yearSpinner, monthSpinner;
		if(from){
			date = prefs.getString(dateFromPref, null);
			yearSpinner = (Spinner)view.findViewById(R.id.from_year_spinner);
			monthSpinner = (Spinner)view.findViewById(R.id.from_month_spinner);
		}else{
			date = prefs.getString(dateToPref, null);
			yearSpinner = (Spinner)view.findViewById(R.id.to_year_spinner);
			monthSpinner = (Spinner)view.findViewById(R.id.to_month_spinner);			
		}
		if(date == null){
			yearSpinner.setSelection(0);
			monthSpinner.setSelection(0);
		}else{
			String[] split = date.split("-");
			if(split[0].equals(String.valueOf(year))){
				yearSpinner.setSelection(0);
			}else{
				yearSpinner.setSelection(1);
			}
			monthSpinner.setSelection(Integer.parseInt(split[1])-1);
		}
	}
	
	private void setupMonthChooser(View parentView){
		Spinner fromMonthSpinner = (Spinner)view.findViewById(R.id.from_month_spinner);
		ArrayAdapter<CharSequence> fromMonthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.months, android.R.layout.simple_spinner_item);
		fromMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromMonthSpinner.setAdapter(fromMonthAdapter);

		Spinner toMonthSpinner = (Spinner)view.findViewById(R.id.to_month_spinner);
		ArrayAdapter<CharSequence> toMonthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.months, android.R.layout.simple_spinner_item);
		toMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toMonthSpinner.setAdapter(toMonthAdapter);

		Spinner fromYearSpinner = (Spinner)view.findViewById(R.id.from_year_spinner);
		int year = calendar.get(Calendar.YEAR);
		int prevYear = year-1;
		ArrayAdapter<CharSequence> fromYearAdapter = 
				new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, new String[]{String.valueOf(year), String.valueOf(prevYear)});
		fromYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromYearSpinner.setAdapter(fromYearAdapter);	
		
		Spinner toYearSpinner = (Spinner)view.findViewById(R.id.to_year_spinner);
		ArrayAdapter<CharSequence> toYearAdapter = 
				new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, new String[]{String.valueOf(year), String.valueOf(prevYear)});
		toYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toYearSpinner.setAdapter(toYearAdapter);		
	}	
	
	public static String getOptions(SharedPreferences prefs, String chart){
		// pref keys
		String numPref = UNIFIED_CHART_SETTINGS ? ChartPref.number.name() : chart + "." + ChartPref.number.name();
		String posPref = UNIFIED_CHART_SETTINGS ? ChartPref.position.name() : chart + "." + ChartPref.position.name();

		int num = prefs.getInt(numPref, 3);
		String topBot = prefs.getString(posPref, "top");		
		return topBot+","+num;
	}
	
	public static String[] getDates(SharedPreferences prefs, String chart){
		String dateSpecPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_spec.name() : chart + "." + ChartPref.date_spec.name();
		String dateFromPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_from.name() : chart + "." + ChartPref.date_from.name();
		String dateToPref = UNIFIED_CHART_SETTINGS ? ChartPref.date_to.name() : chart + "." + ChartPref.date_to.name();

		String dateSpec = prefs.getString(dateSpecPref, "currentDay");
		if(dateSpec.equals(DEFAULT_DATE_SPEC)){
			String from = prefs.getString(dateFromPref, "-");
			String to = prefs.getString(dateToPref, "-");
			return new String[]{from, to};
		}else{
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			 // months in java calendar starts with 0, but in ISO8601 they start with 1, why we add one to the months before we return to the views
			StringBuilder sb = new StringBuilder().append(year).append("-").append(month+1);  
			if(dateSpec.equals("currentWeek")){
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				String to = new StringBuilder(sb).append("-").append(dayOfMonth).toString();
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth-cal.get(Calendar.DAY_OF_WEEK)+2);
				String from = new StringBuilder().append(year).append("-").
						append(calendar.get(Calendar.MONTH)).append("-").
						append(cal.get(Calendar.DAY_OF_MONTH)).toString();
				return new String[]{from, to};				
			}else if(dateSpec.equals("currentMonth")){
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				int fromW = cal.get(Calendar.WEEK_OF_YEAR);
				int toW = calendar.get(Calendar.WEEK_OF_YEAR);
				if(fromW == toW){  // the same as current week
					String from = new StringBuilder(sb).append("-").append("1").toString();
					String to = sb.append("-").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
					return new String[]{from, to};
				}
				return new String[]{year+"-W"+fromW, year+"-W"+toW};
			}else if(dateSpec.equals("currentYear")){
				return new String[]{year+"-01", sb.toString()};
			}
			// otherwise we assume currentDay
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			String daySt = sb.toString()+"-"+day+"T";
			return new String[]{daySt+"00Z", daySt+hour+"Z"};
		}
	}	
	
	public static String getChartPrefName(ChartPref key, String chartName){
		return UNIFIED_CHART_SETTINGS ? key.name() : chartName + "." + key;
	}
}
