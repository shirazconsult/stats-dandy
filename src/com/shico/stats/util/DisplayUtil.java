package com.shico.stats.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {
	
	public static Resolution getResolution(Context ctx){
		int density = getDisplayMetrics(ctx).densityDpi;
		if(density >= DisplayMetrics.DENSITY_HIGH && density < DisplayMetrics.DENSITY_XHIGH){
			return Resolution.HIGH;
		}
		if(density >= DisplayMetrics.DENSITY_XHIGH){
			return Resolution.XHIGH;
		}
		return Resolution.MEDIUM;
	}
	
	public static int getOritentation(Context ctx){
		return ctx.getResources().getConfiguration().orientation;		
	}
	
	private static DisplayMetrics displayMetrics;
	public static DisplayMetrics getDisplayMetrics(Context ctx){
		if(displayMetrics == null){
			WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
			displayMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(displayMetrics);
		}
		return displayMetrics;
	}
	
	public enum Resolution {
		MEDIUM, HIGH, XHIGH; 
	}
}
