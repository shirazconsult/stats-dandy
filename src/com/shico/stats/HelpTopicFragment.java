package com.shico.stats;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpTopicFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_topic, container, false);

		// Read the arguments from the Intent object.
		Bundle args = getArguments();
		getActivity().getActionBar().setTitle(args.getString(HelpFragment.ARG_TITLE));
		getActivity().getActionBar().setIcon(args.getInt(HelpFragment.ARG_ICON_ID, R.drawable.ic_help));
		int mTextResourceId = args.getInt(HelpFragment.ARG_TEXT_ID, R.string.barchart_help_topic);
		
		TextView textView = (TextView) view.findViewById(R.id.topic_help_textview);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		textView.setText(Html.fromHtml(getString(mTextResourceId)));
		
		return view;
	}

	public void onBackPressed() {
		getFragmentManager().popBackStackImmediate();		
	}
	
}
