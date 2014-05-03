package com.comp3111.achievement;

import static com.comp3111.local_database.DataBaseConstants.*;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.comp3111.local_database.DataBaseHelper;

public class PersonalAchievement { // for global uses

	ArrayList<Achievement> personal_ach_list = new ArrayList<Achievement>();
	Context cc;

	private DataBaseHelper dbhelper;

	public PersonalAchievement(Context context) { // all default personal
													// achievements
		Achievement a1 = new Achievement(1,"Run for 1 minute","time",1);
		Achievement a2 = new Achievement(2,"Run for 30 minutes","time",30);
		Achievement a3 = new Achievement(3,"Run for 60 minutes","time",60);
		personal_ach_list.add(a1); 
		personal_ach_list.add(a2);
		personal_ach_list.add(a3);

		Achievement b1 = new Achievement(4,"Walk for 100 steps","step",100);
		Achievement b2 = new Achievement(5,"Walk for 1000 steps","step",1000);
		Achievement b3 = new Achievement(6,"Walk for 2000 steps","step",2000);
		Achievement b4 = new Achievement(7,"Walk for 5000 steps","step",5000);
		personal_ach_list.add(b1);
		personal_ach_list.add(b2);
		personal_ach_list.add(b3);
		personal_ach_list.add(b4);
		  
		/*Achievement c1 = new Achievement(8,"Go for 500 miles","dist",500);
		Achievement c2 = new Achievement(9,"Go for 1000 miles","dist",1000);
		Achievement c3 = new Achievement(10,"Go for 2000 miles","dist",2000);
		personal_ach_list.add(c1);
		personal_ach_list.add(c2);
		personal_ach_list.add(c3);*/
		
		dbhelper = new DataBaseHelper(context);
		update_record_from_db();
	}

	public Achievement get_acheivement(int index) {
		return personal_ach_list.get(index);
	}

	public int get_num_of_p_ach() {
		return personal_ach_list.size();
	}

	public void update_record_from_db() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		for (int i = 0; i < personal_ach_list.size(); i++) {
			String[] columns = { A_REC };
			Cursor cursor = db.query(ACH_TABLE, columns,
					AID + " = " + String.valueOf(get_acheivement(i).id), null,
					null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				personal_ach_list.get(i).record = cursor.getString(0);
			}
		}

	}
	
	public void reset_record() {
		for (int i = 0; i < personal_ach_list.size(); i++) {
			personal_ach_list.get(i).record = "";
		}

	}

	public void store_record(int ach_id, String record) {
		int lo = 0;

		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT MAX(" + A_ORDER + ") FROM "
				+ ACH_TABLE, null);
		if (cursor != null) {
			cursor.moveToFirst();
			lo = cursor.getInt(0) + 1;
		}
		
		Log.v("test2",String.valueOf(lo));
		 db = dbhelper.getWritableDatabase();
		 ContentValues values = new ContentValues();

		 values.put(A_REC,record);
		 values.put(A_ORDER,lo);
		 db.update(ACH_TABLE, values, AID + " = " + String.valueOf(ach_id),
		 null);
		 update_record_from_db(); //update
	}

public 	ArrayList<Achievement> check_if_achieve(String type, int value) {
		ArrayList<Achievement> sublist = new ArrayList<Achievement>();
		for (int i = 0; i < personal_ach_list.size(); i++)
			if (personal_ach_list.get(i).type.equals(type)
					&& value >= personal_ach_list.get(i).threshold
					&& !personal_ach_list.get(i).issucceed())
				sublist.add(personal_ach_list.get(i));
		return sublist;
	}
}