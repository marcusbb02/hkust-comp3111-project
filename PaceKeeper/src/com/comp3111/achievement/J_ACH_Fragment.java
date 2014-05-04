package com.comp3111.achievement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import com.comp3111.pacekeeper.MusicActivity;
import com.comp3111.pacekeeper.R;
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
	Handler mHandler;
	Runnable mRunnable;
	int circleToAnimate = 3, circleAnimating = 1;
	int finalPercentage, currentPercentage;
	public static final int FRAME_RATE = 20;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_j_ach, container, false);
		RelativeLayout rl_step = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_steps);
		rl_step.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(0, "Step Contribution", 38, 4);
			}			
		});
		RelativeLayout rl_dist = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_dist);
		rl_dist.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(0, "Distance Contribution", 45, 6);
			}			
		});
		RelativeLayout rl_calories = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_calories);
		rl_calories.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(0, "Calories Contribution", 75, 12);
			}			
		});

		regionIV = (ImageView) rootView.findViewById(R.id.j_ach_map_loc);
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

	private void showContribution(int id, String title, int regionPer, int devicePer) {
		/*
		// TODO make an alertdialog for details of contribution
		final Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(title);
		dialog.setContentView(R.layout.dialog_joint_stat);
		
		final CircleView CV = (CircleView)dialog.findViewById(R.id.j_ach_circle);
		final CircleView regionCV = (CircleView)dialog.findViewById(R.id.j_ach_circle_region);
		final CircleView deviceCV = (CircleView)dialog.findViewById(R.id.j_ach_circle_device);
		CV.setColor(CircleView.LIGHT_GREY);
		CV.setPercentage(100);
		regionCV.setColor(CircleView.GREEN);
		regionCV.setPercentage(regionPer - devicePer);
		deviceCV.setColor(CircleView.LIGHT_BLUE);
		deviceCV.setStartOffset(regionPer - devicePer);
		deviceCV.setPercentage(devicePer);
		// animate the circle
		currentPercentage = 0;
		finalPercentage = 100;
		CV.setPercentage(0);
		mHandler = new Handler();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				CircleView cv = CV;
				if (finalPercentage - currentPercentage < 0.01) {

				} else {
					currentPercentage += (finalPercentage - currentPercentage) / 10;
					cv.setPercentage((int) currentPercentage);
					Log.i("CircleView", currentPercentage + "+("
							+ finalPercentage + " - " + currentPercentage
							+ ")/10");
					mHandler.postDelayed(this, FRAME_RATE);
				}
			}
		};
		// defer to run after 0.5s
		mHandler.postDelayed(mRunnable, 250);	
		
		// ... and show the dialog
		dialog.show();
		*/
	}

}