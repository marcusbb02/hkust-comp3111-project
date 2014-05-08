package com.comp3111.pacekeeper;

import java.io.IOException;
import java.util.List;

import com.comp3111.achievement.AchievementActivity;
import com.comp3111.local_database.JSONHandler;
import com.comp3111.pacekeeper.musicplayerpackage.STMediaPlayer;
import com.comp3111.pacekeeper.musicplayerpackage.Singleton_PlayerInfoHolder;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.ui.ExpandAnimation;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnInviteListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GreetActivity extends Activity implements OnClickListener{
	
	// global accessible elements
	View panel_anim;
	Intent intent;
	int screenW, screenH;
	private SimpleFacebook mSimpleFacebook;
	protected static final String TAG = GreetActivity.class.getName();
	private TextView mTextStatus;
	
	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	private OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			//mTextStatus.setText(reason);
			Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {
			//mTextStatus.setText("Exception: " + throwable.getMessage());
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
			//mTextStatus.setText("Thinking...");
			
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			//mTextStatus.setText("Logged in");
			ConsistentContents.fblogin=true;
			toast("You are logged in");
		}

		@Override
		public void onNotAcceptingPermissions(Permission.Type type) {
			toast(String.format("You didn't accept %s permissions", type.name()));
		}
	};
	OnLogoutListener onLogoutListener = new OnLogoutListener() {
		@Override
		public void onFail(String reason) {
		//	mTextStatus.setText(reason);
			Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {
		//	mTextStatus.setText("Exception: " + throwable.getMessage());
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
		//	mTextStatus.setText("Thinking...");
		}

		
		@Override
	    public void onLogout() {
	        Log.i(TAG, "You are logged out");
	    }

	    /* 
	     * You can override other methods here: 
	     * onThinking(), onFail(String reason), onException(Throwable throwable)
	     */ 
	};
	final OnInviteListener onInviteListener = new OnInviteListener() {

		@Override
		public void onFail(String reason) {
			// insure that you are logged in before inviting
			Log.w(TAG, reason);
		}

		@Override
		public void onException(Throwable throwable) {
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onComplete(List<String> invitedFriends, String requestId) {
			toast("Invitation was sent to " + invitedFriends.size() + " users, invite request=" + requestId);
		}

		@Override
		public void onCancel() {
			toast("Canceled the dialog");
		}

	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data); 
	    super.onActivityResult(requestCode, resultCode, data);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greet);
		
		// load up previous states from files
		JSONHandler.readAggregatedRecord();
		
		// find the display attributes
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
		
		// set up lower ImageButtons onClick action
		ImageButton greetStatAndAchievement = (ImageButton)findViewById(R.id.greet_stat);
		ImageButton greetCalibrate = (ImageButton)findViewById(R.id.greet_profilelist);
		ImageButton greetOptions = (ImageButton)findViewById(R.id.greet_options);
		greetCalibrate.setOnClickListener(this);
		greetStatAndAchievement.setOnClickListener(this);
		greetOptions.setOnClickListener(this);

		// initialize panel animations
		panel_anim = (View)findViewById(R.id.greet_animate_panel);
		panel_anim.setVisibility(View.VISIBLE);
		panel_anim.setVisibility(View.INVISIBLE);
		
		// initialize music info
 		Singleton_PlayerInfoHolder.loadLists(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    int x = (int)event.getX();
	    int y = (int)event.getY();
	    switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	clicktest(x,y);
	        case MotionEvent.ACTION_MOVE:
	        case MotionEvent.ACTION_UP:
	    }
	    return false;
	}
	
	public void clicktest(int x, int y){
		if(x > screenW / 2){
			panel_anim.setBackgroundColor(getResources().getColor(R.drawable.color_green));
			intent = new Intent(this, GoalActivity.class);
		}else{
			panel_anim.setBackgroundColor(getResources().getColor(R.drawable.color_red));
			intent = new Intent(this, MusicActivity.class);
		}
		ExpandAnimation panel_anim_expand = new ExpandAnimation(panel_anim, 500, ExpandAnimation.EXPAND, x - 25, y - 100, screenH, screenW);
		panel_anim_expand.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				GreetActivity.this.startActivity(GreetActivity.this.intent);
				// animation for leaving activity -> animation for incoming activity
				overridePendingTransition(R.anim.fade_in, R.anim.hold);
				//GreetActivity.this.finish();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panel_anim.startAnimation(panel_anim_expand);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.greet, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//mSimpleFacebook.logout(onLogoutListener);
		switch (item.getItemId()) {
		case R.id.invite:
			// app icon in action bar clicked; go home
			
			//if (ConsistentContents.fblogin == false) {
		   if  (mSimpleFacebook.isLogin()==false){
			Toast.makeText(this, "Please login first", Toast.LENGTH_LONG)
						.show();
				mSimpleFacebook.login( mOnLoginListener);
			} else
				mSimpleFacebook.invite("I invite you to use this app", onInviteListener, "secret data");
			return true;
		
		
		default:

			return super.onOptionsItemSelected(item);

		}
	}

	
	@Override
	public void onResume(){
		super.onResume();
		 mSimpleFacebook = SimpleFacebook.getInstance(this);
		// hide panel_anim block
		if(panel_anim != null){
			panel_anim.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		// View OnClick case switch
		switch(view.getId()){
		case R.id.greet_stat:
			intent = new Intent(this, AchievementActivity.class);
			startActivity(intent);
			break;
		case R.id.greet_profilelist:
			intent = new Intent(this, ProfileListActivity.class);
			startActivity(GreetActivity.this.intent);
			
			break;
		case R.id.greet_options:
			intent = new Intent(this, SettingActivity.class);
			startActivity(GreetActivity.this.intent);
			break;
		}
	}
}
