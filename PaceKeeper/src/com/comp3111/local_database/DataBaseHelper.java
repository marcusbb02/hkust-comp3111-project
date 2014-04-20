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


    private static final int DATABASE_VERSION = 23;
	private static String DB_NAME = "pacekeeper.db";
	
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {

		String PROFILE_TABLE = "CREATE TABLE " + PRO_TABLE + " ( " 
		+ PID		+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ P_NAME 	+ " TEXT," 
		+ P_EMAIL	+ " TEXT," 
		+ P_AGE 	+ " INTEGER," 
		+ P_HEI 	+ " INTEGER," 
		+ P_WEI 	+ " INTEGER," 
		+ P_JOG 	+ " REAL," 
		+ P_WALK	+ " REAL," 
		+ P_SPRINT 	+ " REAL " + ");";
		db.execSQL(PROFILE_TABLE);

		db.execSQL("INSERT INTO "+PRO_TABLE+"  Values "+
		"(0, 'test1', 'test@test.COM', '99', '180', '180', '199','299','399');");
		
		String ACHEIVEMENT_TABLE = "CREATE TABLE " + ACH_TABLE + " ( " 
		+ AID		+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ A_NAME 	+ " TEXT," 
		+ A_REC	    + " TEXT," 
		+ A_ORDER 	+ " INTEGER" +");";
		db.execSQL(ACHEIVEMENT_TABLE);

		db.execSQL("INSERT INTO "+ACH_TABLE+"  Values "+"(null, 'acheivement_1', '','0');");
		db.execSQL("INSERT INTO "+ACH_TABLE+"  Values "+"(null, 'acheivement_2', '','0');");
		db.execSQL("INSERT INTO "+ACH_TABLE+"  Values "+"(null, 'acheivement_3', '2','1');");
	}

	public int getCount(String table) {
	    String countQuery = "SELECT  * FROM " + table;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int c = cursor.getCount();
	    cursor.close();
	    return c;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		resetTable(db);
	}


	public void resetTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + PRO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ACH_TABLE);
		onCreate(db);
	}





}

/**
 * http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */
