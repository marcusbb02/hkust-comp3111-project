package com.comp3111.pacekeeper.musicplayerpackage;

import android.widget.TabHost;

public class Singleton_TabInfoHolder {
	private static Singleton_TabInfoHolder uniqueInstance;
	static boolean fragmentInflated=false;
	static boolean isTabClickable=true;
	static boolean isSwipeable=true;
	static TabHost mTabHost;
	static CustomViewPager mViewPager; 
	
	public static Singleton_TabInfoHolder getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Singleton_TabInfoHolder();
		}
		return uniqueInstance;
	}
	
	public static void setPageSwipeable(boolean Swipeable){
		isSwipeable=Swipeable;
		mViewPager.setPagingEnabled(Swipeable);
	}
	
	public static void setTabClickable(boolean clickable){
		isTabClickable=clickable;
		mTabHost.getTabWidget().getChildTabViewAt(0).setEnabled(clickable);
		mTabHost.getTabWidget().getChildTabViewAt(1).setEnabled(clickable);
		mTabHost.getTabWidget().getChildTabViewAt(2).setEnabled(clickable);
		mTabHost.getTabWidget().getChildTabViewAt(3).setEnabled(clickable);
	}
}
