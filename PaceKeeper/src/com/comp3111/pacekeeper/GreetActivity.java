package com.comp3111.pacekeeper;

import com.comp3111.ui.ExpandAnimation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class GreetActivity extends Activity implements OnTouchListener{
	
	// global accessible elements
	View left_panel;
	View right_panel;
	View panel_anim;
	Intent intent;
	int screenW, screenH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greet);
		
		// find the middle position
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
		
		// set up panels onClick action
		left_panel = (View)findViewById(R.id.greet_left_panel);
		//left_panel.setOnClickListener(this);
		right_panel = (View)findViewById(R.id.greet_right_panel);
		//right_panel.setOnClickListener(this);
		// initialize panel animations
		panel_anim = (View)findViewById(R.id.greet_animate_panel);
		panel_anim.setVisibility(View.VISIBLE);
		panel_anim.setVisibility(View.INVISIBLE);
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
		ExpandAnimation panel_anim_expand = new ExpandAnimation(panel_anim, 500, ExpandAnimation.EXPAND, x - 25, y - 100);
		panel_anim_expand.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				GreetActivity.this.startActivity(GreetActivity.this.intent);
				overridePendingTransition(R.anim.hold, R.anim.fade_in_anim);
				GreetActivity.this.finish();
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
	public boolean onTouch(View view, MotionEvent arg1) {
		// TODO Auto-generated method stub// View OnClick case switch
		switch(view.getId()){
		case R.id.greet_left_panel:
	        //panel_anim_rc = new ExpandAnimation(right_panel, 500, ExpandAnimation.COLLAPSE);
	        //right_panel.startAnimation(panel_anim_rc);
			//panel_anim.startAnimation(panel_anim_expand);
			break;
		case R.id.greet_right_panel:
	        //panel_anim_lc = new ExpandAnimation(left_panel, 500, ExpandAnimation.COLLAPSE);
	        //left_panel.startAnimation(panel_anim_lc);
			//panel_anim.startAnimation(panel_anim_expand);
			break;
	}
		return false;
	}

}
