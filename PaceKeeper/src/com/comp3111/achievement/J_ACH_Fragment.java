package com.comp3111.achievement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.comp3111.pacekeeper.MusicActivity;
import com.comp3111.pacekeeper.R;
import com.comp3111.pacekeeper.ResultActivity;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.PedometerSettings;
import com.comp3111.ui.CircleView;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class J_ACH_Fragment extends Fragment {
	/**
	 * for demonstrating purpose
	 */
	public boolean firstTime = true;
	public ProgressDialog pd;
	public ImageView regionIV;
	public TextView regionTV;
	Handler mHandler;
	Runnable mRunnable;
	int circleToAnimate = 3, circleAnimating = 1;
	int finalPercentage, currentPercentage;
	public static final int FRAME_RATE = 20;
	
	public String[][] contributors = {{"AAAAAA - 3723 steps", "Chill Wong - 2130 steps", "Keven Kasten - 1745 steps"}
									 ,{"AAAAAA - 673 miles", "Chill Wong - 325 miles", "Keven Kasten - 143 miles"}
									 ,{"AAAAAA - 239 calories", "Chill Wong - 158 calories", "Keven Kasten - 79 calories"}};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_j_ach, container, false);
		RelativeLayout rl_step = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_steps);
		rl_step.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(0, "Step Contribution", 5238, 638+ConsistentContents.aggRecords.totalSteps, ConsistentContents.aggRecords.totalSteps);
			}			
		});
		RelativeLayout rl_dist = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_dist);
		rl_dist.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(1, "Distance Contribution",1034, (int)(459+ConsistentContents.aggRecords.totalMiles), (int) ConsistentContents.aggRecords.totalMiles);
			}			
		});
		RelativeLayout rl_calories = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_calories);
		rl_calories.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(2, "Calories Contribution", 459, (int)(153+ConsistentContents.aggRecords.calories), (int) ConsistentContents.aggRecords.calories);
			}			
		});

		regionIV = (ImageView) rootView.findViewById(R.id.j_ach_map_loc);
		regionTV = (TextView) rootView.findViewById(R.id.j_ach_region_title);
		animateRegion();
		
		return rootView;
	}
	
	private void animateProgress() {
		// TODO Auto-generated method stub
		pd = ProgressDialog.show(getActivity(), null, "Retrieving data from server ......");  
        new Thread(new Runnable() {  
            @Override  
            public void run() {  
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	pd.dismiss();
            }
        }).start();
	}

	public void animateRegion() {
		if(regionIV != null){
			Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
			regionIV.setAnimation(anim);
			regionTV.setAnimation(anim);
			anim.setStartOffset(1500);
			anim.setFillAfter(true);
			anim.start();
		}
	}  
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && firstTime) {
			animateProgress();
			firstTime = false;
		}
	}

	private void showContribution(int id, String title, int totalNo, int regionNo, int deviceNo) {
		Intent intent = new Intent(getActivity(), JointStatActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("totalNo", totalNo);
		intent.putExtra("regionNo", regionNo);
		intent.putExtra("deviceNo", deviceNo);
		intent.putExtra("contributors", contributors[id]);
		startActivity(intent);
	}

}