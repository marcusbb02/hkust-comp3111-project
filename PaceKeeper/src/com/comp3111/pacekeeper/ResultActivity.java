package com.comp3111.pacekeeper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		TextView test = (TextView) findViewById(R.id.result_textview);
		test.setText("hihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\n");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

}
