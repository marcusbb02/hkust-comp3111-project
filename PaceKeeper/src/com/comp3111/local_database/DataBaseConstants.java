package com.comp3111.local_database;
import android.provider.BaseColumns;

public interface DataBaseConstants extends BaseColumns{
	//constants
    public static String PRO_TABLE = "personal_profile"; 
    public static String PID = "profile_id"; 
    public static String P_NAME = "profile_name"; 
    public static String P_EMAIL = "profile_email"; 
    public static String P_AGE = "profile_age"; 
    public static String P_WALK = "profile_walk"; 
    public static String P_JOG = "profile_jog"; 
    public static String P_SPRINT = "profile_sprint"; 
    
	
    public static String ACH_TABLE = "acheivement"; 
    public static String AID = "acheivement_id"; 
    public static String ACH = "acheivement_name"; 
    public static String ISS = "is_succeed"; 
    public static String REC = "record"; 
    
    public static String STA_TABLE = "statistic"; 
    public static String AT = "app_times"; 
    
    public static String SET_TABLE = "setting"; 
    
	//
}




