package com.comp3111.pacekeeper;

import static com.comp3111.local_database.DataBaseConstants.ACH_TABLE;
import static com.comp3111.local_database.DataBaseConstants.PRO_TABLE;
import static com.comp3111.local_database.DataBaseConstants.PRO_USING;

import java.io.File;
import java.io.IOException;

import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.local_database.Global_value;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.UserSettings;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity {

	boolean logined = false;
	private SimpleFacebook mSimpleFacebook;
	protected static final String TAG = SettingActivity.class.getName();
	private TextView mTextStatus;
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	OnLogoutListener onLogoutListener = new OnLogoutListener() {
		@Override
		public void onFail(String reason) {
		//	mTextStatus.setText(reason);
			Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {
		//	mTextStatus.setText("Exception: " + throwable.getMessage());
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
		//	mTextStatus.setText("Thinking...");
		}

		
		@Override
	    public void onLogout() {
	        Log.i(TAG, "You are logged out");
	    }

	    /* 
	     * You can override other methods here: 
	     * onThinking(), onFail(String reason), onException(Throwable throwable)
	     */ 
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		Preference regionPref = (Preference) findPreference("region");
		regionPref.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				
				return false;
			}
		});
		Preference fbPref = (Preference) findPreference("resetfb");
		fbPref.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				//if (ConsistentContents.fblogin==true){
				if(mSimpleFacebook.isLogin())
				{mSimpleFacebook.logout(onLogoutListener);
				ConsistentContents.fblogin=false;
				toast("You have logouted");
				}
				else toast("You haven't login");
				return false;
			}
		});

	}
	@Override
	public void onResume() {
	    super.onResume();
	    mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data); 
	    super.onActivityResult(requestCode, resultCode, data);
	} 
	
	
	
	public static void deleteFiles() {

		final String path = Environment.getExternalStorageDirectory()
				.toString() + "/pacekeeper/";
		File file = new File(path);

		// all-records
		file = new File(path + "all-records.dat");
		if (file.exists()) {
			file.delete();
		}
		// reset aggregated records
		ConsistentContents.aggRecords.recordStr = null;
	}


}

class CARDialogPreference extends DialogPreference {

	Context c;

	public CARDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
	}

	public void onClick(DialogInterface dialog, int which) {
		Log.v("d", String.valueOf(which));
		switch (which) {
		case -1:
			SettingActivity.deleteFiles();
			restart_app();
			break;
		}
	}

	void restart_app() {
		Intent i = c.getPackageManager().getLaunchIntentForPackage(
				c.getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		c.startActivity(i);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		persistBoolean(positiveResult);
	}

}

class RESETDialogPreference extends DialogPreference {

	DataBaseHelper dbhelper = null;
	Global_value gv;
	Context c;

	public RESETDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
		dbhelper = new DataBaseHelper(context);
		gv = (Global_value) context.getApplicationContext();

	}

	public void onClick(DialogInterface dialog, int which) {
		Log.v("d", String.valueOf(which));
		switch (which) {
		case -1:
			SettingActivity.deleteFiles();
			dbhelper.close();
			delete_sql_database();
			gv.PA.reset_record(); // update globle achievement values
			ConsistentContents.currentUserSettings=new UserSettings();	//reset user setting
			restart_app();
			break;
		}
	}

	void delete_sql_database() {

		SQLiteDatabase db = dbhelper.getWritableDatabase();
		dbhelper.onUpgrade(db, DataBaseHelper.DATABASE_VERSION, DataBaseHelper.DATABASE_VERSION);
	}

	void restart_app() {
		Intent i = c.getPackageManager().getLaunchIntentForPackage(
				c.getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		c.startActivity(i);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		persistBoolean(positiveResult);
	}

}