package com.comp3111.pedometer;

import android.util.Log;

public class DistanceGoal extends Goal{
	
	// text goes here
	public static final String TITLE_TEXT = "Distance Goal";
	public static final String PLACEHOLDER_TEXT = "Remaining distance:";
	
	// time variables
	private long distance = 5000;
	
	public String getTitle(){
		return TITLE_TEXT;
	}
	
	public String getPlaceholder(){
		return PLACEHOLDER_TEXT;
	}
	
	@Override
	boolean updateGoalState() {
		Log.i("tag", "Remaining: " + distance);
		distance--;
		updateGoalStateCallback();
		if(distance == 0){
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getText(){
		return ""+distance;
	}
	
	public void updateGoalStateCallback(){
		
	}

	@Override
	boolean endGoalState() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
