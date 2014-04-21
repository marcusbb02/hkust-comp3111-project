package com.comp3111.local_database;
import android.provider.BaseColumns;

public interface DataBaseConstants extends BaseColumns{
	//constants
	
	//profile
    public static String PRO_TABLE = "personal_profile"; 
    public static String PID = "profile_id"; 
    public static String P_NAME = "profile_name"; 
    public static String P_COL = "profile_color"; 
    public static String P_EMAIL = "profile_email"; 
    public static String P_AGE = "profile_age"; 
    public static String P_HEI = "profile_height"; 
    public static String P_WEI = "profile_weight"; 
    public static String P_RID = "region_id"; 
    public static String P_WALK = "profile_walk"; 
    public static String P_JOG = "profile_jog"; 
    public static String P_SPRINT = "profile_run"; 
    
    public static String PRO_USING = "profile_using"; 
    public static String P_AID = "profile_applying_id"; 
    
	//personal_acheivements
    public static String ACH_TABLE = "personal_acheivement"; 
    public static String AID = "acheivement_id"; 
    public static String A_NAME = "acheivement_name"; 
    public static String A_REC = "record"; 
    public static String A_ORDER = "finishing_order"; 
    
    //joint_acheivements
    public static String ACH_TABLE2 = "joint_acheivement"; 
    public static String JID = "acheivement_id"; 
    public static String J_NAME = "acheivement_name"; 
    public static String J_REC = "record"; 
    
    //statistic
    public static String STA_TABLE = "statistic"; 
    public static String AT = "app_times"; 
    
	//setting
    public static String SET_TABLE = "setting"; 
    
    
}


