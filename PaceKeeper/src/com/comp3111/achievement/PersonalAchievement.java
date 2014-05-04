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


		personal_ach_list.add(new Achievement("Run for 1 minute","time",1)); 
		personal_ach_list.add(new Achievement("Run for 30 minutes","time",30));
		personal_ach_list.add(new Achievement("Run for 60 minutes","time",60));

		personal_ach_list.add(new Achievement("Walk for 10 steps","step",10));
		personal_ach_list.add(new Achievement("Walk for 100 steps","step",100));
		personal_ach_list.add(new Achievement("Walk for 1000 steps","step",1000));
		personal_ach_list.add(new Achievement("Walk for 2000 steps","step",2000));

		personal_ach_list.add(new Achievement("Go for 50 miles","dist",50));
		personal_ach_list.add(new Achievement("Go for 500 miles","dist",500));
		personal_ach_list.add(new Achievement("Go for 1000 miles","dist",1000));
		
		for(int i=0;i<personal_ach_list.size();i++)
			personal_ach_list.get(i).id=i+1;
		
		dbhelper = new DataBaseHelper(context);
		update_record_from_db();
	}

	public Achievement get_acheivement_by_id(int id) {
		Achievement output=null;
		for(int i=0;i<personal_ach_list.size();i++)
			if(personal_ach_list.get(i).id== id)
				output=personal_ach_list.get(i);
		return output;
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
					AID + " = " + String.valueOf(personal_ach_list.get(i).id), null,
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



public 	ArrayList<Integer> check_if_achieve2(String type, int value) {
	ArrayList<Integer> sublist = new ArrayList<Integer>();
	for (int i = 0; i < personal_ach_list.size(); i++)
		if (personal_ach_list.get(i).type.equals(type)
				&& value >= personal_ach_list.get(i).threshold
				&& !personal_ach_list.get(i).issucceed())
			sublist.add(personal_ach_list.get(i).id);
	return sublist;
}
}