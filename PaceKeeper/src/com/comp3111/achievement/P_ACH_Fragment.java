package com.comp3111.achievement;

import android.content.Context;
import com.comp3111.pacekeeper.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class P_ACH_Fragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_p_ach, container,
				false);
	//	ViewGroup PaperLayout = (ViewGroup) findViewById(R.id.PaperLayout);
		//TextView Paper = new TextView(this);
	//	Paper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	//	Paper.setText("Paper2");
	//	DrawView a =new DrawView(getActivity());
		

		View v = inflater.inflate(R.layout.fragment_p_ach, container,
				false);


		return rootView;

	}

	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}