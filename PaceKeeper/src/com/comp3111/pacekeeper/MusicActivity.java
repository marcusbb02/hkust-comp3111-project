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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
    private int tabWidth, screenW, screenH, offsetX;
    private ImageView ivCursor, ivAlbumArt;
    int finalHeight, actionBarHeight;
    
    // For things inside ViewPager
    private LinearLayout mus_player_buttons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedo_viewpager);
		InitViewPager();
		InitImageView();	// for page cursor and album art resizing
        mPager.setCurrentItem(1);
        InitPostView();		// for setting up view's position dynamically
	}
	
	private void InitPostView(){
        //find actionbar size
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

		LinearLayout collpased_player_layout = (LinearLayout)findViewById(R.id.pedo_vp_collpase_player_layout);
		LinearLayout collpase_player_layout_placeholder = (LinearLayout)findViewById(R.id.pedo_vp_collpase_player_layout_placeholder);
		
		// set height of player
		collpase_player_layout_placeholder.getLayoutParams().height = (int) ((screenH - actionBarHeight) * 0.4);
		collpased_player_layout.getLayoutParams().height = (int) ((screenH - actionBarHeight) * 0.4);
		collpased_player_layout.bringToFront();
		collpased_player_layout.invalidate();
        // collapsed music player action
		collpased_player_layout.setOnTouchListener(new SwipeDismissTouchListener(
			collpased_player_layout,
			actionBarHeight,
			null,
			new SwipeDismissTouchListener.OnDismissCallback(){
				@Override
				public void onDismiss(View view, Object token) {
					// TODO Auto-generated method stub
					// should be problem of swipeView
					//view.setY(screenH - actionBarHeight - view.getHeight());
					//view.requestLayout();
				}
			}
		));
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
        mPager = (ViewPager) findViewById(R.id.pedo_vp_vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        leftPanel = (ViewGroup) mInflater.inflate(R.layout.activity_calibrate, null);
        centerPanel = (ViewGroup) mInflater.inflate(R.layout.activity_pedo, null);
        rightPanel = (ViewGroup) mInflater.inflate(R.layout.activity_goal, null);
        listViews.add(leftPanel);
        listViews.add(centerPanel);
        listViews.add(rightPanel);
        InitViewsListener(centerPanel);	// for pages actions
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }
    
    private void InitViewsListener(ViewGroup leftPanel) {
		mus_player_buttons = (LinearLayout)leftPanel.findViewById(R.id.mus_player_buttons);
		mus_player_buttons.setOnTouchListener(new SwipeDismissTouchListener(
			mus_player_buttons,
			actionBarHeight,
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
        // album art part; to resize after knowing the actual image height
        ivAlbumArt = (ImageView) findViewById(R.id.pedo_vp_collapse_albumart);
        ViewTreeObserver vto = ivAlbumArt.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                finalHeight = ivAlbumArt.getMeasuredHeight();
                ivAlbumArt.getLayoutParams().width = finalHeight;
                return true;
            }
        });
    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
        	Animation animation;
            // initially, current index is same as translate-to index
            if(arg0 == currIndex){
                animation = new TranslateAnimation(tabWidth * currIndex + offsetX, tabWidth * arg0 + offsetX, -12, 0);
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
