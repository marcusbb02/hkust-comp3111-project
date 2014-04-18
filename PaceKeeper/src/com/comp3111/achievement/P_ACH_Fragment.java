package com.comp3111.achievement;

import android.content.Context;

import com.comp3111.pacekeeper.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class P_ACH_Fragment extends Fragment {
	
	View rootView;
	Handler mHandler;
	Runnable mRunnable;
	double currentPercentage = 0;
	public static final int FRAME_RATE = 15; 
	public boolean firstTime = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_p_ach, container,
				false);

		
		return rootView;

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser && firstTime) { 
			// animate percentage animation
			final com.comp3111.ui.CircleView cv = (com.comp3111.ui.CircleView)rootView.findViewById(R.id.p_ach_circle);
			final int finalPercentage = 65;
			
			cv.setPercentage(0);
			mHandler = new Handler();
			mRunnable = new Runnable(){
				@Override
				public void run() {
					if(finalPercentage - currentPercentage < 0.01){
						
					}else{
						currentPercentage += (finalPercentage - currentPercentage) / 10;
						cv.setPercentage((int)currentPercentage);
						Log.i("CircleView",currentPercentage+"+("+finalPercentage+" - "+currentPercentage+")/10");
						mHandler.postDelayed(this, FRAME_RATE);
					}
				}			
			};
			// defer to run after 0.5s
			mHandler.postDelayed(mRunnable, 250);
			firstTime = false;
	    }
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

}