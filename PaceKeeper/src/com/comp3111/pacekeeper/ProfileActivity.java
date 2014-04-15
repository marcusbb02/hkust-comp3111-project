package com.comp3111.pacekeeper;
import static com.comp3111.local_database.DataBaseConstants.*;

import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.local_database.DataBaseConstants.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends Activity implements OnClickListener {

	private EditText textname = null;
	private EditText textemail = null;
	private EditText textage = null;
	private EditText textheight = null;
	private EditText textweight = null;
	private EditText textjogging = null;
	private EditText textwalking = null;
	private EditText textsprinting = null;
	private Button btnok = null;
	private Button btndel = null;
	private Button backdel = null;
	private DataBaseHelper dbhelper;
	private int get_id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		textname = (EditText) findViewById(R.id.Name);
		textemail = (EditText) findViewById(R.id.Email);
		textage = (EditText) findViewById(R.id.Age);
		textheight = (EditText) findViewById(R.id.Height);
		textweight = (EditText) findViewById(R.id.Weight);
		textjogging = (EditText) findViewById(R.id.jogging);
		textwalking = (EditText) findViewById(R.id.walking);
		textsprinting = (EditText) findViewById(R.id.sprinting);
		btnok = (Button) findViewById(R.id.ok_button);
		btndel = (Button) findViewById(R.id.del_profile_btn);
		backdel = (Button) findViewById(R.id.back_btn);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			get_id = extras.getInt("pass_id");
		}

		dbhelper = new DataBaseHelper(this);
		String[] getv = new String[8];
		getv = get_profile_data(get_id);
		textname.setText(getv[0]);
		textemail.setText(getv[1]);
		textage.setText(getv[2]);
		textheight.setText(getv[3]);
		textweight.setText(getv[4]);
		textjogging.setText(getv[5]);
		textwalking.setText(getv[6]);
		textsprinting.setText(getv[7]);

		btnok.setOnClickListener(this);
		btndel.setOnClickListener(this);
		backdel.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ok_button:
			update_to_database(get_id);
			break;

		case R.id.del_profile_btn:
			if (dbhelper.getCount(PRO_TABLE) == 1)
				one_user_error_dialog();
			else {
				Intent intent = new Intent();
				setResult(1, intent);
				finish();
			}
			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}

	void update_to_database(int wantid) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put(P_NAME, textname.getText().toString());
			values.put(P_EMAIL, textemail.getText().toString());
			values.put(P_AGE, Integer.parseInt(textage.getText().toString()));
			values.put(P_HEI, Integer.parseInt(textheight.getText().toString()));
			values.put(P_WEI, Integer.parseInt(textweight.getText().toString()));
			values.put(P_WALK,
					Float.parseFloat(textwalking.getText().toString()));
			values.put(P_JOG,
					Float.parseFloat(textjogging.getText().toString()));
			values.put(P_SPRINT,
					Float.parseFloat(textsprinting.getText().toString()));
			db.update(PRO_TABLE, values, PID + " = " + String.valueOf(wantid),
					null);
			update_dialog();
		} catch (Exception e) {
			errorinput_dialog();
		}

	};

	String[] get_profile_data(int wantid) {
		String[] output = new String[8];
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String[] columns = { PID, P_NAME, P_EMAIL, P_AGE, P_HEI, P_WEI, P_JOG,
				P_WALK, P_SPRINT };

		Cursor cursor = db.query(PRO_TABLE, columns,
				PID + " = " + String.valueOf(wantid), null, null, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			output[0] = cursor.getString(1);
			output[1] = cursor.getString(2);
			output[2] = Integer.toString(cursor.getInt(3));
			output[3] = Integer.toString(cursor.getInt(4));
			output[4] = Integer.toString(cursor.getInt(5));
			output[5] = Float.toString(cursor.getFloat(6));
			output[6] = Float.toString(cursor.getFloat(7));
			output[7] = Float.toString(cursor.getFloat(8));
		}
		return output;
	};

	
	
	public void update_dialog() {
		new AlertDialog.Builder(ProfileActivity.this)
				.setMessage("The profile has been updated")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void one_user_error_dialog() {
		new AlertDialog.Builder(ProfileActivity.this)
				.setMessage("It should contain at least one profile")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void errorinput_dialog() {
		new AlertDialog.Builder(ProfileActivity.this).setTitle("Error")
				.setMessage("Invalid input,Please try again!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

}


