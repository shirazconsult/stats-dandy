package com.shico.stats;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpFragment extends Fragment implements OnClickListener {
	public static final String ARG_TEXT_ID = "ARG_TEXT_ID";
	public static final String ARG_TITLE = "ARG_TITLE";
	public static final String ARG_ICON_ID = "ARG_ICON_ID";
	public static final String HELP_TOPIC_FRAGMENT_TAG = "help.topic.fragment";
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help, container, false);
		
		getActivity().getActionBar().setTitle(R.string.help_title);
		getActivity().getActionBar().setIcon(R.drawable.ic_help);

		((ImageButton)view.findViewById(R.id.help_chart_btn)).setOnClickListener(this);
		((ImageButton)view.findViewById(R.id.help_pie_btn)).setOnClickListener(this);
		((ImageButton)view.findViewById(R.id.help_settings_btn)).setOnClickListener(this);
		

		TextView v1 = (TextView)view.findViewById(R.id.help_page_intro);
		TextView v2 = (TextView)view.findViewById(R.id.barchart_help_intro);
		TextView v3 = (TextView)view.findViewById(R.id.piechart_help_intro);
		TextView v4 = (TextView)view.findViewById(R.id.settings_help_intro);
		
		v1.setMovementMethod(LinkMovementMethod.getInstance());
		v2.setMovementMethod(LinkMovementMethod.getInstance());
		v3.setMovementMethod(LinkMovementMethod.getInstance());
		v4.setMovementMethod(LinkMovementMethod.getInstance());
		
		v1.setText(Html.fromHtml(getString(R.string.help_page_intro)));
		v2.setText(Html.fromHtml(getString(R.string.barchart_help_intro)));
		v3.setText(Html.fromHtml(getString(R.string.piechart_help_intro)));
		v4.setText(Html.fromHtml(getString(R.string.settings_help_intro)));

		setHasOptionsMenu(true);
		
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.removeItem(R.id.menu_item_share);
		menu.removeItem(R.id.menu_item_search);
	}

	@Override
	public void onClick(View v) {
		String title = "";
		int textId = -1;
		int iconId = -1;
		switch (v.getId()) {
		case R.id.help_chart_btn:
			textId = R.string.barchart_help_topic;
			title = getResources().getString(R.string.barchart_help_title);
			iconId = R.drawable.ic_barchart_help;
			break;
		case R.id.help_pie_btn:
			textId = R.string.piechart_help_topic;
			title = getResources().getString(R.string.piechart_help_title);
			iconId = R.drawable.ic_piechart_help;
			break;
		case R.id.help_settings_btn:
			textId = R.string.settings_help_topic;
			title = getResources().getString(R.string.settings_help_title);
			iconId = R.drawable.ic_settings;
			break;
		default:
			break;
		}

		if (textId >= 0) {
			Bundle args = new Bundle();
			args.putInt(ARG_TEXT_ID, textId);
			args.putInt(ARG_ICON_ID, iconId);
			args.putString(ARG_TITLE, title);
			
			Fragment f = new HelpTopicFragment();
			f.setArguments(args);			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.container_frame, f, HELP_TOPIC_FRAGMENT_TAG);
		    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		    ft.addToBackStack(null);
		    ft.commit();
		}
	}
}
