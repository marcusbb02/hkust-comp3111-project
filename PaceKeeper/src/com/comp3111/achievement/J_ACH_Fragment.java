package com.comp3111.achievement;

import android.app.Dialog;
import android.os.Bundle;

import com.comp3111.pacekeeper.MusicActivity;
import com.comp3111.pacekeeper.R;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.PedometerSettings;
import com.comp3111.ui.CircleView;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class J_ACH_Fragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_j_ach, container, false);
		RelativeLayout rl_btn = (RelativeLayout) rootView.findViewById(R.id.j_ach_btn_steps);
		rl_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showContribution(0, "Step Contribution");
			}			
		});
		
		fetchConsistentContents(rootView);
		return rootView;
	}
	

	private void fetchConsistentContents(View v) {
		TextView tv = (TextView) v.findViewById(R.id.j_ach_your_progress_cc);
		tv.setText(	"ConsistentContents debug: \n\n" + 
					"currentStatInfo recordDateStr: \n" + 
					ConsistentContents.currentStatInfo.getDateString() + "\n\n" + 
					"currentUserSettings: \n" + 
					"userName = " + ConsistentContents.currentUserSettings.userName + 
					"\nuserMail = " + ConsistentContents.currentUserSettings.userMail + 
					"\nwalkSpeed = " + ConsistentContents.currentUserSettings.walkSpeed + 
					"\njogSpeed = " + ConsistentContents.currentUserSettings.jogSpeed + 
					"\nrunSpeed = " + ConsistentContents.currentUserSettings.runSpeed);

	}
	
	private void showContribution(int id, String title) {
		// TODO make an alertdialog for details of contribution
		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge);
		dialog.setTitle(title);
		dialog.setContentView(R.layout.dialog_joint_stat);
		
		CircleView regionCV = (CircleView)dialog.findViewById(R.id.j_ach_circle);
		regionCV.setColor(CircleView.GREEN);
		regionCV.setPercentage(75);
		
		// ... and show the dialog
		dialog.show();
		
	}

}