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
import android.widget.LinearLayout;

public class ProfileActivity extends Activity implements OnClickListener {

	private EditText textname = null;
	private EditText textemail = null;
	private EditText textage = null;
	private EditText textheight = null;
	private EditText textweight = null;
	private EditText textjogging = null;
	private EditText textwalking = null;
	private EditText textsprinting = null;
	private EditText textdescription = null;
	private Button btnok = null;
	private Button btndel = null;
	private DataBaseHelper dbhelper;
	private int get_id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		textname = (EditText) findViewById(R.id.Name);
		textdescription = (EditText) findViewById(R.id.description);
		textemail = (EditText) findViewById(R.id.Email);
		textage = (EditText) findViewById(R.id.Age);
		textheight = (EditText) findViewById(R.id.Height);
		textweight = (EditText) findViewById(R.id.Weight);
		textjogging = (EditText) findViewById(R.id.jogging);
		textwalking = (EditText) findViewById(R.id.walking);
		textsprinting = (EditText) findViewById(R.id.sprinting);
		
		btnok = (Button) findViewById(R.id.ok_button);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			get_id = extras.getInt("pass_id");
		}

		dbhelper = new DataBaseHelper(this);
		String[] getv = new String[9];
		getv = get_profile_data(get_id);
		textname.setText(getv[0]);
		textdescription.setText(getv[1]);
		textemail.setText(getv[2]);
		textage.setText(getv[3].toString());
		textheight.setText(getv[4]);
		textweight.setText(getv[5]);
		textjogging.setText(getv[6]);
		textwalking.setText(getv[7]);
		textsprinting.setText(getv[8]);

		LinearLayout layout = (LinearLayout) findViewById(R.id.Profile_layout);
		layout.setBackgroundColor(Integer.valueOf(getv[9])); // set bg color

		btnok.setOnClickListener(this);

	}

	public void onClick(View v) {
		Intent intent = new Intent();

		switch (v.getId()) {
		case R.id.ok_button:
			update_to_database(get_id);
			setResult(2, intent);
			end_activity();
			break;


		}
	}

	void update_to_database(int wantid) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		try {
			values.put(P_NAME, textname.getText().toString());
			values.put(P_DES, textdescription.getText().toString());
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
		} catch (Exception e) {
			dialog("Invalid input,Please try again!");
		}
	};

	public void dialog(String os) {
		new AlertDialog.Builder(ProfileActivity.this).setTitle("Error")
				.setMessage(os).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	String[] get_profile_data(int wantid) {
		String[] output = new String[10];
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		String[] columns = { PID, P_NAME, P_DES, P_EMAIL, P_AGE, P_HEI, P_WEI, P_JOG,
				P_WALK, P_SPRINT, P_COL };

		Cursor cursor = db.query(PRO_TABLE, columns,
				PID + " = " + String.valueOf(wantid), null, null, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			output[0] = cursor.getString(1);
			output[1] = cursor.getString(2);
			output[2] = cursor.getString(3);
			output[3] = Integer.toString(cursor.getInt(4));
			output[4] = Integer.toString(cursor.getInt(5));
			output[5] = Integer.toString(cursor.getInt(6));
			output[6] = Float.toString(cursor.getFloat(7));
			output[7] = Float.toString(cursor.getFloat(8));
			output[8] = Float.toString(cursor.getFloat(9));
			output[9] = Integer.toString(cursor.getInt(10));
		}
		return output;
	};

	public void end_activity() {
		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

}


