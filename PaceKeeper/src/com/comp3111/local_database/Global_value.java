package com.comp3111.local_database;

import com.comp3111.achievement.PersonalAchievement;

import android.app.Application;
import android.content.res.Configuration;

public class Global_value extends Application{
	public PersonalAchievement PA;
	
	  @Override
	    public void onCreate() {
		  PA= new PersonalAchievement(getApplicationContext());

	    }
}
