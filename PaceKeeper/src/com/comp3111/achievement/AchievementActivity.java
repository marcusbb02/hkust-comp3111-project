package com.comp3111.achievement;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.pacekeeper.GreetActivity;
import com.comp3111.pacekeeper.R;
import com.comp3111.pedometer.ConsistentContents;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;

import static com.comp3111.local_database.DataBaseConstants.*;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class AchievementActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private DataBaseHelper dbhelper;

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private SimpleFacebook mSimpleFacebook;
	protected static final String TAG = AchievementActivity.class.getName();
	private TextView mTextStatus;
	
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	private OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			//mTextStatus.setText(reason);
			Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {
			//mTextStatus.setText("Exception: " + throwable.getMessage());
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
			//mTextStatus.setText("Thinking...");
			
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			//mTextStatus.setText("Logged in");
			ConsistentContents.fblogin=true;
			toast("You are logged in");
		}

		@Override
		public void onNotAcceptingPermissions(Permission.Type type) {
			toast(String.format("You didn't accept %s permissions", type.name()));
		}
	};
	
	
	final OnPublishListener onPublishListener = new OnPublishListener() {

		@Override
		public void onFail(String reason) {
			//hideDialog();
			// insure that you are logged in before publishing
			Log.w(TAG, "Failed to publish");
		}

		@Override
		public void onException(Throwable throwable) {
			//hideDialog();
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while publishing
			//showDialog();
		}

		@Override
		public void onComplete(String postId) {
			//hideDialog();
			toast("Published successfully. The new post id = " + postId);
		}
	

	// feed builder
	final Feed feed = new Feed.Builder()
			.setMessage("Running with pacekeeper")
			.setName("Best Running App for Android")
			.setCaption("Run with music, Run with your own pace.")
			.setDescription(
					"Pacekeeper aims to help user keep a constant speed during exercise. By using this, running becomes more efficient to one's cardio workout; it also trains their stamina to run for a longer time.")
			.setPicture("https://hkust-comp3111-project.googlecode.com/svn/trunk/PaceKeeper/ic_launcher-web.png").setLink("https://github.com/sromku/android-simple-facebook").build();

	};
	
	// Tab titles
	// private String[] tabs = { "STAT", "PER-ACH", "JOINT-ACH" };
	private int[] tabs = { R.drawable.ic_action_view_as_list,
			R.drawable.ic_action_person, R.drawable.ic_action_group };

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data); 
	    super.onActivityResult(requestCode, resultCode, data);
	} 
	
	
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

		/*
		 * // Adding Tabs for (String tab_name : tabs) {
		 * actionBar.addTab(actionBar.newTab() .setText(tab_name)
		 * .setTabListener(this)); }
		 */
		// Adding Tabs
		for (int tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setIcon(tab_name)
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

		dbhelper = new DataBaseHelper(this);

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.postmss, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//mSimpleFacebook.logout(onLogoutListener);
		switch (item.getItemId()) {
		case R.id.post_message:
			// app icon in action bar clicked; go home
			
			//if (ConsistentContents.fblogin == false) {
			if(mSimpleFacebook.isLogin()==false){
				Toast.makeText(this, "Please login first", Toast.LENGTH_LONG)
						.show();
				mSimpleFacebook.login( mOnLoginListener);
			} else{
			final Feed feed = new Feed.Builder()
			.setMessage("Running with pacekeeper")
			.setName("Best Running App for Android")
			.setCaption("Run with music, Run with your own pace.")
			.setDescription(
					"I have run "+ConsistentContents.aggRecords.totalSteps+" steps and burnt "+ roundOneDecimal(ConsistentContents.aggRecords.calories) +" calories")
			.setPicture("https://hkust-comp3111-project.googlecode.com/svn/trunk/PaceKeeper/ic_launcher-web.png").setLink("https://code.google.com/p/hkust-comp3111-project/").build();
			
				mSimpleFacebook.publish(feed, true, onPublishListener);
			}
			return true;
		
		
		default:

			return super.onOptionsItemSelected(item);

		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    mSimpleFacebook = SimpleFacebook.getInstance(this);
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



	ArrayList<Integer> get_latest(String table) { // get latest finishing id in
													// descending order
		Log.v("d","s");
		ArrayList<Integer> output = new ArrayList<Integer>();
		int i = num_of_ach_finished(table);
		if (i == 1){
			output.add(retrieve_order(1,table));
		}
		else if (i > 1) {
			output.add(retrieve_order(i,table));
			output.add(retrieve_order(i-1,table));
		}
		return output;
	}

	int retrieve_order(int num, String table) { // num=ordering, return id
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String countQuery = "SELECT  " + AID + " FROM " + table + " WHERE "
				+ A_ORDER + "= ?";
		Cursor cursor = db.rawQuery(countQuery,
				new String[] { String.valueOf(num) });
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	void update_ach(int wantid, String record, String table) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		String[] columns = { A_ORDER };

		Cursor cursor = db.query(table, columns,
				AID + " = " + String.valueOf(wantid), null, null, null, null,
				null);
		cursor.moveToFirst();

		if (cursor.getInt(0) == 0) {

			values.put(A_REC, record); // new finish,record update
			values.put(A_ORDER, num_of_ach_finished(table) + 1);
			db.update(table, values, AID + " = " + String.valueOf(wantid), null);
		} else {
			values.put(A_REC, record); // record update
			db.update(table, values, AID + " = " + String.valueOf(wantid), null);

		}

	}

	public int num_of_ach_finished(String table) {
		int output = 0;
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE "
				+ A_ORDER + " != ? ", new String[] { "0" });
		output = cursor.getCount();
		return output;
	}

	int get_achievement_from_db() {
		int output = 0;
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + ACH_TABLE + " WHERE "
				+ A_ORDER + " != ? ", new String[] { "0" });
		output = cursor.getCount();
		return output;
	}

	int finish_percentage(String table) {
		return (int) (100 * ((float) num_of_ach_finished(table) / (float) dbhelper
				.getCount(table)));
	}
	double roundOneDecimal(double d) { 
        DecimalFormat twoDForm = new DecimalFormat("#.#"); 
        return Double.valueOf(twoDForm.format(d));
    }  
}
