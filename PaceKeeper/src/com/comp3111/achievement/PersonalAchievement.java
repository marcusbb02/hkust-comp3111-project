package com.comp3111.achievement;

import static com.comp3111.local_database.DataBaseConstants.*;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.comp3111.local_database.DataBaseHelper;

public class PersonalAchievement{

	ArrayList<Achievement> personal_ach_list=new ArrayList<Achievement>();
	Context cc;
	
	private DataBaseHelper dbhelper;
	public PersonalAchievement(Context context) {	//all default personal achievements
		Achievement a1 = new Achievement();
		a1.id = 1;
		a1.name = "Run for 30 minutes";
		a1.type = "time";
		a1.threshold = 30;
		personal_ach_list.add(a1);

		Achievement a2 = new Achievement();
		a2.id = 2;
		a2.name = "walk for 1000 steps";
		a2.type = "step";
		a2.threshold = 1000;
		personal_ach_list.add(a2);
		dbhelper =new DataBaseHelper(context);
		update_record();
	}

	 public Achievement get_acheivement(int index) {
		return personal_ach_list.get(index);
	}
	
	 public int get_num_of_p_ach() {
		return personal_ach_list.size() ;
	}
	 
	 public void update_record()
	 {
		 SQLiteDatabase db=	dbhelper.getReadableDatabase();
		 for(int i=0;i<personal_ach_list.size();i++){
				String[] columns = { A_REC};
			Cursor cursor = db.query(ACH_TABLE, columns,
					AID + " = " + String.valueOf(get_acheivement(i).id), null, null, null, null,
					null);
			cursor.moveToFirst();
		personal_ach_list.get(i).record=cursor.getString(0);
		 }
		
		 
	 }
	 
	ArrayList<Achievement> check_if_achieve(String type, int value) {
		ArrayList<Achievement> sublist=new ArrayList<Achievement>();
		for(int i=0;i<personal_ach_list.size();i++)
		if(personal_ach_list.get(i).type.equals(type)&& value >= personal_ach_list.get(i).threshold && !personal_ach_list.get(i).issucceed())
				sublist.add(personal_ach_list.get(i));
		return sublist;
	}
}