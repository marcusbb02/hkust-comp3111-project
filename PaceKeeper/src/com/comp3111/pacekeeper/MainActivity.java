package com.comp3111.pacekeeper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button lm  = (Button)findViewById(R.id.launch_music);	//	by chau and wyman
		Button lg  = (Button)findViewById(R.id.launch_goal);	//	by chau
		Button lc  = (Button)findViewById(R.id.launch_calibrate);
		lm.setOnClickListener(this);
		lc.setOnClickListener(this);
		lg.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		// TODO Case switch of buttons
		switch(v.getId()){
		case	R.id.launch_music:
			intent = new Intent(this, MusicActivity.class);
			this.startActivity(intent);
			break;
		case	R.id.launch_goal:
			intent = new Intent(this, GoalActivity.class);
			this.startActivity(intent);
			break;
		case	R.id.launch_social:
			intent = new Intent(this, SocialActivity.class);
			this.startActivity(intent);
			break;
		case	R.id.launch_stat:
			intent = new Intent(this, StatActivity.class);
			this.startActivity(intent);
			break;
		case	R.id.launch_calibrate:
			intent = new Intent(this, CalibrateActivity.class);
			this.startActivity(intent);
			break;
		case	R.id.launch_setting:
		}
	}
	

}
