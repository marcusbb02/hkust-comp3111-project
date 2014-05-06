package com.comp3111.pacekeeper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.comp3111.achievement.Achievement;
import com.comp3111.achievement.PersonalAchievement;
import com.comp3111.local_database.Global_value;
import com.comp3111.local_database.JSONHandler;
import com.comp3111.pacekeeper.musicplayerpackage.MainMusicPlayerActivity;
import com.comp3111.pacekeeper.musicplayerpackage.STMediaPlayer;
import com.comp3111.pacekeeper.musicplayerpackage.Singleton_PlayerInfoHolder;
import com.comp3111.pedometer.*;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.smp.soundtouchandroid.SoundTouchPlayable;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicActivity extends Activity {
	
	// For ViewPager
	private ViewPager mPager;
    private List<View> listViews;
    private ViewGroup leftPanel, centerPanel, rightPanel;
    private int currIndex = 1;	// start off from middle page
    private int ivCursorWidth;
    private int tabWidth, screenW, screenH, offsetX;
    private ImageView ivCursor, ivAlbumArt;
    int finalHeight, actionBarHeight;
    
    // For things inside ViewPager
	String fullPathToAudioFile = Environment.getExternalStorageDirectory().toString() + "/test.mp3";
	TextView gForce, pedoSteps, av_duration, left_steps, left_miles, left_stepsPerMin, left_milesPerHour, left_calories, rht_main_text, vp_song_name, vp_artist_name, vp_album_name;
	ImageButton btn_pause, btn_play;
	double time_axis;
	Pedometer pedo;
	int lastSpeedState = SpeedAdjuster.SA_NORMAL;
	Goal goal = null;
	//public PersonalAchievement per_ach = new PersonalAchievement(this);
	
	/**
	 * Consistent Contents includes
	 * currentStatInfo : StatisticsInfo
	 * playerInfoHolder.player : SoundTouchPlayable
	 */

	//class-accessible Player
	float tempoValue = 1.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedo_viewpager);
		// reset stat info
		ConsistentContents.currentStatInfo = new StatisticsInfo(68);
		InitViewPager();
		InitImageView();	// for page cursor and album art resizing
        mPager.setCurrentItem(1);
        InitPostView();		// for setting up view's position dynamically
        InitExtra();
		// request for the default average step duration
        RequestMode();
	}
	
	private void RequestMode() {
		// TODO make an alertdialog asking for walk/jog/run, and put the appropriate average step duration to infoContent
		final Dialog dialog = new Dialog(MusicActivity.this);
		dialog.setContentView(R.layout.dialog_mode);
		dialog.setTitle("I would like to");
		// force user to choose from one of the modes
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		
		// OnClickListener to put appropriate average step duration value to infoContent
		OnClickListener userModeOCL = new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				switch(arg0.getId()){
				case R.id.d_mode_walk:
					PedometerSettings.pDefaultAverageStepDuration = ConsistentContents.currentUserSettings.walkSpeed;
					break;
				case R.id.d_mode_jog:
					PedometerSettings.pDefaultAverageStepDuration = ConsistentContents.currentUserSettings.jogSpeed;
					break;
				case R.id.d_mode_run:
					PedometerSettings.pDefaultAverageStepDuration = ConsistentContents.currentUserSettings.runSpeed;
					break;
				}
				// make the dialog disappear and set the Pedometer for the desired value
				pedo.resetAverageStepDuration();
				dialog.dismiss();				
			}			
		};
		
		// initialize the buttons
		RelativeLayout dialog_but = (RelativeLayout) dialog.findViewById(R.id.d_mode_walk);
		dialog_but.setOnClickListener(userModeOCL);
		dialog_but = (RelativeLayout) dialog.findViewById(R.id.d_mode_jog);
		dialog_but.setOnClickListener(userModeOCL);
		dialog_but = (RelativeLayout) dialog.findViewById(R.id.d_mode_run);
 		dialog_but.setOnClickListener(userModeOCL);
		// ... and show the dialog
		dialog.show();
		
	}

	private void InitExtra() {
		// for specifying goals from GoalActivity
		Bundle extras = this.getIntent().getExtras();
		if ( extras != null ) {
		  if ( extras.containsKey("goal_type") ) {
			// Set the color to different atmosphere
			View mainview = (View)findViewById(R.id.pedo_vp_main);
			mainview.setBackgroundColor(getResources().getColor(R.drawable.color_green));
			// create goal according to pref selected, and set the text
			String title = extras.getString("goal_type");
		    this.setTitle(title);
		    if(title.equals(TimeGoal.TITLE_TEXT)){
		        goal = new TimeGoal(){
					public void updateGoalStateCallback(){
						rht_main_text.setText(this.getText());
					}
				};
		    }
		    if(title.equals(StepGoal.TITLE_TEXT)){
		        goal = new StepGoal(){
					public void updateGoalStateCallback(){
						rht_main_text.setText(this.getText());
					}
				};
		    }
		    if(title.equals(DistanceGoal.TITLE_TEXT)){
		        goal = new DistanceGoal(){
					public void updateGoalStateCallback(){
						rht_main_text.setText(this.getText());
					}
				};
		    }
		    if(title.equals("Cardio Goal")){
		        goal = new StepGoal(){
					public void updateGoalStateCallback(){
						rht_main_text.setText(this.getText());
					}
				};
		    }
			TextView rht_text = (TextView)rightPanel.findViewById(R.id.pedo_right_title);
			rht_text.setText(goal.getTitle());
			rht_text = (TextView)rightPanel.findViewById(R.id.pedo_right_placeholder);
			rht_text.setText(goal.getPlaceholder());
			// setup for ResultActivity
			ConsistentContents.currentStatInfo.journeyType = goal.getTitle();
		  }
		}
	}

	private void InitPostView(){
        //find actionbar size
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

		LinearLayout collapsed_player_layout = (LinearLayout)findViewById(R.id.pedo_vp_collpase_player_layout);
		LinearLayout collpase_player_layout_placeholder = (LinearLayout)findViewById(R.id.pedo_vp_collpase_player_layout_placeholder);
		
		// set height of player
		collpase_player_layout_placeholder.getLayoutParams().height = (int) ((screenH - actionBarHeight) * 0.4);
		collapsed_player_layout.getLayoutParams().height = (int) ((screenH - actionBarHeight) * 0.4);
		collapsed_player_layout.bringToFront();
		collapsed_player_layout.invalidate();
        // collapsed music player action
		btn_pause = (ImageButton)findViewById(R.id.pedo_vp_collapsed_pause);
		btn_play = (ImageButton)findViewById(R.id.pedo_vp_collapsed_play);
		btn_pause.setVisibility(View.GONE);
		collapsed_player_layout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//Launch ExtendedMusicActivity
				pedo.stopSensor();
				goal.pauseGoal();
				SpeedAdjuster.resetNormal(ConsistentContents.playerInfoHolder.player.getSoundTouchPlayable());
				Intent intent = new Intent(MusicActivity.this, MainMusicPlayerActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_from_above, R.anim.hold);
			}			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calibrate, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(ConsistentContents.playerInfoHolder.player != null)
			updateAlbumArtAndInfo();
		if(ConsistentContents.playerInfoHolder.player.isPlaying()){
			pedo.startSensor();
			goal.startGoal(1000);
			btn_pause.setVisibility(View.VISIBLE);
			btn_play.setVisibility(View.GONE);
		}else{
			btn_pause.setVisibility(View.GONE);
			btn_play.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    ConsistentContents.playerInfoHolder.player.stop();
		pedo.stopSensor();
		goal.pauseGoal();
	}
    
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.pedo_vp_vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        leftPanel = (ViewGroup) mInflater.inflate(R.layout.activity_pedo_left, null);
        centerPanel = (ViewGroup) mInflater.inflate(R.layout.activity_pedo, null);
        rightPanel = (ViewGroup) mInflater.inflate(R.layout.activity_pedo_right, null);
        listViews.add(leftPanel);
        listViews.add(centerPanel);
        listViews.add(rightPanel);
        InitViewsListener(leftPanel, centerPanel, rightPanel);	// for pages actions
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }
    
    private void InitViewsListener(ViewGroup leftPanel, final ViewGroup centerPanel, ViewGroup rightPanel) {
    	// init start distribution
		ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted()-0.1, lastSpeedState));
		lastSpeedState = SpeedAdjuster.SA_NORMAL;
		ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted(), lastSpeedState));

    	// rename left cells
    	TextView cell_text = (TextView)leftPanel.findViewById(R.id.pedo_left_cell2).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("miles");
    	cell_text = (TextView)leftPanel.findViewById(R.id.pedo_left_cell3).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("steps/min");
    	cell_text = (TextView)leftPanel.findViewById(R.id.pedo_left_cell4).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("miles/hour");
    	cell_text = (TextView)leftPanel.findViewById(R.id.pedo_left_cell5).findViewById(R.id.pedo_left_block_desc);
    	cell_text.setText("calories");
    	left_steps = (TextView)leftPanel.findViewById(R.id.pedo_left_cell1).findViewById(R.id.pedo_left_block_number);
    	left_miles = (TextView)leftPanel.findViewById(R.id.pedo_left_cell2).findViewById(R.id.pedo_left_block_number);
    	left_stepsPerMin = (TextView)leftPanel.findViewById(R.id.pedo_left_cell3).findViewById(R.id.pedo_left_block_number);
    	left_milesPerHour = (TextView)leftPanel.findViewById(R.id.pedo_left_cell4).findViewById(R.id.pedo_left_block_number);
    	left_calories = (TextView)leftPanel.findViewById(R.id.pedo_left_cell5).findViewById(R.id.pedo_left_block_number);
		// center page controls
		gForce=(TextView)centerPanel.findViewById(R.id.tw_pedo_gf);
		pedoSteps=(TextView)centerPanel.findViewById(R.id.tw_pedo_steps);
		av_duration=(TextView)centerPanel.findViewById(R.id.tw_pedo_av_duration);
		final ImageView btn_ru  = (ImageView)centerPanel.findViewById(R.id.mus_btn_rampup);
		final ImageView btn_f1 = (ImageView)centerPanel.findViewById(R.id.mus_btn_filler);
		final ImageView btn_rn  = (ImageView)centerPanel.findViewById(R.id.mus_btn_rampnormal);
		final ImageView btn_f2 = (ImageView)centerPanel.findViewById(R.id.mus_btn_filler2);
		final ImageView btn_rd  = (ImageView)centerPanel.findViewById(R.id.mus_btn_rampdown);
		gForce.setText(Environment.getExternalStorageDirectory().toString());
		// right page text and button
		// assume quick start goal at start, could be replaced by other goals at InitExtra
        goal = new QuickStartGoal(){
			public void updateGoalStateCallback(){
				rht_main_text.setText(this.getText());
			}
		};
		TextView rht_text = (TextView)rightPanel.findViewById(R.id.pedo_right_title);
		rht_text.setText(goal.getTitle());
		rht_text = (TextView)rightPanel.findViewById(R.id.pedo_right_placeholder);
		rht_text.setText(goal.getPlaceholder());
		ConsistentContents.currentStatInfo.journeyType = goal.getTitle();
		rht_main_text = (TextView)rightPanel.findViewById(R.id.pedo_right_maintext);
		Button btn_end = (Button)rightPanel.findViewById(R.id.pedo_right_halt);
		btn_end.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				// stop soundtouch component and else, then launch ResultActivity
				ConsistentContents.playerInfoHolder.player.stop();
				pedo.stopSensor();
				goal.pauseGoal();

				// settle the last distribution state and store to file
				ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted(), lastSpeedState));
				ConsistentContents.currentStatInfo.journeyTime = goal.getText();
				Log.v("tts",String.valueOf(ConsistentContents.aggRecords.totalSteps));
				succeed_pa_list_to_cc();//////////////////////
				ConsistentContents.aggRecords.addCurrentRecord();
				
				Intent intent = new Intent(MusicActivity.this, ResultActivity.class);
				startActivity(intent);
				Log.v("tts",String.valueOf(ConsistentContents.aggRecords.totalSteps));
				MusicActivity.this.finish();
			}
		});
				

		
		// center page graph
		// init example series data
		final GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {new GraphViewData(0, 0.0d)});		
		final GraphView graphView = new LineGraphView(this, "");
		graphView.addSeries(exampleSeries); // data
		graphView.setScrollable(true);		// for appending
		// optional - set view port; size matched with data stored, start at y-value 0
		graphView.setViewPort(0, 49);
		graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.chart_text)); // set text size
		graphView.getGraphViewStyle().setGridColor(Color.WHITE);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
		
		// fire up the chart
		LinearLayout layout = (LinearLayout)centerPanel.findViewById(R.id.chart_container);
		layout.addView(graphView);
		// init time_axis
		time_axis = 0;		

        // init Pedometer, a new thread is preserved for graph updating action
		// it is running at 100ms = 10Hz
        pedo = new Pedometer(MusicActivity.this, 100){
        	// for immediate actions
        	public void onSensorChangedCallback(float g){
        		gForce.setText("G-Force: " + g);
        	}
        	// for lengthier thread action
        	public void PedoThreadCallback(int st, float threshold, float s_duration){
        		// set left page values
        		ConsistentContents.currentStatInfo.setTotalSteps(st);
        		ConsistentContents.currentStatInfo.addTime(0.1);	// 0.1 * 10 = 1 sec
        		left_steps.setText(""+st);
        		left_miles.setText(""+ConsistentContents.currentStatInfo.getDistanceTravelled());	// side effect : changed miles field
        		left_stepsPerMin.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getStepPerTime()));
        		left_milesPerHour.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getDistancePerTime()));
        		left_calories.setText(""+roundOneDecimal(ConsistentContents.currentStatInfo.getCaloriesBurn()));

        		// graph 
				time_axis += 1d;
				exampleSeries.appendData(new GraphViewData(time_axis, pForce), true, 50 );
				pedoSteps.setText("Steps taken: " + st+"; Th: " + threshold);
				av_duration.setText("Av. Step Duration: "+s_duration);
				// change UI reacting the speed
				switch(SpeedAdjuster.react(pedo, ConsistentContents.playerInfoHolder.player.getSoundTouchPlayable())){
					case	SpeedAdjuster.SA_FAST:
						btn_ru.setVisibility(View.INVISIBLE);
						btn_f1.setVisibility(View.INVISIBLE);
						btn_rn.setVisibility(View.INVISIBLE);
						btn_f2.setVisibility(View.VISIBLE);
						btn_rd.setVisibility(View.VISIBLE);
						if(lastSpeedState != SpeedAdjuster.SA_FAST){
							ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted()-0.1, lastSpeedState));
							lastSpeedState = SpeedAdjuster.SA_FAST;
							ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted(), lastSpeedState));
						}
						break;
					case	SpeedAdjuster.SA_NORMAL:
						btn_ru.setVisibility(View.INVISIBLE);
						btn_f1.setVisibility(View.VISIBLE);
						btn_rn.setVisibility(View.VISIBLE);
						btn_f2.setVisibility(View.VISIBLE);
						btn_rd.setVisibility(View.INVISIBLE);
						if(lastSpeedState != SpeedAdjuster.SA_NORMAL){
							ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted()-0.1, lastSpeedState));
							lastSpeedState = SpeedAdjuster.SA_NORMAL;
							ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted(), lastSpeedState));
						}
						break;
					case	SpeedAdjuster.SA_SLOW:
						btn_ru.setVisibility(View.VISIBLE);
						btn_f1.setVisibility(View.VISIBLE);
						btn_rn.setVisibility(View.INVISIBLE);
						btn_f2.setVisibility(View.INVISIBLE);
						btn_rd.setVisibility(View.INVISIBLE);
						if(lastSpeedState != SpeedAdjuster.SA_SLOW){
							ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted()-0.1, lastSpeedState));
							lastSpeedState = SpeedAdjuster.SA_SLOW;
							ConsistentContents.currentStatInfo.pace_dist.add(new GraphViewData(ConsistentContents.currentStatInfo.getTimeLasted(), lastSpeedState));
						}
						break;
				}
        	}
        };
        // center trigger button
        RelativeLayout btn_pp = (RelativeLayout)centerPanel.findViewById(R.id.mus_btn_trigger);
        btn_pp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!ConsistentContents.playerInfoHolder.player.isPlaying()){
					ConsistentContents.playerInfoHolder.player.start();
					ConsistentContents.playerInfoHolder.setStarted(true);
			        pedo.startSensor();
					goal.startGoal(1000);
					btn_pause.setVisibility(View.VISIBLE);
					btn_play.setVisibility(View.GONE);
				}else{
					ConsistentContents.playerInfoHolder.player.pause();
					ConsistentContents.playerInfoHolder.setStarted(false);
					pedo.stopSensor();
					goal.pauseGoal();
					btn_pause.setVisibility(View.GONE);
					btn_play.setVisibility(View.VISIBLE);
				}
			}        	
        });
        
        // fire up the player
 		try {
 			// set music to the first file
 			Singleton_PlayerInfoHolder.getInstance().currentFile = Singleton_PlayerInfoHolder.getInstance().currentList.getPath(0);
 			ConsistentContents.playerInfoHolder.player = new STMediaPlayer(this);
 			// and replace the album art and song info
 			//updateAlbumArtAndInfo();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
	}

	private void updateAlbumArtAndInfo() {
		// TODO Auto-generated method stub
		// init the textviews
		vp_song_name = (TextView) findViewById(R.id.pedo_vp_songname);
		vp_artist_name = (TextView) findViewById(R.id.pedo_vp_artistname);
		vp_album_name = (TextView) findViewById(R.id.pedo_vp_albumname);
		vp_song_name.setText(Singleton_PlayerInfoHolder.allSongsList.getTitle(Singleton_PlayerInfoHolder.currentFile));
		vp_artist_name.setText(Singleton_PlayerInfoHolder.allSongsList.getArtist(Singleton_PlayerInfoHolder.currentFile));
		vp_album_name.setText(Singleton_PlayerInfoHolder.allSongsList.getAlbum(Singleton_PlayerInfoHolder.currentFile));
        // album art part; to resize after knowing the actual image height
        ivAlbumArt = (ImageView) findViewById(R.id.pedo_vp_collapse_albumart);
		ConsistentContents.playerInfoHolder.setAlbumArt(ivAlbumArt, Singleton_PlayerInfoHolder.currentFile, false);
        ViewTreeObserver vto = ivAlbumArt.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                finalHeight = ivAlbumArt.getMeasuredHeight();
                ivAlbumArt.getLayoutParams().width = finalHeight;
                return true;
            }
        });
	}

	void succeed_pa_list_to_cc(){
		Global_value gv = (Global_value) getApplicationContext();		//put personal_ach id into statistic info
		ArrayList<Integer> pa1 = gv.PA.check_if_achieve("step",		
				ConsistentContents.aggRecords.totalSteps+(int)ConsistentContents.currentStatInfo.getTotalSteps());
		Log.v("pa1",String.valueOf(pa1.size()));
		ArrayList<Integer> pa2 = gv.PA.check_if_achieve("time",
				(int) (ConsistentContents.aggRecords.totalSec +ConsistentContents.currentStatInfo.getTimeLasted())/60);
		Log.v("pa2",String.valueOf(pa2.size()));
		ArrayList<Integer> pa3 = gv.PA.check_if_achieve("dist",
				(int)ConsistentContents.aggRecords.totalMiles+(int)ConsistentContents.currentStatInfo.getDistanceTravelled());
		Log.v("pa3",String.valueOf(pa3.size()));
		ConsistentContents.currentStatInfo.ach_list=pa1;
		ConsistentContents.currentStatInfo.ach_list.addAll(pa2);
		ConsistentContents.currentStatInfo.ach_list.addAll(pa3);
		for(int i=0;i< ConsistentContents.currentStatInfo.ach_list.size();i++)
		gv.PA.store_record(ConsistentContents.currentStatInfo.ach_list.get(i), ConsistentContents.currentStatInfo.getDateString());
	}
    
	public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }
    }
    
    private void InitImageView() {
    	// cursor part for correct indicator width
    	ivCursor = (ImageView) findViewById(R.id.pedo_vp_cursor);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
        ivCursorWidth = BitmapFactory.decodeResource(getResources(), R.drawable.viewpager_tab).getWidth();
        tabWidth = screenW / listViews.size();
        ivCursor.getLayoutParams().width = tabWidth;
        ivCursorWidth = tabWidth;
        offsetX = (tabWidth - ivCursorWidth) / 2;
    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
        	Animation animation;
            // initially, current index is same as translate-to index
            if(arg0 == currIndex){
                animation = new TranslateAnimation(tabWidth * currIndex + offsetX, tabWidth * arg0 + offsetX, -3, 0);
            }else{
                animation = new TranslateAnimation(tabWidth * currIndex + offsetX, tabWidth * arg0 + offsetX, 0, 0);              	
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(350);
            ivCursor.startAnimation(animation);
        }

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
    }
    
    double roundOneDecimal(double d) { 
        DecimalFormat twoDForm = new DecimalFormat("#.#"); 
        return Double.valueOf(twoDForm.format(d));
    }  

}
