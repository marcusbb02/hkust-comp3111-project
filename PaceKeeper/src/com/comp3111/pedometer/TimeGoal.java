package com.comp3111.pedometer;

import android.util.Log;

public class TimeGoal extends Goal{
	
	// time variables
	private long secs = 301;
	
	@Override
	boolean updateGoalState() {
		Log.i("tag", "Remaining: " + secs);
		secs--;
		updateGoalStateCallback();
		if(secs == 0){
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	public long getSecs(){
		return secs;
	}
	
	public void updateGoalStateCallback(){
		
	}
	
}
