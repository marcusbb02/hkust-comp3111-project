package com.comp3111.pacekeeper;

import java.io.IOException;

import com.comp3111.pedometer.Pedometer;
import com.comp3111.pedometer.SpeedAdjuster;
import com.comp3111.swipeview.SwipeDismissTouchListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.smp.soundtouchandroid.SoundTouchPlayable;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PedoActivity extends Activity implements OnClickListener{

	String fullPathToAudioFile = Environment.getExternalStorageDirectory().toString() + "/test.mp3";
	TextView gForce, pedoSteps, av_duration;
	double time_axis;
	Pedometer pedo;

	//class-accessible Player
	SoundTouchPlayable stp;
	float tempoValue = 1.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedo);
				
		// initialize UI objects
		gForce=(TextView)findViewById(R.id.tw_pedo_gf);
		pedoSteps=(TextView)findViewById(R.id.tw_pedo_steps);
		av_duration=(TextView)findViewById(R.id.tw_pedo_av_duration);
		final Button btn_ru  = (Button)findViewById(R.id.mus_btn_rampup);
		final Button btn_rn  = (Button)findViewById(R.id.mus_btn_rampnormal);
		final Button btn_rd  = (Button)findViewById(R.id.mus_btn_rampdown);
		
		LinearLayout mus_player_buttons = (LinearLayout)findViewById(R.id.mus_player_buttons);
		mus_player_buttons.setOnTouchListener(new SwipeDismissTouchListener(
			mus_player_buttons,
			null,
			new SwipeDismissTouchListener.OnDismissCallback(){
				@Override
				public void onDismiss(View view, Object token) {
					// TODO Auto-generated method stub
					
				}
			}
		));
		
		gForce.setText(Environment.getExternalStorageDirectory().toString());
              
		// init example series data
		final GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
			        new GraphViewData(0, 0.0d)
			});		
		final GraphView graphView = new LineGraphView(
				this // context
				, "" // heading
		);
		graphView.addSeries(exampleSeries); // data
		graphView.setScrollable(true);		// for appending
		// optional - set view port; size matched with data stored, start at y-value 0
		graphView.setViewPort(0, 49);
		graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.chart_text)); // set text size
		graphView.getGraphViewStyle().setGridColor(Color.WHITE);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
		
		// fire up the chart
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart_container);
		layout.addView(graphView);
		
		// init time_axis
		time_axis = 0;		

        // init Pedometer, a new thread is preserved for graph updating action
		// it is running at 100ms = 10Hz
        pedo = new Pedometer(PedoActivity.this, 100, 10){
        	// for immediate actions
        	public void onSensorChangedCallback(float g){
        		gForce.setText("G-Force: " + g);
        	}
        	// for lengthier thread action
        	public void PedoThreadCallback(int st, float threshold, float s_duration){
				time_axis += 1d;
				exampleSeries.appendData(new GraphViewData(time_axis, pForce), true, 50 );
				pedoSteps.setText("Steps taken: " + st+"; Th: " + threshold);
				av_duration.setText("Av. Step Duration: "+s_duration);
				switch(SpeedAdjuster.react(pedo, stp)){
					case	SpeedAdjuster.SA_FAST:
						btn_ru.setVisibility(View.VISIBLE);
						btn_rn.setVisibility(View.INVISIBLE);
						btn_rd.setVisibility(View.INVISIBLE);
						break;
					case	SpeedAdjuster.SA_NORMAL:
						btn_ru.setVisibility(View.INVISIBLE);
						btn_rn.setVisibility(View.VISIBLE);
						btn_rd.setVisibility(View.INVISIBLE);
						break;
					case	SpeedAdjuster.SA_SLOW:
						btn_ru.setVisibility(View.INVISIBLE);
						btn_rn.setVisibility(View.INVISIBLE);
						btn_rd.setVisibility(View.VISIBLE);
						break;
				}
        	}
        };

		Button btn_pp  = (Button)findViewById(R.id.mus_btn_pedo_start);
		Button btn_st  = (Button)findViewById(R.id.mus_btn_pedo_stop);
		btn_pp.setOnClickListener(this);
		btn_st.setOnClickListener(this);
		
		//the last two parameters are speed of playback and pitch in semi-tones.
		try {
			// use temporarily - for internal testing
			AssetFileDescriptor assetFd = getAssets().openFd("test.mp3");
			stp = SoundTouchPlayable.createSoundTouchPlayable(assetFd , 0, 1.0f, 0.0f);
			//stp = SoundTouchPlayable.createSoundTouchPlayable(fullPathToAudioFile , 0, 1.0f, 0.0f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(stp != null && !stp.isPaused()){
			stp.pause();
			stp.play();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		stp.stop();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Case switch of buttons
		switch(arg0.getId()){
		case	R.id.mus_btn_pedo_start:
			if(stp.isPaused()){
				//the last two parameters are speed of playback and pitch in semi-tones.
				try {
					// use temporarily - for internal testing
					AssetFileDescriptor assetFd = getAssets().openFd("test.mp3");
					stp = SoundTouchPlayable.createSoundTouchPlayable(assetFd , 0, 1.0f, 0.0f);
					//stp = SoundTouchPlayable.createSoundTouchPlayable(fullPathToAudioFile , 0, 1.0f, 0.0f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stp.play();
				Button btn_pp  = (Button)findViewById(R.id.mus_btn_pedo_start);
				btn_pp.setText("Pause");
				
			}else{
				stp.pause();
				Button btn_pp  = (Button)findViewById(R.id.mus_btn_pedo_start);
				btn_pp.setText("Play");
			}
	        pedo.startSensor();
			break;
		case	R.id.mus_btn_pedo_stop:
			stp.stop();
			Button btn_pp  = (Button)findViewById(R.id.mus_btn_pedo_start);
			btn_pp.setText("Play");
			pedo.stopSensor();
			break;
		}
	}

}
