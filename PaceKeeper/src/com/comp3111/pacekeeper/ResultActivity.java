package com.comp3111.pacekeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.comp3111.achievement.Achievement;
import com.comp3111.local_database.Global_value;
import com.comp3111.local_database.JSONHandler;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.DistanceGoal;
import com.comp3111.pedometer.QuickStartGoal;
import com.comp3111.pedometer.SpeedAdjuster;
import com.comp3111.pedometer.StepGoal;
import com.comp3111.pedometer.TimeGoal;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		TextView test = (TextView) findViewById(R.id.result_textview);
		test.setText("" + ConsistentContents.currentStatInfo.getCaloriesBurn());

		initPaceDistGraph();
		initJourneyStat();
		checkGoalType();
		check_achievement();

	}

	public void initJourneyStat() {
		// rename cells
		TextView cell_text = (TextView) findViewById(R.id.result_stat_cell2)
				.findViewById(R.id.pedo_left_block_desc);
		cell_text.setText("miles");
		cell_text = (TextView) findViewById(R.id.result_stat_cell3)
				.findViewById(R.id.pedo_left_block_desc);
		cell_text.setText("steps/min");
		cell_text = (TextView) findViewById(R.id.result_stat_cell4)
				.findViewById(R.id.pedo_left_block_desc);
		cell_text.setText("miles/hour");
		cell_text = (TextView) findViewById(R.id.result_stat_cell5)
				.findViewById(R.id.pedo_left_block_desc);
		cell_text.setText("calories");
		TextView left_steps = (TextView) findViewById(R.id.result_stat_cell1)
				.findViewById(R.id.pedo_left_block_number);
		TextView left_miles = (TextView) findViewById(R.id.result_stat_cell2)
				.findViewById(R.id.pedo_left_block_number);
		TextView left_stepsPerMin = (TextView) findViewById(
				R.id.result_stat_cell3).findViewById(
				R.id.pedo_left_block_number);
		TextView left_milesPerHour = (TextView) findViewById(
				R.id.result_stat_cell4).findViewById(
				R.id.pedo_left_block_number);
		TextView left_calories = (TextView) findViewById(R.id.result_stat_cell5)
				.findViewById(R.id.pedo_left_block_number);
		left_steps.setText(""
				+ ConsistentContents.currentStatInfo.getTotalSteps());
		left_miles.setText(""
				+ roundOneDecimal(ConsistentContents.currentStatInfo
						.getDistanceTravelled()));
		left_stepsPerMin.setText(""
				+ roundOneDecimal(ConsistentContents.currentStatInfo
						.getStepPerTime()));
		left_milesPerHour.setText(""
				+ roundOneDecimal(ConsistentContents.currentStatInfo
						.getDistancePerTime()));
		left_calories.setText(""
				+ roundOneDecimal(ConsistentContents.currentStatInfo
						.getCaloriesBurn()));

	}

	public void initPaceDistGraph() {
		// init example series data
		GraphViewData[] graphData = ConsistentContents.currentStatInfo.pace_dist
				.toArray(new GraphViewData[ConsistentContents.currentStatInfo.pace_dist
						.size()]);
		final GraphViewSeries exampleSeries = new GraphViewSeries(graphData);
		final GraphView graphView = new LineGraphView(this, "");
		graphView.addSeries(exampleSeries); // data
		// styling
		graphView.getGraphViewStyle().setNumVerticalLabels(3);
		graphView.setManualYAxisBounds(SpeedAdjuster.SA_FAST,
				SpeedAdjuster.SA_SLOW);
		graphView.getGraphViewStyle().setTextSize(
				getResources().getDimension(R.dimen.chart_text)); // set text
																	// size
		graphView.getGraphViewStyle().setGridColor(Color.WHITE);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);

		// fire up the chart
		LinearLayout layout = (LinearLayout) findViewById(R.id.result_chart_container);
		layout.addView(graphView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

	public void checkGoalType() {
		// set the text of placeholders
		TextView placeholder = (TextView) findViewById(R.id.result_journey_type);
		placeholder.setText(ConsistentContents.currentStatInfo.journeyType);
		placeholder = (TextView) findViewById(R.id.result_goal_title);
		if (!ConsistentContents.currentStatInfo.journeyType
				.equals(QuickStartGoal.TITLE_TEXT)) {
			// Set the color to different atmosphere
			View mainview = (View) findViewById(R.id.result_main);
			mainview.setBackgroundColor(getResources().getColor(
					R.drawable.color_green));
		} else {
			// set goal target text
			placeholder.setText(QuickStartGoal.PLACEHOLDER_TEXT);
		}
		if (ConsistentContents.currentStatInfo.journeyType
				.equals(TimeGoal.TITLE_TEXT)) {
			// set goal target text
			placeholder.setText(TimeGoal.PLACEHOLDER_TEXT);
		} else if (ConsistentContents.currentStatInfo.journeyType
				.equals(DistanceGoal.TITLE_TEXT)) {
			// set goal target text
			placeholder.setText(DistanceGoal.PLACEHOLDER_TEXT);
		} else if (ConsistentContents.currentStatInfo.journeyType
				.equals(StepGoal.TITLE_TEXT)) {
			// set goal target text
			placeholder.setText(StepGoal.PLACEHOLDER_TEXT);
		}/*
		 * else
		 * if(ConsistentContents.currentStatInfo.journeyType.equals(TimeGoal
		 * .TITLE_TEXT)){ // set goal target text
		 * placeholder.setText(TimeGoal.PLACEHOLDER_TEXT); }
		 */
		placeholder = (TextView) findViewById(R.id.result_goal_value);
		placeholder.setText(ConsistentContents.currentStatInfo.journeyTime);
	}

	public void check_achievement() {
		LinearLayout ach_l = (LinearLayout) findViewById(R.id.result_achievement_container);
		LayoutInflater vi = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Global_value gv = (Global_value) getApplicationContext();

		// check step
		ArrayList<Achievement> a = gv.PA.check_if_achieve("step",
				ConsistentContents.aggRecords.totalSteps);
		for (int i = 0; i < a.size(); i++) {

			gv.PA.store_record(a.get(i).id, "33");
			View v = vi.inflate(R.layout.item_picture_block, null);
			TextView tv = (TextView) v.findViewById(R.id.item_picture_desc);
			tv.setText(a.get(i).name);
			ach_l.addView(v);
		}

		// check time

		ArrayList<Achievement> b = gv.PA
				.check_if_achieve(
						"time",
						(int) (ConsistentContents.aggRecords.totalSteps / ConsistentContents.aggRecords.avgSPM));
		Log.v("dd", String.valueOf((int) (ConsistentContents.aggRecords.totalSteps / ConsistentContents.aggRecords.avgSPM)));
		for (int i = 0; i < b.size(); i++) {

			gv.PA.store_record(b.get(i).id, "33");
			View v = vi.inflate(R.layout.item_picture_block, null);
			TextView tv = (TextView) v.findViewById(R.id.item_picture_desc);
			tv.setText(b.get(i).name);
			ach_l.addView(v);
		}

	}

	double roundOneDecimal(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.#");
		return Double.valueOf(twoDForm.format(d));
	}
}
