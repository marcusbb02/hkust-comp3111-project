package com.comp3111.pacekeeper;

import static com.comp3111.local_database.DataBaseConstants.ACH_TABLE;
import static com.comp3111.local_database.DataBaseConstants.PRO_TABLE;
import static com.comp3111.local_database.DataBaseConstants.PRO_USING;

import java.io.File;
import java.io.IOException;

import com.comp3111.local_database.DataBaseHelper;

import android.content.Context;
import android.content.DialogInterface;
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
	}
	




}

class CARDialogPreference extends DialogPreference {

	public CARDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onClick(DialogInterface dialog, int which) {
		Log.v("d", String.valueOf(which));
		switch (which) {
		case -1:
			SettingActivity.deleteFiles();
			break;
		}
	}
	


	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		persistBoolean(positiveResult);
	}

}

class RESETDialogPreference extends DialogPreference {

	public RESETDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onClick(DialogInterface dialog, int which) {
		Log.v("d", String.valueOf(which));
		switch (which) {
		case -1:
			SettingActivity.deleteFiles();
			break;
		}
	}


	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		persistBoolean(positiveResult);
	}

	public static void deletesqltable() {
	//	DataBaseHelper dbhelper=new DataBaseHelper(getActivity());
	//	SQLiteDatabase db = dbhelper.getWritableDatabase();
		
	//	db.execSQL("DROP TABLE IF EXISTS " + PRO_TABLE);
	//	db.execSQL("DROP TABLE IF EXISTS " + PRO_USING);
	//	db.execSQL("DROP TABLE IF EXISTS " + ACH_TABLE);
//		dbhelper.onCreate(db);
	}

}