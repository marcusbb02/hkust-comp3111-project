package com.comp3111.pacekeeper;

import static com.comp3111.local_database.DataBaseConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.comp3111.local_database.DataBaseHelper;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ProfileListActivity extends Activity implements
Button.OnClickListener {

ArrayList<HashMap<String, Object>> listarray;

private ListView list;
private Button btnapply;
private Button btnadd;
private Button btndel;
private DataBaseHelper dbhelper;
ProfileListAdapter myadapter;

private int id_list[];
private int color_list[];
static private int cur_position = -1;
static private int edit_position = -1;
static private int apply_position = 0;

View prev = null;
String strName = null;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

setContentView(R.layout.activity_profilelist);
dbhelper = new DataBaseHelper(this);
list = (ListView) findViewById(R.id.pro_list);

btnadd = (Button) findViewById(R.id.add_profile_btn);
btnapply = (Button) findViewById(R.id.apply_profile_btn);
btndel = (Button) findViewById(R.id.del_profile_btn);

btnadd.setOnClickListener(this);
btnapply.setOnClickListener(this);
btndel.setOnClickListener(this);

list_refresh();

}

public void onClick(View v) {
switch (v.getId()) {
case R.id.add_profile_btn:
	add_item();
	list_refresh();
	break;
case R.id.apply_profile_btn:
	change_apply_profile();
	break;
case R.id.del_profile_btn:
	del_item();
	break;
}

}

void change_apply_profile() {
SQLiteDatabase db = dbhelper.getWritableDatabase();
ContentValues values = new ContentValues();
if (cur_position != -1) {
	values.put(P_AID, id_list[cur_position]);
	db.update(PRO_USING, values, null, null);
	dialog("The profile you selected has been applied");
	list_refresh();
}

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
id_list = get_all_profile_id();
color_list = get_all_profile_color();
apply_position = get_apply_profile_location();

listarray = new ArrayList<HashMap<String, Object>>();

for (int i = 0; i < get_all_profile_id().length; i++) {
	HashMap<String, Object> item = new HashMap<String, Object>();
	item.put("applyman", name_list[i]);
	item.put("name", name_list[i]);
	item.put("desc", name_list[i]);
	item.put("editimg", R.drawable.edit);
	item.put("bg", R.drawable.blue_back);

	listarray.add(item);
}

myadapter = new ProfileListAdapter(this, listarray,
		R.layout.profile_item_layout, new String[] { "applyman",
				"name", "desc", "editimg", "bg" }, new int[] {
				R.id.apply_icon, R.id.p_name, R.id.p_description,
				R.id.edit_btn, R.id.onclick_bg });

list.setAdapter(myadapter);

list.setOnItemClickListener(new OnItemClickListener() {
	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			int position, long arg3) {
		cur_position = position;
		View temp = prev;
		prev = view;
		// if (temp != view) { // not click the same item
		if (temp != null) {
			ImageView a = (ImageView) temp
					.findViewById(R.id.onclick_bg);
			Log.v("A", a.toString());
			a.setImageAlpha(0);
		}
		ImageView a = (ImageView) view.findViewById(R.id.onclick_bg);
		a.setImageAlpha(255);
		// }
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
		arrayAdapter.add("Black");
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
							arg1.setBackgroundColor(Color.RED);
							values.put(P_COL, Color.RED);
						} else if (strName == "Orange") {
							arg1.setBackgroundColor(Color.rgb(255, 146,
									36));
							values.put(P_COL, Color.rgb(255, 146, 36));
						} else if (strName == "Yellow") {
							arg1.setBackgroundColor(Color.YELLOW);
							values.put(P_COL, Color.YELLOW);
						} else if (strName == "Green") {
							arg1.setBackgroundColor(Color.GREEN);
							values.put(P_COL, Color.GREEN);
						} else if (strName == "Blue") {
							arg1.setBackgroundColor(Color.BLUE);
							values.put(P_COL, Color.BLUE);
						} else if (strName == "Violet") {
							arg1.setBackgroundColor(Color.rgb(159, 77,
									149));
							values.put(P_COL, Color.rgb(159, 77, 149));
						} else if (strName == "Grey") {
							arg1.setBackgroundColor(Color.rgb(123, 123,
									123));
							values.put(P_COL, Color.BLUE);
						} else if (strName == "Black") {
							arg1.setBackgroundColor(Color.BLACK);
							values.put(P_COL, Color.BLUE);
						}

						strName = "";
						int a = Color.RED;

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
}

public void add_item() {
SQLiteDatabase database = dbhelper.getWritableDatabase();
ContentValues values = new ContentValues();
values.put(P_NAME, "new user");
values.put(P_EMAIL, "");
values.put(P_AGE, 0);
values.put(P_COL, -8684677); // (16) FF7B7B7B-100000000
values.put(P_HEI, 0);
values.put(P_WEI, 0);
values.put(P_WALK, 0);
values.put(P_JOG, 0);
values.put(P_SPRINT, 0);
database.insert(PRO_TABLE, null, values);

cur_position = -1;
dialog("a profile new user has been added");
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
			dialog("The profile has been deleted");
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

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if (requestCode == 0) {
	if (resultCode == 0) { // no change
		edit_position = -1;
	} else if (resultCode == 2) { // update call
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
			intent = new Intent(parent.getContext(), ProfileActivity.class);
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

//animation
/*
* class fade_choice { int current = 0; Handler mHandler; Runnable mRunnable;
* 
* void color_fade(final View v, final int start, final int finish, final int
* delta, final int frametime) { current = start;
* 
* mHandler = new Handler(); mRunnable = new Runnable() {
* 
* @Override public void run() { if (Math.abs(finish - current) < delta - 1) { }
* else { if (finish > start) current += delta; else current -= delta;
* v.setBackgroundColor(Color.argb(current, 120, 255, 255));
* mHandler.postDelayed(this, frametime); } } }; mHandler.postDelayed(mRunnable,
* 250);
* 
* }
* 
* }
*/