package com.comp3111.achievement;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

import static com.comp3111.local_database.DataBaseConstants.*;
import com.comp3111.local_database.DataBaseHelper;
import com.comp3111.local_database.Global_value;
import com.comp3111.pacekeeper.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class P_ACH_Fragment extends Fragment {

	View rootView;
	Handler mHandler;
	Runnable mRunnable;
	double currentPercentage = 0;
	public static final int FRAME_RATE = 15;
	public boolean firstTime = true;

	int nof;

	DataBaseHelper db = new DataBaseHelper(getActivity());

	// ((MyActivity ) getActivity()).getClassX() ;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_p_ach, container, false);

		LinearLayout all_ach_list = (LinearLayout) rootView
				.findViewById(R.id.p_ach_a_layout);
		LinearLayout recent_list = (LinearLayout) rootView
				.findViewById(R.id.p_ach_r_layout);

		Global_value gv = (Global_value) getActivity().getApplicationContext();

		// circle view
		nof = ((AchievementActivity) getActivity())
				.finish_percentage(ACH_TABLE);
		TextView fp = (TextView) rootView
				.findViewById(R.id.p_ach_finishpercent);
		fp.setText(String.valueOf(nof) + "%");

		// init_all_achievement_view();

		for (int i = 0; i < gv.PA.get_num_of_p_ach(); i++) { // succeed
			View v = inflater.inflate(R.layout.item_picture_block, null);
			final Achievement a = gv.PA.get_acheivement(i);
			TextView tv = (TextView) v.findViewById(R.id.item_picture_desc);
			tv.setText(a.name);
			if (a.issucceed()) {
				ImageView iv = (ImageView) v
						.findViewById(R.id.item_picture_trop);
				iv.setVisibility(0);
				all_ach_list.addView(v);
			}
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.v("record", a.record);
					Builder MyAlertDialog = new AlertDialog.Builder(
							getActivity());
					MyAlertDialog.setMessage("Date:" + a.record);
					MyAlertDialog.show();
				}

			});
		}

		for (int i = 0; i < gv.PA.get_num_of_p_ach(); i++) { // not succeed
			View v = inflater.inflate(R.layout.item_picture_block, null);
			Achievement a = gv.PA.get_acheivement(i);
			TextView tv = (TextView) v.findViewById(R.id.item_picture_desc);
			tv.setText(a.name);
			if (!a.issucceed()) {
				all_ach_list.addView(v);
			}
		}

		// recent view
		ArrayList<Integer> a = ((AchievementActivity) getActivity())
				.get_latest(ACH_TABLE);
		for (int j = 0; j < a.size(); j++)
			for (int i = 0; i < gv.PA.get_num_of_p_ach(); i++) {
				final Achievement b = gv.PA.get_acheivement(i);
				if (a.get(j) == b.id) {
					View v = inflater
							.inflate(R.layout.item_picture_block, null);
					TextView tv = (TextView) v
							.findViewById(R.id.item_picture_desc);
					ImageView iv = (ImageView) v
							.findViewById(R.id.item_picture_trop);
					iv.setVisibility(0);
					tv.setText(b.name);
					recent_list.addView(v);

					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Log.v("record", b.record);
							Builder MyAlertDialog = new AlertDialog.Builder(
									getActivity());
							MyAlertDialog.setMessage("Date:" + b.record);
							MyAlertDialog.show();
						}

					});
				}

			}
		return rootView;

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && firstTime) {
			// animate percentage animation
			final com.comp3111.ui.CircleView cv = (com.comp3111.ui.CircleView) rootView
					.findViewById(R.id.p_ach_circle);
			final int finalPercentage = nof;

			cv.setPercentage(0);
			mHandler = new Handler();
			mRunnable = new Runnable() {
				@Override
				public void run() {
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
			firstTime = false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
