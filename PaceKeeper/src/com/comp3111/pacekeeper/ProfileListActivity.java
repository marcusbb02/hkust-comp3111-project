package com.comp3111.pacekeeper;

import static com.comp3111.local_database.DataBaseConstants.*;

import java.util.ArrayList;
import java.util.List;

import com.comp3111.local_database.DataBaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class ProfileListActivity extends Activity implements
Button.OnClickListener, android.widget.AdapterView.OnItemClickListener  {

	private ListView list;
	private Button btnadd;
	private DataBaseHelper dbhelper;
	private int id_list[];
	static private int cur_position = -1;
	ArrayAdapter<String> aa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profilelist);
		dbhelper = new DataBaseHelper(this);
		list = (ListView) findViewById(R.id.pro_list);
		btnadd = (Button) findViewById(R.id.add_profile_btn);
		btnadd.setOnClickListener(this);

		list_init();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_profile_btn:
			add_item();
			list_refresh();
			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {
		for (int a = 0; a < parent.getChildCount(); a++)
			parent.getChildAt(a).setBackgroundColor(Color.WHITE);

		view.setBackgroundColor(Color.argb(120, 0, 150, 255));
		cur_position = position;

		Intent intent;
		intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("pass_id", id_list[position]);
		startActivityForResult(intent, 0);

	}

	public String[] get_all_profile_name() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		List<String> al = new ArrayList<String>();

		String[] columns = { P_NAME };

		Cursor cursor = db.query(PRO_TABLE, columns, null, null, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			al.add(cursor.getString(0));
			while (cursor.moveToNext()) {
				al.add(cursor.getString(0));
			}
		}

		String[] output = new String[al.size()];
		al.toArray(output);

		return output;

	};

	public int[] get_all_profile_id() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		List<Integer> al = new ArrayList<Integer>();

		String[] columns = { PID };

		Cursor cursor = db.query(PRO_TABLE, columns, null, null, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			al.add(cursor.getInt(0));
			while (cursor.moveToNext()) {
				al.add(cursor.getInt(0));
			}
		}

		int[] output = new int[al.size()];
		for (int i = 0; i < output.length; i++)
			output[i] = al.get(i);

		return output;

	};

	public void list_init() {
		list.setOnItemClickListener(this);
		list.setBackgroundColor(Color.WHITE);
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, get_all_profile_name());
		list.setAdapter(aa);
		list.setTextFilterEnabled(true);
		id_list = get_all_profile_id();

	}

	public void list_refresh() {
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, get_all_profile_name());
		list.setAdapter(aa);
		list.setTextFilterEnabled(true);
		aa.notifyDataSetChanged();
		id_list = get_all_profile_id();
	}

	public void add_item() {
		SQLiteDatabase database = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(P_NAME, "new user");
		values.put(P_EMAIL, "");
		values.put(P_AGE, 0);
		values.put(P_HEI, 0);
		values.put(P_WEI, 0);
		values.put(P_WALK, 0);
		values.put(P_JOG, 0);
		values.put(P_SPRINT, 0);
		database.insert(PRO_TABLE, null, values);
	}

	public void del_dialog() {
		new AlertDialog.Builder(ProfileListActivity.this)
				.setMessage("The profile has been deleted")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void del_item() {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		if (dbhelper.getCount(PRO_TABLE) > 1) 
			if (cur_position != -1) {
				db.delete(PRO_TABLE, PID + "=" + id_list[cur_position], null);
				cur_position = -1;
			}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == 0)				//no change
				list_refresh();
			else if (resultCode == 1) {			//delete call
				del_item();
				list_refresh();
				del_dialog();
			}

		}

	}


}
