package com.shico.stats.util;

import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class BezierTranslateAnimation extends TranslateAnimation {

	private int mFromXType = ABSOLUTE;
	private int mToXType = ABSOLUTE;
	private int mFromYType = ABSOLUTE;
	private int mToYType = ABSOLUTE;
	private float mFromXValue = 0.0f;
	private float mToXValue = 0.0f;
	private float mFromYValue = 0.0f;
	private float mToYValue = 0.0f;
	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;
	private float mBezierXDelta;
	private float mBezierYDelta;

	public BezierTranslateAnimation(float fromXDelta, float toXDelta,
			float fromYDelta, float toYDelta, float bezierXDelta,
			float bezierYDelta) {
		super(fromXDelta, toXDelta, fromYDelta, toYDelta);

		mFromXValue = fromXDelta;
		mToXValue = toXDelta;
		mFromYValue = fromYDelta;
		mToYValue = toYDelta;
		mFromXType = ABSOLUTE;
		mToXType = ABSOLUTE;
		mFromYType = ABSOLUTE;
		mToYType = ABSOLUTE;
		mBezierXDelta = bezierXDelta;
		mBezierYDelta = bezierYDelta;

	}

	@Override
	public void applyTransformation(float interpolatedTime, Transformation t) {

		if(interpolatedTime == 0f){
			interpolatedTime = 0.8f;
		}
		float dx = 0, dy = 0;
		if (mFromXValue != mToXValue) {

			dx = (float) ((1.0 - interpolatedTime) * (1.0 - interpolatedTime)
					* mFromXValue + 2.0 * interpolatedTime
					* (1.0 - interpolatedTime) * mBezierXDelta + interpolatedTime
					* interpolatedTime * mToXValue);
		}

		if (mFromYValue != mToYValue) {

			dy = (float) ((1.0 - interpolatedTime) * (1.0 - interpolatedTime)
					* mFromYValue + 2.0 * interpolatedTime
					* (1.0 - interpolatedTime) * mBezierYDelta + interpolatedTime
					* interpolatedTime * mToYValue);
		}

		t.getMatrix().setTranslate(dx, dy);

	}

}