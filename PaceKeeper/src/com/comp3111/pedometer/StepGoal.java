package com.comp3111.pedometer;

import android.util.Log;

public class StepGoal extends Goal{
	
	// text goes here
	public static final String TITLE_TEXT = "Step Goal";
	public static final String PLACEHOLDER_TEXT = "Remaining steps";
	
	// step variables
	private long step = 1000;

	public String getTitle(){
		return TITLE_TEXT;
	}
	
	public String getPlaceholder(){
		return PLACEHOLDER_TEXT;
	}
	
	@Override
	boolean updateGoalState() {
		Log.i("tag", "Remaining: " + step);
		step--;
		updateGoalStateCallback();
		if(step == 0){
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getText(){
		return ""+step;
	}
	
	public void updateGoalStateCallback(){
		
	}

	@Override
	boolean endGoalState() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
