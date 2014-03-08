package com.comp3111.pedometer;

import android.os.Handler;

public abstract class Goal {
	
	// goal thread handler
	private static Handler gHandler = new Handler();
	
	public void startTimedGoal(final long delayMillis){
		gHandler.postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!updateGoalState()){
					gHandler.removeCallbacks(this);
				}
				// repeat itself
				gHandler.postDelayed(this, delayMillis);
			}
			
		}, delayMillis);
	}
	
	abstract boolean updateGoalState();
	
}
