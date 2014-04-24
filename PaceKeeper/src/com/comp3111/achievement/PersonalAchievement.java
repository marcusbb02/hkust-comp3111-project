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
		Achievement a1 = new Achievement();
		a1.id = 1;
		a1.name = "Run for 30 minutes";
		a1.type = "time";
		a1.threshold = 30;
		personal_ach_list.add(a1);

		Achievement a2 = new Achievement();
		a2.id = 2;
		a2.name = "Run for 60 minutes";
		a2.type = "time";
		a2.threshold = 60;
		personal_ach_list.add(a2);

		Achievement a3 = new Achievement();
		a3.id = 3;
		a3.name = "walk for 1000 steps";
		a3.type = "step";
		a3.threshold = 1000;
		personal_ach_list.add(a3);
		dbhelper = new DataBaseHelper(context);

		Achievement a4 = new Achievement();
		a4.id = 4;
		a4.name = "walk for 2000 steps";
		a4.type = "step";
		a4.threshold = 2000;
		personal_ach_list.add(a4);
		dbhelper = new DataBaseHelper(context);
		update_record_from_db();

		Achievement a5 = new Achievement();
		a5.id = 5;
		a5.name = "walk for 5000 steps";
		a5.type = "step";
		a5.threshold = 5000;
		personal_ach_list.add(a5);
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