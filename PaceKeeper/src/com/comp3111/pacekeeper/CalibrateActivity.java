package com.comp3111.pacekeeper;

import com.comp3111.local_database.DataBaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class CalibrateActivity extends Activity {
	
	// global VARIABLES
	Animation fadeInAnimation;
	DataBaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		
		TextView guideText = (TextView)findViewById(R.id.calibrate_guideText);
		fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
		guideText.startAnimation(fadeInAnimation);
		
		dbHelper = new DataBaseHelper(this, 1);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calibrate, menu);
		return true;
	}

}
