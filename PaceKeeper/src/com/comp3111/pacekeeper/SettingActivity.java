package com.comp3111.pacekeeper;

import static com.comp3111.local_database.DataBaseConstants.ACH_TABLE;
import static com.comp3111.local_database.DataBaseConstants.PRO_TABLE;
import static com.comp3111.local_database.DataBaseConstants.PRO_USING;

import java.io.File;
import java.io.IOException;

import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.local_database.Global_value;
import com.comp3111.pedometer.ConsistentContents;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.util.Log;

public class SettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

	}

	public static void deleteFiles() {

		final String path = Environment.getExternalStorageDirectory()
				.toString() + "/pacekeeper/";
		File file = new File(path);

		if (file.exists()) {
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) {
			}
		}
		
		// all-records
		file = new File(path + "all-records.dat");
		if(file.exists()){
			file.delete();
		}
		// reset aggregated records
		ConsistentContents.aggRecords.recordStr = null;
	}

	public static void deletesqltable() {

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
			gv.PA.update_record_from_db(); // update globle achievement values
			restart_app();
			break;
		}
	}

	void delete_sql_database() {
		c.deleteDatabase("pacekeeper.db");
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