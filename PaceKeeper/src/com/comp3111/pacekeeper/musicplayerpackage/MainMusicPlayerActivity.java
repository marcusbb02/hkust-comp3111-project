package com.comp3111.pacekeeper.musicplayerpackage;

import com.comp3111.pacekeeper.R;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment
 * activity that maintains a TabHost using a ViewPager.
 */
public class MainMusicPlayerActivity extends FragmentActivity {

	private Singleton_TabInfoHolder tabInfoHolder = Singleton_TabInfoHolder
			.getInstance();
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainMusicPlayerActivity.TabInfo>();
	private PagerAdapter mPagerAdapter;

	/**
	 * Maintains extrinsic info of a tab's construct
	 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private ListFragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	/**
	 * A simple factory that returns dummy views to the Tabhost
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;

		}

	}

	// Music part starts
	private static final int UPDATE_FREQUENCY = 50;
	private static final int STEP_VALUE = 4000;
	private static final int LENGTH_LONG = 0;
	private static final int LENGTH_SHORT = 0;

	private TextView songInfoTextView = null;
	private SeekBar seekbar = null;

	private ImageButton playButton = null;
	private ImageButton prevButton = null;
	private ImageButton nextButton = null;
	private ImageView showAlbumArtButton = null;

	private Singleton_PlayerInfoHolder playerInfoHolder = Singleton_PlayerInfoHolder
			.getInstance();

	private final Handler handler = new Handler();

	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};
	public void selectSong(String filePath){
		playerInfoHolder.currentFile=filePath;
		playerInfoHolder.setAlbumArt(showAlbumArtButton, filePath, false);

		songInfoTextView.setText(playerInfoHolder.allSongsList
				.getTitle(playerInfoHolder.currentFile)
				+ "-"
				+ playerInfoHolder.allSongsList
						.getArtist(playerInfoHolder.currentFile));
		seekbar.setProgress(0);
	}
	
	public void startPlay(String filePath) {
		Log.i("Selected: ", filePath);

		playerInfoHolder.setAlbumArt(showAlbumArtButton, filePath, false);

		// selectedFile.setText(songsList.getTitle(listPosition)
		// + "-" + songsList.getArtist(listPosition));

		songInfoTextView.setText(playerInfoHolder.allSongsList
				.getTitle(playerInfoHolder.currentFile)
				+ "-"
				+ playerInfoHolder.allSongsList
						.getArtist(playerInfoHolder.currentFile));

		playerInfoHolder.player.stop();
		playerInfoHolder.player.reset();

		try {
			playerInfoHolder.player.setDataSource(filePath);
			playerInfoHolder.player.prepare();
			playerInfoHolder.player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		seekbar.setProgress(0);
		
		seekbar.setMax(playerInfoHolder.player.getDuration());

		playButton.setImageResource(R.drawable.ic_action_pause);

		updatePosition();

		playerInfoHolder.setStarted(true);
		playerInfoHolder.player.setOnCompletionListener(MainMusicPlayerActivity.this, onCompletion);
	}

	private void stopPlay() {
		playerInfoHolder.player.stop();
		playerInfoHolder.player.reset();

		songInfoTextView.setText("No song selected");

		playButton.setImageResource(R.drawable.ic_action_play);
		showAlbumArtButton.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_expandplayer_placeholder));
		handler.removeCallbacks(updatePositionRunnable);
		seekbar.setProgress(0);

		playerInfoHolder.setStarted(false);
		Log.i("stopPlay", "Song stopped");
	}

	private void updatePosition() {
		handler.removeCallbacks(updatePositionRunnable);

		seekbar.setProgress(playerInfoHolder.player.getCurrentPosition());

		handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(tabInfoHolder.fragmentInflated){
			
			tabInfoHolder.fragmentInflated=false;
		}
		else{
		overridePendingTransition(R.anim.hold, R.anim.slide_out_to_above);
		handler.removeCallbacks(updatePositionRunnable);
		finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(updatePositionRunnable);
		/*
		 * playerInfoHolder.player.stop(); playerInfoHolder.player.reset();
		 * playerInfoHolder.player.release();
		 * 
		 * playerInfoHolder.player = null;
		 */
	}

	private com.smp.soundtouchandroid.SoundTouchPlayable.OnCompleteListener onCompletion = new com.smp.soundtouchandroid.SoundTouchPlayable.OnCompleteListener() {

		@Override
		public void onCompletion() {
			stopPlay();
			
			if (playerInfoHolder.currentFile != null) {
				switch (playerInfoHolder.repeatMode) {

				// Repeat all
				case 2: {
					playerInfoHolder.currentFile = playerInfoHolder.currentList
							.nextFileLoop(playerInfoHolder.currentFile);
					startPlay(playerInfoHolder.currentFile);
					break;
				}
				// Repeat once
				case 1: {
					startPlay(playerInfoHolder.currentFile);
					break;
				}
				// No repeat
				case 0: {
					playerInfoHolder.currentFile = playerInfoHolder.currentList
							.nextFile(playerInfoHolder.currentFile);
					if (playerInfoHolder.currentFile != null)
						startPlay(playerInfoHolder.currentFile);
					else{
						selectSong(playerInfoHolder.currentList.getPath(0));
					}
					break;
				}
				}
			}
		}
	};

	private View.OnClickListener onButtonClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case (R.id.play): {
				if (playerInfoHolder.player.isPlaying()) {

					handler.removeCallbacks(updatePositionRunnable);
					playerInfoHolder.player.pause();
					playButton.setImageResource(R.drawable.ic_action_play);
				} else {
					if (playerInfoHolder.isStarted()) {

						playerInfoHolder.player.start();
						playButton.setImageResource(R.drawable.ic_action_pause);
						updatePosition();

					} else {
						if (playerInfoHolder.currentFile != null) {
							startPlay(playerInfoHolder.currentFile);
						} else {
							Toast.makeText((Activity) v.getContext(),
									"Please select a music!", LENGTH_SHORT)
									.show();
						}
					}
				}

				break;
			}
			case R.id.next: {
				if (playerInfoHolder.currentFile != null) {
					switch (playerInfoHolder.repeatMode) {

					// Repeat all
					case 2: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.nextFileLoop(playerInfoHolder.currentFile);
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// Repeat once
					case 1: {
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// No repeat
					case 0: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.nextFile(playerInfoHolder.currentFile);
						if (playerInfoHolder.currentFile == null) {
							Toast.makeText((Activity) v.getContext(),
									"This is the last song!", LENGTH_SHORT)
									.show();
							
						stopPlay();
						selectSong(playerInfoHolder.currentList.getPath(0));
						} else
							startPlay(playerInfoHolder.currentFile);
						break;
					}
					}
				} else {
					stopPlay();
					Toast.makeText((Activity) v.getContext(),
							"Please select a music!", LENGTH_SHORT).show();
				}

				break;
			}
			case R.id.prev: {
				// int seekto = player.getCurrentPosition() - STEP_VALUE;
				//
				// if (seekto < 0)
				// seekto = 0;
				//
				// player.pause();
				// player.seekTo(seekto);
				// player.start();

				// currentFile = songsList.getPath(songsList
				// .matchWithPath(currentFile) - 1);
				if (playerInfoHolder.currentFile != null) {
					switch (playerInfoHolder.repeatMode) {

					// Repeat all
					case 2: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.prevFileLoop(playerInfoHolder.currentFile);
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// Repeat once
					case 1: {
						startPlay(playerInfoHolder.currentFile);
						break;
					}
					// No repeat
					case 0: {
						playerInfoHolder.currentFile = playerInfoHolder.currentList
								.prevFile(playerInfoHolder.currentFile);
						if (playerInfoHolder.currentFile == null) {
							Toast.makeText((Activity) v.getContext(),
									"This is the first song!", LENGTH_SHORT)
									.show();
							
							stopPlay();
							selectSong(playerInfoHolder.currentList.getPath(playerInfoHolder.currentList.getSize()-1));
						} else
							startPlay(playerInfoHolder.currentFile);
						break;
					}
					}

				} else {
					stopPlay();
					Toast.makeText((Activity) v.getContext(),
							"Please select a music!", LENGTH_SHORT).show();
				}

				break;
			}
			case R.id.showalbumart: {

				Intent intObj = new Intent(MainMusicPlayerActivity.this,
						AssoMusicPlayerActivity.class);

				startActivity(intObj);

				break;
			}
			}
		}
	};
	private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// returning false will call the OnCompletionListener
			return false;
		}
	};

	private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			playerInfoHolder.isMoveingSeekBar = false;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			playerInfoHolder.isMoveingSeekBar = true;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (playerInfoHolder.isMoveingSeekBar) {
				playerInfoHolder.player.seekTo(progress);
				Log.i("OnSeekBarChangeListener", "onProgressChanged");
			}
		}
	};

	// music part methods end

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Inflate the layout
		setContentView(R.layout.mainmusicplayeractivity_layout);
		// Singleton_PlayerInfoHolder.loadLists(this);
		// Singleton_PlayerInfoHolder.getInstance().currentFile =
		// Singleton_PlayerInfoHolder.getInstance().currentList.getPath(0);
		// Initialise the TabHost

		this.initialiseTabHost(savedInstanceState);
		this.intialiseViewPager();

		// Music part
		songInfoTextView = (TextView) findViewById(R.id.selectedfile);
		seekbar = (SeekBar) findViewById(R.id.seekbar);

		playButton = (ImageButton) findViewById(R.id.play);
		prevButton = (ImageButton) findViewById(R.id.prev);
		nextButton = (ImageButton) findViewById(R.id.next);
		showAlbumArtButton = (ImageView) findViewById(R.id.showalbumart);

		playerInfoHolder.player.setOnCompletionListener(MainMusicPlayerActivity.this, onCompletion);
		playerInfoHolder.player.setOnErrorListener(onError);
		seekbar.setOnSeekBarChangeListener(seekBarChanged);

		playButton.setOnClickListener(onButtonClick);
		nextButton.setOnClickListener(onButtonClick);
		prevButton.setOnClickListener(onButtonClick);
		showAlbumArtButton.setOnClickListener(onButtonClick);
		showAlbumArtButton.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_expandplayer_placeholder));
		songInfoTextView.setText("No song selected");
		return;
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", tabInfoHolder.mTabHost.getCurrentTabTag()); // save
																				// the
																				// tab
		// selected
		// outState.putString("currentFile", playerInfoHolder.currentFile);
		Log.i("onSaveInstanceState", "currentFile: "
				+ playerInfoHolder.currentFile);
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			tabInfoHolder.mTabHost.setCurrentTabByTag(savedInstanceState
					.getString("tab")); // set the tab as per the saved state
			// playerInfoHolder.currentFile =
			// savedInstanceState.getString("currentFile");
			Log.i("onRestoreInstanceState", "currentFile: "
					+ playerInfoHolder.currentFile);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		playerInfoHolder.player.setOnCompletionListener(MainMusicPlayerActivity.this, onCompletion);
		playerInfoHolder.player.setOnErrorListener(onError);
		if (playerInfoHolder.currentFile != null) {
			playerInfoHolder.setAlbumArt(showAlbumArtButton,
					playerInfoHolder.currentFile, false);
			songInfoTextView.setText(playerInfoHolder.allSongsList
					.getTitle(playerInfoHolder.currentFile)
					+ "-"
					+ playerInfoHolder.allSongsList
							.getArtist(playerInfoHolder.currentFile));
			
			seekbar.setProgress(0);
			
			seekbar.setMax(playerInfoHolder.player.getDuration());

			if(playerInfoHolder.player.isPlaying()){
				playButton.setImageResource(R.drawable.ic_action_pause);
			}
			else{
				playButton.setImageResource(R.drawable.ic_action_play);
			}
			updatePosition();
		}
		else{
			showAlbumArtButton.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_expandplayer_placeholder));
			songInfoTextView.setText("No song selected");
			seekbar.setProgress(0);
		}
	}

	/**
	 * Initialise ViewPager
	 */
	private void intialiseViewPager() {

		List<ListFragment> fragments = new Vector<ListFragment>();

		fragments.add((ListFragment) ListFragment.instantiate(this,
				ListFragment_SortBySongTitle.class.getName()));
		fragments.add((ListFragment) ListFragment.instantiate(this,
				ListFragment_SortByArtist.class.getName()));
		fragments.add((ListFragment) ListFragment.instantiate(this,
				ListFragment_SortByAlbum.class.getName()));
		fragments.add((ListFragment) ListFragment.instantiate(this,
				ListFragment_SortByPlayList.class.getName()));

		this.mPagerAdapter = new PagerAdapter(
				super.getSupportFragmentManager(), fragments);

		//
		tabInfoHolder.mViewPager = (CustomViewPager) super
				.findViewById(R.id.tabviewpager);
		tabInfoHolder.mViewPager.setAdapter(this.mPagerAdapter);
		tabInfoHolder.mViewPager
				.setOnPageChangeListener(myOnPageChangeListener);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {

		tabInfoHolder.mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabInfoHolder.mTabHost.setup();
		TabInfo tabInfo = null;

		MainMusicPlayerActivity
				.AddTab(this, tabInfoHolder.mTabHost, tabInfoHolder.mTabHost
						.newTabSpec("Tab1").setIndicator("Songs"),
						(tabInfo = new TabInfo("Tab1",
								ListFragment_SortBySongTitle.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainMusicPlayerActivity.AddTab(this, tabInfoHolder.mTabHost,
				tabInfoHolder.mTabHost.newTabSpec("Tab2")
						.setIndicator("Artist"), (tabInfo = new TabInfo("Tab2",
						ListFragment_SortByArtist.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainMusicPlayerActivity
				.AddTab(this, tabInfoHolder.mTabHost, tabInfoHolder.mTabHost
						.newTabSpec("Tab3").setIndicator("Album"),
						(tabInfo = new TabInfo("Tab3",
								ListFragment_SortByAlbum.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainMusicPlayerActivity.AddTab(
				this,
				tabInfoHolder.mTabHost,
				tabInfoHolder.mTabHost.newTabSpec("Tab3").setIndicator(
						"Playlist"), (tabInfo = new TabInfo("Tab4",
						ListFragment_SortByPlayList.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		// Default to first tab
		// this.onTabChanged("Tab1");
		//
		tabInfoHolder.mTabHost.setOnTabChangedListener(myOnTabChangedListener);

	}

	/**
	 * Add Tab content to the Tabhost
	 * 
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void AddTab(MainMusicPlayerActivity activity,
			TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	private OnTabChangeListener myOnTabChangedListener = new OnTabChangeListener() {
		Singleton_TabInfoHolder tabInfoHolder = Singleton_TabInfoHolder
				.getInstance();

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
		 */
		public void onTabChanged(String tag) {

			// TabInfo newTab = this.mapTabInfo.get(tag);
			int pos = tabInfoHolder.mTabHost.getCurrentTab();
			tabInfoHolder.mViewPager.setCurrentItem(pos);
		}
	};

	private OnPageChangeListener myOnPageChangeListener = new android.support.v4.view.ViewPager.OnPageChangeListener() {
		Singleton_TabInfoHolder tabInfoHolder = Singleton_TabInfoHolder
				.getInstance();

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			tabInfoHolder.mTabHost.setCurrentTab(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}
	};
}
