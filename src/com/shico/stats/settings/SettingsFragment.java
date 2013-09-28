package com.shico.stats.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.shico.stats.R;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
