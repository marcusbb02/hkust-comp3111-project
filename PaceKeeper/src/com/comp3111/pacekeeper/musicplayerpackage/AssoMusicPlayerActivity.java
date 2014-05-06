package com.comp3111.pacekeeper.musicplayerpackage;

import com.comp3111.pacekeeper.R;
import java.io.IOException;

import com.comp3111.pacekeeper.musicplayerpackage.MyArrayAdaptor.InstantListViewHolder;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AssoMusicPlayerActivity extends Activity {

	private static final long UPDATE_FREQUENCY = 50;

	private Singleton_PlayerInfoHolder playerInfoHolder = Singleton_PlayerInfoHolder
			.getInstance();
	private ImageButton playButton = null;
	private ImageButton prevButton = null;
	private ImageButton nextButton = null;
	private ImageButton repeatButton = null;
	private ImageButton shuffleButton = null;
	private ImageButton backButton = null;
	private ImageView albumArtView = null;

	private boolean isListInflated;
	private int height;
	private int width;
	private ListView inflatedListView = null;

	private TextView textSongTitle = null;
	private TextView textArtist = null;
	private TextView textAlbum = null;
	private TextView textPrev = null;
	private TextView textNext = null;
	private SeekBar seekbar = null;

	private final Handler handler = new Handler();

	private final Runnable updatePositionRunnable2 = new Runnable() {
		public void run() {
			updatePosition2();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.assomusicplayeractivity_layout);

		textSongTitle = (TextView) findViewById(R.id.assoplayer_song_title);
		textArtist = (TextView) findViewById(R.id.assoplayer_artist);
		textAlbum = (TextView) findViewById(R.id.assoplayer_album);
		textPrev = (TextView) findViewById(R.id.assoplayer_prevSongDescription);
		textNext = (TextView) findViewById(R.id.assoplayer_nextSongDescription);
		seekbar = (SeekBar) findViewById(R.id.assoplayer_seekbar);
		playButton = (ImageButton) findViewById(R.id.assoplayer_play);
		prevButton = (ImageButton) findViewById(R.id.assoplayer_prev);
		nextButton = (ImageButton) findViewById(R.id.assoplayer_next);
		shuffleButton = (ImageButton) findViewById(R.id.assoplayer_shuffle);
		repeatButton = (ImageButton) findViewById(R.id.assoplayer_repeat);
		albumArtView = (ImageView) findViewById(R.id.assoplayer_showalbumart);
		inflatedListView = (ListView) findViewById(R.id.assoplayer_playlist);
		backButton = (ImageButton) findViewById(R.id.assoplayer_back);
		
		playButton.setOnClickListener(onButtonClick);
		nextButton.setOnClickListener(onButtonClick);
		prevButton.setOnClickListener(onButtonClick);
		repeatButton.setOnClickListener(onButtonClick);
		albumArtView.setOnClickListener(onButtonClick);
		shuffleButton.setOnClickListener(onButtonClick);
		albumArtView.setOnClickListener(onButtonClick);
		backButton.setOnClickListener(onButtonClick);
		seekbar.setOnSeekBarChangeListener(seekBarChanged);

		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				InstantListViewHolder holder = (InstantListViewHolder) view
						.getTag();

				Singleton_PlayerInfoHolder.getInstance().currentFile = (String) holder.path;
				if (Singleton_PlayerInfoHolder.getInstance().currentFile != null) {
					startPlay(Singleton_PlayerInfoHolder.getInstance().currentFile);
				}

			}
		};

		inflatedListView.setOnItemClickListener(onItemClickListener);

		SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(
				inflatedListView,
				new SwipeListViewTouchListener.OnSwipeCallback() {
					@Override
					public void onSwipeLeft(ListView listView,
							int[] reverseSortedPositions) {
						// Log.i(this.getClass().getName(),
						// "swipe left : pos="+reverseSortedPositions[0]);
						// TODO : YOUR CODE HERE FOR LEFT ACTION

					}

					@Override
					public void onSwipeRight(ListView listView,
							int[] reverseSortedPositions) {
						// Log.i(ProfileMenuActivity.class.getClass().getName(),
						// "swipe right : pos="+reverseSortedPositions[0]);
						// TODO : YOUR CODE HERE FOR RIGHT ACTION
						if (AssoMusicPlayerActivity.this.playerInfoHolder.currentList != null) {
							String songTitle = AssoMusicPlayerActivity.this.playerInfoHolder.currentList
									.getTitle(reverseSortedPositions[0]);

							Toast.makeText(
									AssoMusicPlayerActivity.this,
									songTitle
											+ " is deleted from current playlist successfully!",
									Toast.LENGTH_SHORT).show();

							if (AssoMusicPlayerActivity.this.playerInfoHolder.currentFile
									.equals(AssoMusicPlayerActivity.this.playerInfoHolder.currentList
											.getPath(reverseSortedPositions[0]))) {

								playerInfoHolder.currentFile = playerInfoHolder.currentList
										.nextFile(playerInfoHolder.currentFile);
								if (playerInfoHolder.currentFile == null) {
									Toast.makeText(
											(Activity) listView.getContext(),
											"This is the last song!",
											Toast.LENGTH_SHORT).show();
									AssoMusicPlayerActivity.this.stopPlay();
								} else {

									startPlay(playerInfoHolder.currentFile);
								}
							}
							AssoMusicPlayerActivity.this.playerInfoHolder.currentList
									.deleteSong(reverseSortedPositions[0]);
							setInstantPlayList();
							listView.setSelection(reverseSortedPositions[0] - 3);

						}
					}
				}, false, // example : left action =without dismiss
				true, // example : right action without dismiss animation
				false, false);
		inflatedListView.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		inflatedListView
				.setOnScrollListener(touchListener.makeScrollListener());

		resetStatus();
	}

	@Override
	protected void onResume() {
		super.onResume();
		playerInfoHolder.player.setOnCompletionListener(AssoMusicPlayerActivity.this, onCompletion);
		playerInfoHolder.player.setOnErrorListener(onError);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		handler.removeCallbacks(updatePositionRunnable2);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isListInflated) {
				isListInflated = false;
				height = inflatedListView.getHeight();
				width = inflatedListView.getWidth();

				inflatedListView.getLayoutParams().height = 0;
				inflatedListView.getLayoutParams().width = 0;
				inflatedListView.requestLayout();

				albumArtView.getLayoutParams().height = height;
				albumArtView.getLayoutParams().width = width;
				albumArtView.requestLayout();
				return true;
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Reset the state base on MusicPlayer_with_songslist
	 * 
	 */
	protected void resetStatus() {
		if (!playerInfoHolder.isStarted()) {
			albumArtView.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_expandplayer_placeholder));
		} else {
			updatePosition2();
			textSongTitle.setText(playerInfoHolder.allSongsList
					.getTitle(playerInfoHolder.currentFile));
			textArtist.setText(playerInfoHolder.allSongsList
					.getArtist(playerInfoHolder.currentFile));
			textAlbum.setText(playerInfoHolder.allSongsList
					.getAlbum(playerInfoHolder.currentFile));

			// album art part; to resize after knowing the actual image height
			ViewTreeObserver vto = albumArtView.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				public boolean onPreDraw() {

					int temp = albumArtView.getMeasuredWidth();
					albumArtView.getLayoutParams().height = temp;
					return true;
				}
			});
			playerInfoHolder.setAlbumArt(albumArtView,
					playerInfoHolder.currentFile, false);

			seekbar.setMax(playerInfoHolder.player.getDuration());
			if (playerInfoHolder.player.isPlaying()) {
				playButton.setImageResource(R.drawable.ic_action_pause);
			} else {
				playButton.setImageResource(R.drawable.ic_action_play);
			}
		}

		if (playerInfoHolder.isShuffle) {
			shuffleButton.setImageResource(R.drawable.ic_action_shuffle1);
		} else {
			shuffleButton.setImageResource(R.drawable.ic_action_mute);
		}

		switch (playerInfoHolder.repeatMode) {

		// Repeat all
		case 2: {
			repeatButton.setImageResource(R.drawable.ic_action_repeat_a);
			break;
		}
		// Repeat once
		case 1: {
			repeatButton.setImageResource(R.drawable.ic_action_repeat_1);
			break;
		}
		// No repeat
		case 0: {
			repeatButton.setImageResource(R.drawable.ic_action_repeat_once);
			break;
		}
		}
		setInstantPlayList();
	}

	protected void setInstantPlayList() {
		MyArrayAdaptor arrayAdaptor;
		if (playerInfoHolder.currentList != null) {
			arrayAdaptor = new MyArrayAdaptor(this,
					R.layout.musicplayer_listitem,
					playerInfoHolder.currentList.getTitleArray(), true);

		} else {
			arrayAdaptor = new MyArrayAdaptor(this,
					R.layout.musicplayer_listitem,
					new String[] { "No song selected" }, false);
		}
		inflatedListView.setAdapter(arrayAdaptor);
	}

	protected void updatePosition2() {
		handler.removeCallbacks(updatePositionRunnable2);

		seekbar.setProgress(playerInfoHolder.player.getCurrentPosition());

		handler.postDelayed(updatePositionRunnable2, UPDATE_FREQUENCY);
	}

	public void startPlay(String file) {
		Log.i("Selected: ", file);

		playerInfoHolder.setAlbumArt(albumArtView, file, false);
		ViewTreeObserver vto = albumArtView.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {

				int temp = albumArtView.getMeasuredWidth();
				albumArtView.getLayoutParams().height = temp;
				return true;
			}
		});

		textSongTitle.setText(playerInfoHolder.allSongsList
				.getTitle(playerInfoHolder.currentFile));
		textArtist.setText(playerInfoHolder.allSongsList
				.getArtist(playerInfoHolder.currentFile));
		textAlbum.setText(playerInfoHolder.allSongsList
				.getAlbum(playerInfoHolder.currentFile));

		seekbar.setProgress(0);

		playerInfoHolder.player.stop();
		playerInfoHolder.player.reset();

		try {
			playerInfoHolder.player.setDataSource(file);
			playerInfoHolder.player.prepare();
			playerInfoHolder.player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		seekbar.setMax(playerInfoHolder.player.getDuration());

		playButton.setImageResource(R.drawable.ic_action_pause);

		updatePosition2();

		playerInfoHolder.setStarted(true);
	}

	private void stopPlay() {
		playerInfoHolder.player.stop();
		playerInfoHolder.player.reset();

		textSongTitle.setText("No song selected");
		textArtist.setText(" ");
		textAlbum.setText(" ");

		playButton.setImageResource(R.drawable.ic_action_play);
		albumArtView.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_expandplayer_placeholder));

		handler.removeCallbacks(updatePositionRunnable2);
		seekbar.setProgress(0);

		playerInfoHolder.setStarted(false);
	}

	private View.OnClickListener onButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.assoplayer_play: {
				if (playerInfoHolder.player.isPlaying()) {

					handler.removeCallbacks(updatePositionRunnable2);
					playerInfoHolder.player.pause();
					playButton.setImageResource(R.drawable.ic_action_play);
				} else {
					if (playerInfoHolder.isStarted()) {

						playerInfoHolder.player.start();
						playButton.setImageResource(R.drawable.ic_action_pause);
						updatePosition2();

					} else {
						if (playerInfoHolder.currentFile != null) {
							startPlay(playerInfoHolder.currentFile);
						} else {
							Toast.makeText((Activity) v.getContext(),
									"Please select a music!",
									Toast.LENGTH_SHORT).show();
						}
					}
				}

				break;
			}

			case R.id.assoplayer_prev: {

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
									"This is the first song!",
									Toast.LENGTH_SHORT).show();
						} else
							startPlay(playerInfoHolder.currentFile);
						break;
					}
					}
				} else {
					stopPlay();
					Toast.makeText((Activity) v.getContext(),
							"Please select a music!", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}

			case R.id.assoplayer_next: {

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
									"This is the last song!",
									Toast.LENGTH_SHORT).show();
						} else
							startPlay(playerInfoHolder.currentFile);
						break;
					}
					}
				} else {
					stopPlay();
					Toast.makeText((Activity) v.getContext(),
							"Please select a music!", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			}
			case R.id.assoplayer_repeat: {
				switch (playerInfoHolder.repeatMode) {
				case 2: {
					repeatButton
							.setImageResource(R.drawable.ic_action_repeat_1);
					playerInfoHolder.repeatMode = 1;
					break;
				}
				case 1: {
					repeatButton
							.setImageResource(R.drawable.ic_action_repeat_once);
					playerInfoHolder.repeatMode = 0;
					break;
				}
				case 0: {
					repeatButton
							.setImageResource(R.drawable.ic_action_repeat_a);
					playerInfoHolder.repeatMode = 2;
					break;
				}

				}
				break;
			}
			case R.id.assoplayer_shuffle: {
				if (playerInfoHolder.isShuffle) {
					playerInfoHolder.isShuffle = false;
					shuffleButton.setImageResource(R.drawable.ic_action_mute);
					if (playerInfoHolder.currentList != null)
					playerInfoHolder.currentList.unrandomize();
					setInstantPlayList();
				} else {
					playerInfoHolder.isShuffle = true;
					shuffleButton
							.setImageResource(R.drawable.ic_action_shuffle1);
					if (playerInfoHolder.currentList != null)
						playerInfoHolder.currentList.randomize();
					setInstantPlayList();
				}
				break;
			}
			case R.id.assoplayer_showalbumart: {
				isListInflated = true;
				height = albumArtView.getHeight();
				width = albumArtView.getWidth();

				albumArtView.getLayoutParams().height = 0;
				albumArtView.getLayoutParams().width = 0;
				albumArtView.requestLayout();

				inflatedListView.getLayoutParams().height = height;
				inflatedListView.getLayoutParams().width = width;
				inflatedListView.requestLayout();
				break;
			}
			
			case R.id.assoplayer_back: {
				
				AssoMusicPlayerActivity.this.finish();
				break;
			}
			}
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
					break;
				}
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
}
