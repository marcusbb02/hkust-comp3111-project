package com.comp3111.achievement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ListIterator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.comp3111.local_database.JSONHandler;
import com.comp3111.pacekeeper.MusicActivity;
import com.comp3111.pacekeeper.R;
import com.comp3111.pacekeeper.ResultActivity;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.QuickStartGoal;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class STAT_Fragment extends Fragment {

	public static final int initAnimationDelay = 100;
	public static final int postDelay = 400;
	private boolean firstTime = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ConsistentContents.aggRecords.recalculateStat();
		// inflate the view for this fragment
		View rootView = inflater.inflate(R.layout.fragment_stat, container, false);

		RelativeLayout rl_ts = (RelativeLayout) rootView.findViewById(R.id.f_stat_total_steps);
		RelativeLayout rl_tm = (RelativeLayout) rootView.findViewById(R.id.f_stat_total_miles);
		RelativeLayout rl_as = (RelativeLayout) rootView.findViewById(R.id.f_stat_avg_spm);
		RelativeLayout rl_am = (RelativeLayout) rootView.findViewById(R.id.f_stat_avg_mph);
		RelativeLayout rl_tc = (RelativeLayout) rootView.findViewById(R.id.f_stat_total_calories);
		
		// animation for total stats
		if(firstTime){
			Animation slideIn1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn3 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn4 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			Animation slideIn5 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
			slideIn1.setStartTime(AnimationUtils.currentAnimationTimeMillis() + postDelay + initAnimationDelay);
			rl_ts.setAnimation(slideIn1);
			slideIn2.setStartTime(AnimationUtils.currentAnimationTimeMillis() + postDelay + initAnimationDelay*2);
			rl_tm.setAnimation(slideIn2);
			slideIn3.setStartTime(AnimationUtils.currentAnimationTimeMillis() + postDelay + initAnimationDelay*3);
			rl_as.setAnimation(slideIn3);
			slideIn4.setStartTime(AnimationUtils.currentAnimationTimeMillis() + postDelay + initAnimationDelay*4);
			rl_am.setAnimation(slideIn4);
			slideIn5.setStartTime(AnimationUtils.currentAnimationTimeMillis() + postDelay + initAnimationDelay*5);
			rl_tc.setAnimation(slideIn5);
			firstTime = false;
		}
		
		// set Text for total stat
		TextView tv = (TextView) rl_ts.findViewById(R.id.item_picture_desc);
		tv.setText("Total Steps taken: " + ConsistentContents.aggRecords.totalSteps + "  ");
		tv = (TextView) rl_tm.findViewById(R.id.item_picture_desc);
		tv.setText("Total miles walken: "+ roundOneDecimal(ConsistentContents.aggRecords.totalMiles) + "  ");
		tv = (TextView) rl_as.findViewById(R.id.item_picture_desc);
		tv.setText("Average steps per minute:"+ roundOneDecimal(ConsistentContents.aggRecords.avgSPM) + "  ");
		tv = (TextView) rl_am.findViewById(R.id.item_picture_desc);
		tv.setText("Average miles per hour: "+roundOneDecimal(ConsistentContents.aggRecords.avgMPH) + "  ");
		tv = (TextView) rl_tc.findViewById(R.id.item_picture_desc);
		tv.setText("Total calories burnt: "+roundOneDecimal(ConsistentContents.aggRecords.calories) + "  ");
		
		// inflate buttons for the records and insert into scroll view
		LinearLayout scrollView = (LinearLayout) rootView.findViewById(R.id.f_stat_scrollLL);
	    
	    // iterate through all records from back to front
	    ArrayList<String> recordStr = ConsistentContents.aggRecords.recordStr;
	    final ListIterator<String> li = recordStr.listIterator(recordStr.size());
	    while(li.hasPrevious()){
		    View v = inflater.inflate(R.layout.item_picture_block, null);	// create template instance for record button
	    	tv = (TextView) v.findViewById(R.id.item_picture_desc);			// and hook the TextView for changing names
	    	final String recordDate = li.previous();								// preserve the date of record
			tv.setText(recordDate);
			v.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// when clicked, retrieve json object, replace the record in ConsistentContents and show to user
					JSONHandler.readFromRecord(recordDate);
					Intent intent = new Intent(getActivity() , ResultActivity.class);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.hold);
				}
			});
			ImageView iv = (ImageView) v.findViewById(R.id.item_picture_arrow);
			iv.setVisibility(0);
			scrollView.addView(v);
	    }
	    
		return rootView;
	}
	
    double roundOneDecimal(double d) { 
        DecimalFormat twoDForm = new DecimalFormat("#.#"); 
        return Double.valueOf(twoDForm.format(d));
    }  
}