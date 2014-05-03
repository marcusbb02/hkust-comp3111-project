package com.comp3111.achievement;

import android.os.Bundle;
import com.comp3111.pacekeeper.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
				
			}			
		});
		return rootView;
	}
}