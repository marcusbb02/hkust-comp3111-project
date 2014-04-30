package com.comp3111.pacekeeper;

import static com.comp3111.local_database.DataBaseConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.UserSettings;
import com.comp3111.ui.SwipeListViewTouchListener;

public class ProfileListActivity extends Activity implements
		Button.OnClickListener {

	ArrayList<HashMap<String, Object>> listarray;

	private ListView list;
	private LinearLayout btnadd;
	private DataBaseHelper dbhelper;
	ProfileListAdapter myadapter;

	private int id_list[];
	private int color_list[];
	static private int cur_position = -1;
	static private int edit_position = -1;
	static private int apply_position = 0;
	
	public static String[] randomNames = {"Kim Sung", "Chill Wong", "Orchin", "Wyman", "Henry", "Alex", "Mars"};

	boolean firsttime = true;

	View prev = null;
	String strName = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profilelist);
		dbhelper = new DataBaseHelper(this);
		list = (ListView) findViewById(R.id.pro_list);

		btnadd = (LinearLayout) findViewById(R.id.add_profile_layout);

		btnadd.setOnClickListener(this);

		cur_position = -1; // reset
		list_refresh();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_profile_layout:
			add_item();
			break;
		}

	}

	void change_apply_profile() {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		if (cur_position != -1) {
			values.put(P_AID, id_list[cur_position]);
			db.update(PRO_USING, values, null, null);
			change_setting_data();
			list_refresh();
		}
		cur_position = -1;

	}

	int get_apply_profile_location() { // list array location

		int get = 0;

		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String[] columns = { P_AID };
		Cursor cursor = db.query(PRO_USING, columns, null, null, null, null,
				null, null);
		cursor.moveToFirst();
		get = cursor.getInt(0);

		int k = 0; // search
		for (int i = 0; i < id_list.length; i++)
			if (id_list[i] != get)
				k++;
			else
				break;

		return k;
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

	public String[] get_all_profile_description() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		List<String> al = new ArrayList<String>();

		String[] columns = { P_DES };

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

	public int[] get_all_profile_color() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		List<Integer> al = new ArrayList<Integer>();
		Cursor cursor = db.query(PRO_TABLE, new String[] { P_COL }, null, null,
				null, null, null, null);
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

	}

	public int[] get_all_profile_id() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		List<Integer> al = new ArrayList<Integer>();
		Cursor cursor = db.query(PRO_TABLE, new String[] { PID }, null, null,
				null, null, null, null);
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

	}

	public void list_refresh() {
		String[] name_list = get_all_profile_name();
		String[] des_list = get_all_profile_description();
		id_list = get_all_profile_id();
		color_list = get_all_profile_color();
		apply_position = get_apply_profile_location();

		listarray = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < get_all_profile_id().length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("applyman", R.drawable.ic_circle_man);
			item.put("name", name_list[i]);
			item.put("desc", des_list[i]);
			item.put("editimg", R.drawable.ic_action_edit);
			item.put("bg", R.drawable.blue_back);

			listarray.add(item);
		}

		myadapter = new ProfileListAdapter(this, listarray,
				R.layout.profile_item_layout, new String[] { "applyman",
						"name", "desc", "editimg", "bg" }, new int[] {
						R.id.apply_icon, R.id.p_name, R.id.p_description,
						R.id.edit_btn, R.id.onclick_bg });

		// if (firsttime) {
		// / list.setLayoutAnimation(getAnimationController());
		// firsttime = false;
		// }

		list.setAdapter(myadapter);		
		list.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long arg3) {
				cur_position = position;
				View temp = prev;
				prev = view;

				if (temp != null) {
					ImageView a = (ImageView) temp
							.findViewById(R.id.onclick_bg);
					a.setImageAlpha(0);
				}
				ImageView a = (ImageView) view.findViewById(R.id.onclick_bg);
				a.setImageAlpha(255);

				change_apply_profile();
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0,
					final View arg1, final int pos, long id) {

				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
						ProfileListActivity.this);
				builderSingle.setTitle("Select a colour");
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
						ProfileListActivity.this,
						android.R.layout.select_dialog_item);
				arrayAdapter.add("Red");
				arrayAdapter.add("Orange");
				arrayAdapter.add("Yellow");
				arrayAdapter.add("Green");
				arrayAdapter.add("Blue");
				arrayAdapter.add("Violet");
				arrayAdapter.add("Grey");
				builderSingle.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								strName = "";
								dialog.dismiss();
							}
						});

				builderSingle.setAdapter(arrayAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								strName = arrayAdapter.getItem(which);
								SQLiteDatabase db = dbhelper
										.getWritableDatabase();
								ContentValues values = new ContentValues();
								if (strName == "Red") {
									arg1.setBackgroundColor(Color.argb(202,
											255, 175, 175));
									values.put(P_COL,
											Color.argb(202, 255, 175, 175));
								} else if (strName == "Orange") {
									arg1.setBackgroundColor(Color.argb(202,
											255, 227, 160));
									values.put(P_COL,
											Color.argb(202, 255, 227, 160));
								} else if (strName == "Yellow") {
									arg1.setBackgroundColor(Color.argb(202,
											255, 251, 102));
									values.put(P_COL,
											Color.argb(202, 255, 251, 102));
								} else if (strName == "Green") {
									arg1.setBackgroundColor(Color.argb(202,
											211, 233, 146));
									values.put(P_COL,
											Color.argb(202, 211, 233, 146));
								} else if (strName == "Blue") {
									arg1.setBackgroundColor(Color.argb(194,
											168, 223, 244));
									values.put(P_COL,
											Color.argb(194, 168, 223, 244));
								} else if (strName == "Violet") {
									arg1.setBackgroundColor(Color.argb(202,
											221, 188, 238));
									values.put(P_COL,
											Color.argb(202, 159, 77, 149));
								} else if (strName == "Grey") {
									arg1.setBackgroundColor(Color.argb(202,
											215, 209, 216));
									values.put(P_COL,
											Color.argb(202, 215, 209, 216));
								}

								strName = "";
								db.update(PRO_TABLE, values, PID + " = "
										+ String.valueOf(id_list[pos]), null);
								color_list = get_all_profile_color();
								dialog.dismiss();
							}
						});
				builderSingle.show();

				return true;
			}
		});
		
		// swipe to delete listener
		 SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(
                 list,
                 new SwipeListViewTouchListener.OnSwipeCallback() {
                         @Override
                         public void onSwipeLeft(ListView listView,
                                         int[] reverseSortedPositions) {
                                 // Log.i(this.getClass().getName(),
                                 // "swipe left : pos="+reverseSortedPositions[0]);
                                 // TODO : YOUR CODE HERE FOR LEFT ACTION
                         }

                         @Override
                         public void onSwipeRight(ListView listView,
                                         int[] reverseSortedPositions) {
                             // Log.i(ProfileMenuActivity.class.getClass().getName(),
                             // "swipe right : pos="+reverseSortedPositions[0]);
                             // TODO : YOUR CODE HERE FOR RIGHT ACTION
                    	 	cur_position = reverseSortedPositions[0];
                    	 	del_item();
                         }
                 }, false, // example : left action =without dismiss
                 false, // example : right action without dismiss animation
                 false, false);
		list.setOnTouchListener(touchListener);
		
	}

	public void add_item() {
		// get a pre-defined name
		Random rand = new Random();
		int n = rand.nextInt(randomNames.length-1) + 1;
		
		SQLiteDatabase database = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(P_NAME, randomNames[n]);
		values.put(P_DES, "");
		values.put(P_EMAIL, "");
		values.put(P_AGE, 0);
		values.put(P_COL, -8684677); // (16) FF7B7B7B-100000000
		values.put(P_HEI, 0);
		values.put(P_WEI, 0);
		values.put(P_RID, 1);
		values.put(P_WALK, 0);
		values.put(P_JOG, 0);
		values.put(P_SPRINT, 0);
		database.insert(PRO_TABLE, null, values);
		cur_position = -1;
		list_refresh();
		//dialog("a profile new user has been added");
	}

	public void del_item() {

		if (dbhelper.getCount(PRO_TABLE) == 1)
			dialog("It should contain at least one profile");
		else {
			if (cur_position != -1) { // currently selected
				if (dbhelper.get_applying_profile() == id_list[cur_position])
					dialog("You are applying this profile");
				else {
					SQLiteDatabase db = dbhelper.getWritableDatabase();
					db.delete(PRO_TABLE, PID + "=" + id_list[cur_position],
							null);
					edit_position = -1;
					cur_position = -1;
					//dialog("The profile has been deleted");
					list_refresh();
				}

			}
		}
	}

	public void dialog(String os) {
		new AlertDialog.Builder(ProfileListActivity.this).setMessage(os)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	public void change_setting_data() { // change user setting class
		String[] getdata = new String[9];
		getdata = dbhelper.get_applying_profile_data();
		UserSettings us = ConsistentContents.currentUserSettings;
		us.userName = getdata[0];
		us.userMail = getdata[1];
		us.age = Integer.valueOf(getdata[2]);
		us.height = Integer.valueOf(getdata[3]);
		us.weight = Integer.valueOf(getdata[4]);
		us.regionID = Integer.valueOf(getdata[5]);
		us.walkSpeed = Float.valueOf(getdata[6]);
		us.jogSpeed = Float.valueOf(getdata[7]);
		us.runSpeed = Float.valueOf(getdata[8]);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == 0) { // no change
				edit_position = -1;
			} else if (resultCode == 2) { // update call
				if (apply_position == edit_position)
					change_setting_data();
				edit_position = -1;
				cur_position = -1;
				dialog("The profile has been updated");
				list_refresh();
			}

		}

	}

	// custom adapter inner class

	class ProfileListAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> list;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private int[] valueViewID;

		private ViewTag itemView;

		public class ViewTag {
			ImageView apply;
			TextView text1;
			TextView text2;
			ImageButton ib;
			ImageView bg;

			public ViewTag(ImageView apply, TextView textview1, TextView text2,
					ImageButton ib, ImageView bg) {

				this.apply = apply;
				this.text1 = textview1;
				this.text2 = text2;
				this.ib = ib;
				this.bg = bg;
			}
		}

		public ProfileListAdapter(Context c,
				ArrayList<HashMap<String, Object>> appList, int resource,
				String[] from, int[] to) {

			list = appList;
			mContext = c;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			keyString = new String[from.length];
			valueViewID = new int[to.length];
			System.arraycopy(from, 0, keyString, 0, from.length);
			System.arraycopy(to, 0, valueViewID, 0, to.length);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView != null) {
				itemView = (ViewTag) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.profile_item_layout,
						null);
				itemView = new ViewTag(
						(ImageView) convertView.findViewById(valueViewID[0]),
						(TextView) convertView.findViewById(valueViewID[1]),
						(TextView) convertView.findViewById(valueViewID[2]),
						(ImageButton) convertView.findViewById(valueViewID[3]),
						(ImageView) convertView.findViewById(valueViewID[4]));
				convertView.setTag(itemView);
			}

			HashMap<String, Object> appInfo = list.get(position);
			if (appInfo != null) {

				String name = (String) appInfo.get(keyString[1]);
				String desc = (String) appInfo.get(keyString[2]);

				itemView.text1.setText(name);
				itemView.text2.setText(desc);
				if (position != apply_position) // apply position
					itemView.apply.setImageAlpha(20);
				else
					itemView.apply.setImageAlpha(255);

				if (position == cur_position) // current position
					itemView.bg.setImageAlpha(255);
				else
					itemView.bg.setImageAlpha(0);

				convertView.setBackgroundColor(color_list[position]); // background
																		// color
				itemView.ib.setOnClickListener(new ItemButton_Click(position,
						parent, convertView));
			}

			return convertView;

		}

		class ItemButton_Click implements OnClickListener {
			private int position;
			private View parent;
			private ViewGroup parentgroup;

			ItemButton_Click(int pos, ViewGroup parentgroup, View parent) {
				position = pos;
				this.parentgroup = parentgroup;
				this.parent = parent;
			}

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.edit_btn: {
					Intent intent;
					intent = new Intent(parent.getContext(),
							ProfileActivity.class);
					edit_position = position;
					intent.putExtra("pass_id", id_list[position]);
					startActivityForResult(intent, 0);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				}
					break;
				}
			}
		}

	}
}