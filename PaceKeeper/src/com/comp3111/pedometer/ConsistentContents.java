package com.comp3111.pedometer;

import com.comp3111.achievement.PersonalAchievement;
import com.comp3111.pacekeeper.musicplayerpackage.Singleton_PlayerInfoHolder;
import com.smp.soundtouchandroid.SoundTouchPlayable;

public class ConsistentContents {
	
	public static StatisticsInfo currentStatInfo = new StatisticsInfo(68); 
	public static SoundTouchPlayable soundTouchModule;
	public static Singleton_PlayerInfoHolder playerInfoHolder = Singleton_PlayerInfoHolder.getInstance();
	public static AggregatedRecords aggRecords = new AggregatedRecords();
	public static UserSettings currentUserSettings = new UserSettings();
	public static boolean fblogin=false;
	//public static PedometerSettings pedoSetting = new PedometerSettings();

}