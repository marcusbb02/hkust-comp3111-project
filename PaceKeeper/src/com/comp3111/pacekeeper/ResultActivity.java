package com.comp3111.pacekeeper;

import java.text.DecimalFormat;

import com.comp3111.local_database.RecordHandler;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.SpeedAdjuster;
import com.comp3111.pedometer.TimeGoal;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
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
		test.setText("hihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\nhihi\n");
		test.setText(""+ConsistentContents.currentStatInfo.getCaloriesBurn());
		
		InitPaceDistGraph();
		InitJourneyStat();
		InitExtra();
		RecordHandler.createCurrentRecord();
		ConsistentContents.currentStatInfo.toJSON();
	}

	private void InitExtra() {
		// TODO for specifying goals from GoalActivity
		Bundle extras = this.getIntent().getExtras();
		if ( extras != null ) {
		  if ( extras.containsKey("goal") ) {
			// Set the color to different atmosphere
			View mainview = (View)findViewById(R.id.result_main);
			mainview.setBackgroundColor(getResources().getColor(R.drawable.color_green));
		  }
		}		
	}

	public void InitJourneyStat() {
		// rename cells
    	TextView cell_text = (TextView)findViewById(R.id.result_stat_cell2).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("miles");
    	cell_text = (TextView)findViewById(R.id.result_stat_cell3).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("steps/min");
    	cell_text = (TextView)findViewById(R.id.result_stat_cell4).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("miles/hour");
    	cell_text = (TextView)findViewById(R.id.result_stat_cell5).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("calories");
    	TextView left_steps = (TextView)findViewById(R.id.result_stat_cell1).findViewById(R.id.pedo_left_block_number);
    	TextView left_miles = (TextView)findViewById(R.id.result_stat_cell2).findViewById(R.id.pedo_left_block_number);
    	TextView left_stepsPerMin = (TextView)findViewById(R.id.result_stat_cell3).findViewById(R.id.pedo_left_block_number);
    	TextView left_milesPerHour = (TextView)findViewById(R.id.result_stat_cell4).findViewById(R.id.pedo_left_block_number);
    	TextView left_calories = (TextView)findViewById(R.id.result_stat_cell5).findViewById(R.id.pedo_left_block_number);
    	left_steps.setText(""+ConsistentContents.currentStatInfo.getTotalSteps());
		left_miles.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getDistanceTravelled()));
		left_stepsPerMin.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getStepPerTime()));
		left_milesPerHour.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getDistancePerTime()));
		left_calories.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getCaloriesBurn()));

	}

	public void InitPaceDistGraph() {
		// init example series data
		GraphViewData[] graphData = ConsistentContents.currentStatInfo.pace_dist.toArray(new GraphViewData[ConsistentContents.currentStatInfo.pace_dist.size()]);
		final GraphViewSeries exampleSeries = new GraphViewSeries(graphData);		
		final GraphView graphView = new LineGraphView(this, "");
		graphView.addSeries(exampleSeries); // data
		// styling
		graphView.getGraphViewStyle().setNumVerticalLabels(3);
		graphView.setManualYAxisBounds(SpeedAdjuster.SA_FAST, SpeedAdjuster.SA_SLOW);
		graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.chart_text)); // set text size
		graphView.getGraphViewStyle().setGridColor(Color.WHITE);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
		
		// fire up the chart
		LinearLayout layout = (LinearLayout)findViewById(R.id.result_chart_container);
		layout.addView(graphView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}
	
    double roundOneDecimal(double d) { 
        DecimalFormat twoDForm = new DecimalFormat("#.#"); 
        return Double.valueOf(twoDForm.format(d));
    }  
}
