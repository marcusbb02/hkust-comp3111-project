package com.comp3111.pacekeeper;

import com.comp3111.local_database.DataBaseHelper;
import static com.comp3111.local_database.DataBaseConstants.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
	private EditText textjogging = null;
	private EditText textwalking = null;
	private EditText textsprinting = null;
	private Button btnok = null;
	private DataBaseHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		textname = (EditText) findViewById(R.id.Name);
		textemail = (EditText) findViewById(R.id.Email);
		textage = (EditText) findViewById(R.id.Age);
		textjogging = (EditText) findViewById(R.id.jogging);
		textwalking = (EditText) findViewById(R.id.walking);
		textsprinting = (EditText) findViewById(R.id.sprinting);
		btnok = (Button) findViewById(R.id.ok_button);

		dbhelper = new DataBaseHelper(this);
		String[] getv = new String[6];
		getv = get_profile_data(1);
		textname.setText(getv[0]);
		textemail.setText(getv[1]);
		textage.setText(getv[2].toString());
		textjogging.setText(getv[3]);
		textwalking.setText(getv[4]);
		textsprinting.setText(getv[5]);

		btnok.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_button:
			update_to_database(1);
			break;
		}
	}

	void update_to_database(int wantid) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(P_NAME, textname.getText().toString());
		values.put(P_EMAIL, textemail.getText().toString());
		values.put(P_AGE, Integer.parseInt(textage.getText().toString()));
		values.put(P_WALK, textage.getText().toString());
		values.put(P_JOG, textage.getText().toString());
		values.put(P_SPRINT, textage.getText().toString());

		db.update(PRO_TABLE, values, PID + " = " + String.valueOf(wantid), null);
		update_dialog();

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

	String[] get_profile_data(int wantid) {
		String[] output = new String[6];
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String[] columns = { PID, P_NAME, P_EMAIL, P_AGE, P_JOG, P_WALK,
				P_SPRINT };

		Cursor cursor = db.query(PRO_TABLE, columns,
				PID + " = " + String.valueOf(wantid), null, null, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			output[0] = cursor.getString(1);

			output[1] = cursor.getString(2);
			output[2] = Integer.toString(cursor.getInt(2));
			output[3] = cursor.getString(4);
			output[4] = cursor.getString(5);
			output[5] = cursor.getString(6);
		}
		return output;
	};

}
