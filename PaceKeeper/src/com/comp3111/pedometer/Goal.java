package com.comp3111.pedometer;

import android.os.Handler;

public abstract class Goal {
	
	// goal thread handler
	private static Handler gHandler = new Handler();
	private static Runnable gRunnable = null;
	
	public void startGoal(final long delayMillis){
		gHandler.postDelayed(gRunnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!updateGoalState()){
					gHandler.removeCallbacks(this);
					endGoalState();
				}
				// repeat itself
				gHandler.postDelayed(this, delayMillis);
			}
			
		}, delayMillis);
	}
	
	public void pauseGoal(){
		gHandler.removeCallbacks(gRunnable);
	}
	
	abstract boolean updateGoalState();
	abstract boolean endGoalState();
	public abstract String getText();
	public abstract String getTitle();
	public abstract String getPlaceholder();
	
}
