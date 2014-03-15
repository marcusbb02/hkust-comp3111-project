package com.comp3111.pacekeeper;

import java.util.ArrayList;
import java.util.List;

import com.comp3111.swipeview.SwipeDismissTouchListener;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicActivity extends Activity {
	
	// For ViewPager
	private ViewPager mPager;
    private List<View> listViews;
    private TextView t1, t2, t3;
    private ViewGroup leftPanel, centerPanel, rightPanel;
    private int currIndex = 1;	// start off from middle page
    private int ivCursorWidth;
    private int tabWidth;
    private int offsetX;
    private ImageView ivCursor;
    
    // For things inside ViewPager
    private LinearLayout mus_player_buttons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedo_viewpager);
		InitViewPager();
		InitImageView();
		Log.i("tag", ivCursor.getLayoutParams().width + " is the width");
        mPager.setCurrentItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calibrate, menu);
		return true;
	}
	
	// Methods for ViewPager
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };
    
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        leftPanel = (ViewGroup) mInflater.inflate(R.layout.activity_calibrate, null);
        centerPanel = (ViewGroup) mInflater.inflate(R.layout.activity_pedo, null);
        rightPanel = (ViewGroup) mInflater.inflate(R.layout.activity_goal, null);
        listViews.add(leftPanel);
        listViews.add(centerPanel);
        listViews.add(rightPanel);
        InitViewsListener(centerPanel);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }
    
    private void InitViewsListener(ViewGroup leftPanel) {
		mus_player_buttons = (LinearLayout)leftPanel.findViewById(R.id.mus_player_buttons);
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
        public void finishUpdate(View arg0) {
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

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
    
    private void InitImageView() {
    	ivCursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenW = dm.widthPixels;
        ivCursorWidth = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher).getWidth();
        tabWidth = screenW / listViews.size();
        if (ivCursorWidth > tabWidth) {
            ivCursor.getLayoutParams().width = tabWidth;
            ivCursorWidth = tabWidth;
        }
        offsetX = (tabWidth - ivCursorWidth) / 2;
    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
        	Animation animation;
            // initially, current index is same as translate-to index
            if(arg0 == currIndex){
                animation = new TranslateAnimation(tabWidth * currIndex + offsetX, tabWidth * arg0 + offsetX, -100, 0);
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
    }
}
