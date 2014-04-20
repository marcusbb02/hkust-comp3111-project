package com.comp3111.pedometer;

import android.util.Log;

public class QuickStartGoal extends Goal{
	
	// text goes here
	public static final String TITLE_TEXT = "Quick Start";
	public static final String PLACEHOLDER_TEXT = "Time elapsed:";
	
	// time variables
	private long secs = 0;

	public String getTitle(){
		return TITLE_TEXT;
	}
	
	public String getPlaceholder(){
		return PLACEHOLDER_TEXT;
	}
	
	@Override
	boolean updateGoalState() {
		Log.i("Goal", "Elapsed: " + secs);
		secs++;
		updateGoalStateCallback();
		if(secs == 0){
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getText(){
		String min = Integer.toString((int) (secs/60));
		String sec = Integer.toString((int) (secs % 60));
		if(secs % 60 < 10){
			sec = "0" + sec;
		}
		if(secs/60 < 10){
			min = "0" + min;
		}
		return (min+":"+sec);
	}
	
	public void updateGoalStateCallback(){
		
	}

	@Override
	boolean endGoalState() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
