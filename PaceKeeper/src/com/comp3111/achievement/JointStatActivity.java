package com.comp3111.achievement;

import com.comp3111.pacekeeper.R;
import com.comp3111.pacekeeper.R.layout;
import com.comp3111.pacekeeper.R.menu;
import com.comp3111.ui.CircleView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class JointStatActivity extends Activity {
	
	public int regionPer, devicePer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joint_stat);
		//get info from extra
		Bundle params = getIntent().getExtras();
		if (params != null) {
			regionPer = params.getInt("regionPer");
			devicePer = params.getInt("devicePer");
		}

		// Show the Up button in the action bar.
		setupActionBar();
		animateCircles();
	}

	private void animateCircles() {
		// TODO Auto-generated method stub
		CircleView cvWhole = (CircleView) findViewById(R.id.j_ach_circle);
		final CircleView cvRegion = (CircleView) findViewById(R.id.j_ach_circle_region);
		final CircleView cvDevice = (CircleView) findViewById(R.id.j_ach_circle_device);
		cvWhole.setColor(CircleView.LIGHT_GREY);
		cvWhole.animateCircle(250, 100);
		cvRegion.setColor(CircleView.GREEN);
		cvRegion.animateCircle(350, regionPer);
		cvDevice.setColor(CircleView.RED);
		cvDevice.setStartOffset(regionPer - devicePer);
		cvDevice.animateCircle(450, devicePer);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
