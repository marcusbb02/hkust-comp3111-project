package com.comp3111.achievement;

import android.content.Intent;
import android.os.Bundle;

import com.comp3111.local_database.RecordHandler;
import com.comp3111.pacekeeper.MusicActivity;
import com.comp3111.pacekeeper.R;
import com.comp3111.pacekeeper.ResultActivity;
import com.comp3111.pedometer.QuickStartGoal;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class STAT_Fragment extends Fragment {
	
	public static final int initAnimationDelay = 100;
	private boolean firstTime = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_stat, container, false);

		RelativeLayout rl_ts = (RelativeLayout) rootView.findViewById(R.id.f_stat_total_steps);
		RelativeLayout rl_tm = (RelativeLayout) rootView.findViewById(R.id.f_stat_total_miles);
		RelativeLayout rl_as = (RelativeLayout) rootView.findViewById(R.id.f_stat_avg_spm);
		RelativeLayout rl_am = (RelativeLayout) rootView.findViewById(R.id.f_stat_avg_mph);
		RelativeLayout rl_tc = (RelativeLayout) rootView.findViewById(R.id.f_stat_total_calories);
		
		// set Text for total stat
		TextView tv = (TextView) rl_ts.findViewById(R.id.item_picture_desc);
		tv.setText("Total Steps taken: 51282");
		tv = (TextView) rl_tm.findViewById(R.id.item_picture_desc);
		tv.setText("Total miles walken: 32713");
		tv = (TextView) rl_as.findViewById(R.id.item_picture_desc);
		tv.setText("Average steps per minute: 120");
		tv = (TextView) rl_am.findViewById(R.id.item_picture_desc);
		tv.setText("Average miles per hour: 325");
		tv = (TextView) rl_tc.findViewById(R.id.item_picture_desc);
		tv.setText("Total calories burnt: 1280");
		
		// animation for total stats
		if(firstTime){
			Animation slideIn1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn3 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn4 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn5 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			slideIn1.setStartTime(AnimationUtils.currentAnimationTimeMillis() + initAnimationDelay);
			rl_ts.setAnimation(slideIn1);
			slideIn2.setStartTime(AnimationUtils.currentAnimationTimeMillis() + initAnimationDelay*2);
			rl_tm.setAnimation(slideIn2);
			slideIn3.setStartTime(AnimationUtils.currentAnimationTimeMillis() + initAnimationDelay*3);
			rl_as.setAnimation(slideIn3);
			slideIn4.setStartTime(AnimationUtils.currentAnimationTimeMillis() + initAnimationDelay*4);
			rl_am.setAnimation(slideIn4);
			slideIn5.setStartTime(AnimationUtils.currentAnimationTimeMillis() + initAnimationDelay*5);
			rl_tc.setAnimation(slideIn5);
			firstTime = false;
		}
		
		// test button for retriving json record
		Button f_stat_load = (Button)rootView.findViewById(R.id.f_stat_load);
		f_stat_load.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RecordHandler.readFromRecord("2014-04-18-15-55-04");
				Intent intent = new Intent(getActivity() , ResultActivity.class);
				startActivity(intent);
			}
		});
		return rootView;
	}
}