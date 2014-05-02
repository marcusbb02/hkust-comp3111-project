package com.comp3111.local_database;

import static com.comp3111.local_database.DataBaseConstants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.

	private static String DB_NAME = "pacekeeper.db";
	   private static final int DATABASE_VERSION = 32;
	   
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
//profile
		String PROFILE_TABLE = "CREATE TABLE " + PRO_TABLE + " ( " 
		+ PID		+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ P_NAME 	+ " TEXT," 
		+ P_DES 	+ " TEXT," 
		+ P_COL 	+ " INTEGER," 
		+ P_EMAIL	+ " TEXT," 
		+ P_AGE 	+ " INTEGER," 
		+ P_HEI 	+ " INTEGER," 
		+ P_WEI 	+ " INTEGER," 
		+ P_RID     + " INTEGER,"
		+ P_WALK	+ " REAL," 
		+ P_JOG 	+ " REAL," 
		+ P_SPRINT 	+ " REAL " + ");";
		db.execSQL(PROFILE_TABLE);
//default
		db.execSQL("INSERT INTO "+PRO_TABLE+"  Values "+
		"(null, 'Default','Default description', '-8684677','test@default.com', '20', '170', '68','0', '5.0','4.5','4.0');");
		
		db.execSQL("CREATE TABLE " + PRO_USING + " ( " + P_AID + " INTEGER" + ");");
		db.execSQL("INSERT INTO "+PRO_USING+"  Values "+" ('1') ;");
		
		//
		String ACHEIVEMENT_TABLE = "CREATE TABLE " + ACH_TABLE + " ( " 
		+ AID		+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ A_REC	    + " TEXT," 
		+ A_ORDER 	+ " INTEGER" +");";
		db.execSQL(ACHEIVEMENT_TABLE);

		for(int i=0;i<7;i++)
		db.execSQL("INSERT INTO "+ACH_TABLE+"  Values "+"(null , '','0');");
	}

	public int getCount(String table) {
	    String countQuery = "SELECT  * FROM " + table;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int c = cursor.getCount();
	    cursor.close();
	    return c;
	}
	
	public int get_applying_profile() {
	    String countQuery = "SELECT  * FROM " + PRO_USING;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    cursor.moveToFirst();
		return cursor.getInt(0);
		}
	
	public String[] get_applying_profile_data() {
		String[] output = new String[9];
		SQLiteDatabase db = this.getReadableDatabase();
		String[] columns = { PID, P_NAME, P_EMAIL, P_AGE, P_HEI, P_WEI,P_RID, P_WALK,
				P_JOG, P_SPRINT };

		Cursor cursor = db.query(PRO_TABLE, columns,
				PID + " = " + String.valueOf(get_applying_profile()), null, null, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			output[0] = cursor.getString(1);
			output[1] = cursor.getString(2);
			output[2] = Integer.toString(cursor.getInt(3));
			output[3] = Integer.toString(cursor.getInt(4));
			output[4] = Integer.toString(cursor.getInt(5));
			output[5] = Integer.toString(cursor.getInt(6));
			output[6] = Float.toString(cursor.getFloat(7));
			output[7] = Float.toString(cursor.getFloat(8));
			output[8] = Float.toString(cursor.getFloat(9));
		}
		return output;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PRO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + PRO_USING);
		db.execSQL("DROP TABLE IF EXISTS " + ACH_TABLE);
		onCreate(db);
	}

	
	

}

/**
 * http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */
