package com.comp3111.pedometer;

import com.comp3111.local_database.DataBaseHelper;

public class UserSettings {
	// user information
	public String userName = "Default";
	public String userMail = "test@default.com";
	public int age = 20;
	public float height = 170;
	public float weight = 68;
	public  int regionID = 0;
	
	// for pDefaultAverageStepDuration
	public float walkSpeed = 5.0f;
	public float jogSpeed = 4.5f;
	public float runSpeed = 4.0f;
	
}
