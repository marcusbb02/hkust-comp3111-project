package com.comp3111.achievement;


import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.pacekeeper.R;

import static com.comp3111.local_database.DataBaseConstants.*;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;


@SuppressLint("NewApi")
public class AchievementActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private DataBaseHelper dbhelper;
	private int total_achievements = 3;


	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "STAT", "PER-ACH", "JOINT-ACH" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievement);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.ach_tab); 
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.WHITE));

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab()
					.setText(tab_name)
					.setTabListener(this));
		}
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	
		dbhelper=new DataBaseHelper(this);
		
	//      FrameLayout ll = (FrameLayout)findViewById(R.id.circle_framelayout);
		
	}

	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	

	
	Achievement get_ach(int wantid) { // get data from database

		Achievement output = new Achievement();
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String[] columns = { AID, A_NAME, A_REC };

		Cursor cursor = db.query(ACH_TABLE, columns, AID + " = " + wantid,
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			output.aid = cursor.getInt(0);
			output.name = cursor.getString(1);
			output.record = cursor.getString(2);

			db.close(); // Closing database connection
		}
		return output;
	}

	int[] get_latest() { // get latest finishing id in descending order
		int[] output = { retrieve_order(num_of_ach_finished()),
				retrieve_order(num_of_ach_finished() - 1),
				retrieve_order(num_of_ach_finished() - 2) };
		return output;
	}

	int retrieve_order(int num) { // num=ordering, return id
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String countQuery = "SELECT  " + AID + " FROM " + ACH_TABLE + " WHERE "
				+ A_ORDER + "= ?";
		Cursor cursor = db.rawQuery(countQuery,
				new String[] { String.valueOf(num) });
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	void update_ach(int wantid, String record) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		String[] columns = { A_ORDER };

		Cursor cursor = db.query(ACH_TABLE, columns,
				AID + " = " + String.valueOf(wantid), null, null, null, null,
				null);
		cursor.moveToFirst();

	if (cursor.getInt(0)==0) {

			values.put(A_REC, record);					//new finish,record update
			values.put(A_ORDER, num_of_ach_finished() + 1);
			db.update(ACH_TABLE, values, AID + " = " + String.valueOf(wantid),
					null);
		} else {
			values.put(A_REC, record);					//record update
			db.update(ACH_TABLE, values, AID + " = " + String.valueOf(wantid),
					null);

		}
	}

	int num_of_ach_finished() {
		int output = 0;
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + ACH_TABLE + " WHERE "
				+ A_ORDER + " != ? ", new String[] { "0" });
		output = cursor.getCount();
		return output;
	}

	int finish_percentage() {
		int percentage = 0;
		percentage = 100 * num_of_ach_finished() / total_achievements;
		return percentage;
	}
		
}



class Achievement {
	int aid;
	String name = "";
	String record = "";

	String ach_name() {
		return name;
	}

	public boolean issucceed() {
		if (record.equals(""))
			return false;
		else
			return true;
	}
}

