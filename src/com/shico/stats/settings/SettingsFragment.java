package com.shico.stats.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shico.stats.R;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		v.setPadding(10, 30, 0, 0);
		v.setBackgroundColor(getResources().getColor(R.color.OldLace));
		
		getActivity().getActionBar().setTitle(R.string.settings_title);
		getActivity().getActionBar().setIcon(R.drawable.ic_settings);
		
		return v;
	}
	
}
