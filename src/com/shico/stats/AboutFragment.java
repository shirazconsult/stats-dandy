package com.shico.stats;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.shico.stats.util.Typewriter;

public class AboutFragment extends Fragment {

	interface AnimEndCallback{
		void onAnimationEnd();
	}
	interface AnimStartCallback{
		void onAnimationStart();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.about, container, false);
						
		if(savedInstanceState == null){
			startAnimation(view);
		}else{
			setTexts(view);
		}
		
		return view;
	}

	private ScaleAnimation newScaleAnimation(long startOffset, float startScale, final AnimStartCallback startCallback, final AnimEndCallback endCallback){
		ScaleAnimation anim = new ScaleAnimation(startScale, 1f, startScale, 1f, Animation.RELATIVE_TO_SELF, 0.5f, AnimationSet.RELATIVE_TO_SELF, 0.5f);
		anim.setStartOffset(startOffset);
		anim.setDuration(1000);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				if(startCallback != null){
					startCallback.onAnimationStart();
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(endCallback != null){
					endCallback.onAnimationEnd();
				}
			}
		});
		return anim;
	}
	
	private void startAnimation(final View view){
		final TextView titleView = (TextView)view.findViewById(R.id.nordija_stats_title);
		final TextView verstionView = (TextView)view.findViewById(R.id.version_title);
		final TextView poweredByView = (TextView)view.findViewById(R.id.powered_by);
	
			ScaleAnimation sa1 = newScaleAnimation(500, 25f, null, new AnimEndCallback(){
				@Override
				public void onAnimationEnd() {
					verstionView.setText(R.string.version);				
				}
			});
			titleView.startAnimation(sa1);
			
			ScaleAnimation sa2 = newScaleAnimation(1100, 25f, null, new AnimEndCallback(){
				@Override
				public void onAnimationEnd() {
					poweredByView.setText(R.string.powered_by);
				}
			});
			verstionView.startAnimation(sa2);
			
			ScaleAnimation sa3 = newScaleAnimation(1700, 25f, null, new AnimEndCallback(){
				@Override
				public void onAnimationEnd() {
					Typewriter writer = (Typewriter)view.findViewById(R.id.shico_writer);
					writer.animateText(getActivity().getResources().getString(R.string.shiraz_consult));				
				}
			});
			poweredByView.startAnimation(sa3);	
	}
	
	private void setTexts(View view){
		TextView verstionView = (TextView)view.findViewById(R.id.version_title);
		TextView poweredByView = (TextView)view.findViewById(R.id.powered_by);
		TextView shicoView = (TextView)view.findViewById(R.id.shico_writer);
		
		verstionView.setText(R.string.version);				
		poweredByView.setText(R.string.powered_by);
		shicoView.setText(R.string.shiraz_consult);
	}
}
