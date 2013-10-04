package com.shico.stats.util;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
	private static final int swipe_Min_Distance = 100;
	private static final int swipe_Max_Distance = 350;
	private static final int swipe_Min_Velocity = 100;

	public interface Swipeable {
		public void onToLeft();
		public void onToRight();
		public void onDown();
		public void onUp();
	}
	public interface DownSwipe {
		public void onDown();
	}
	
	private Swipeable callback;
	private DownSwipe downCallback;
	
	public MyGestureDetectorListener(Swipeable callback) {
		super();
		this.callback = callback;
	}

	public MyGestureDetectorListener(DownSwipe downCallback) {
		super();
		this.downCallback = downCallback;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		final float xDistance = Math.abs(e1.getX() - e2.getX());
		final float yDistance = Math.abs(e1.getY() - e2.getY());
		if (xDistance > swipe_Max_Distance || yDistance > swipe_Max_Distance){
			return false;
		}
		velocityX = Math.abs(velocityX);
		velocityY = Math.abs(velocityY);
		boolean result = false;

		if (velocityX > swipe_Min_Velocity
				&& xDistance > swipe_Min_Distance) {
			if (e1.getX() > e2.getX()){ // right to left
				// swipe to left
				if(callback != null) callback.onToLeft();
			}else{
				if(callback != null) callback.onToRight();
				// swipe to right
			}
			result = true;
		} else if (velocityY > swipe_Min_Velocity
				&& yDistance > swipe_Min_Distance) {
			if (e1.getY() > e2.getY()){
				// swipe up
				if(callback != null) callback.onUp();
			}else if(e1.getY() < 400){
				// swipe down
				if(callback != null) callback.onDown();
				if(downCallback != null) downCallback.onDown();
			}
			result = true;
		}
		return result;
	}

}
