package com.comp3111.pacekeeper;

import com.comp3111.pedometer.TimeGoal;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GoalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);
		
		final TextView goal_tv = (TextView)findViewById(R.id.goal_textview);
		Button goal_but_start = (Button)findViewById(R.id.goal_btn_pedo_start);
		Button goal_but_stop = (Button)findViewById(R.id.goal_btn_pedo_stop);
		final TimeGoal tg = new TimeGoal(){
			public void updateGoalStateCallback(){
				goal_tv.setText("Remaining time: " + this.getSecs());
			}
		};
		
		goal_but_start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tg.startTimedGoal(1000);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goal, menu);
		return true;
	}

}
