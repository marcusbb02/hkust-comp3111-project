package com.comp3111.pacekeeper;

import com.comp3111.achievement.AchievementActivity;
import com.comp3111.local_database.DataBaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CalibrateActivity extends Activity  implements OnClickListener {
	
	// global VARIABLES
	Animation fadeInAnimation;
	DataBaseHelper dbHelper;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		
		TextView guideText = (TextView)findViewById(R.id.calibrate_guideText);
//		fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
//		guideText.startAnimation(fadeInAnimation);
		Button setting_c  = (Button)findViewById(R.id.profile_settingButton);	
		setting_c.setOnClickListener(this);
		Button tst  = (Button)findViewById(R.id.tst2);	
		tst.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calibrate, menu);
		return true;
	}

	
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case	R.id.profile_settingButton:
			intent = new Intent(this, ProfileListActivity.class);
			this.startActivity(intent);
			break;
		case	R.id.tst2:	
			intent = new Intent(this, AchievementActivity.class);
		this.startActivity(intent);
		break;
			}
}
}