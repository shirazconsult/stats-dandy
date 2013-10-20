package com.shico.stats.util;

import com.shico.stats.R;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

public class MenuActionProvider extends ActionProvider {

	private Context context;
	public MenuActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.menu.menu, null);
		return view;
	}


}
