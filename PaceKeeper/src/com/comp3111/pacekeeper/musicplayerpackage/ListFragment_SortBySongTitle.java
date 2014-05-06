/**
 * 
 */
package com.comp3111.pacekeeper.musicplayerpackage;
import com.comp3111.pacekeeper.R;
import java.math.BigDecimal;

import com.comp3111.pacekeeper.musicplayerpackage.MediaCursorAdapter.MediaViewHolder;

import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 */
public class ListFragment_SortBySongTitle extends ListFragment {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */

	private MediaCursorAdapter mediaAdapter = null;
	private MainMusicPlayerActivity mainMusicPlayerActivity;
	private TextView indicater;
	private ImageButton playAllButton;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainMusicPlayerActivity = (MainMusicPlayerActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		View view = (LinearLayout) inflater.inflate(
				R.layout.lf_sortbysongtitle_layout, container, false);

		Cursor cursor = view
				.getContext()
				.getContentResolver()
				.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
						MediaStore.Audio.Media.IS_MUSIC + " != 0", null,
						MediaStore.Audio.Media.TITLE_KEY);

		if (null != cursor) {
			cursor.moveToFirst();

			mediaAdapter = new MediaCursorAdapter(mainMusicPlayerActivity,
					view.getContext(), R.layout.musicplayer_listitem, cursor);

			setListAdapter(mediaAdapter);

		}

		indicater = (TextView) view
				.findViewById(R.id.sortBySongTitle_Indicator);
		playAllButton = (ImageButton) view
				.findViewById(R.id.sortBySongTitle_playAll);
		playAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Singleton_PlayerInfoHolder.getInstance().currentList = new MediaList(
						mainMusicPlayerActivity, false,
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						MediaStore.Audio.Media.IS_MUSIC + " != 0 ", null,
						MediaStore.Audio.Media.TITLE_KEY);

				// Shuffle mode
				if (Singleton_PlayerInfoHolder.getInstance().isShuffle) {
					Singleton_PlayerInfoHolder.getInstance().currentList
							.randomize();
				}

				Singleton_PlayerInfoHolder.getInstance().currentFile = Singleton_PlayerInfoHolder
						.getInstance().currentList.getPath(0);
				mainMusicPlayerActivity.startPlay(Singleton_PlayerInfoHolder
						.getInstance().currentFile);
			}
		});
				
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(
				getListView(),
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
						View view = (View) listView.getChildAt(0);

						MediaViewHolder holder = (MediaViewHolder) view
								.getTag();

						view = (View) listView
								.getChildAt(reverseSortedPositions[0]
										- holder.position);
						if (view == null)
							Toast.makeText(
									mainMusicPlayerActivity,
									reverseSortedPositions[0] + " "
											+ listView.getChildCount(),
									Toast.LENGTH_SHORT).show();

						holder = (MediaViewHolder) view.getTag();

						if (Singleton_PlayerInfoHolder.getInstance().currentList != null) {
							if (Singleton_PlayerInfoHolder.getInstance().currentList
									.addSong(holder.path)) {
								Toast.makeText(
										mainMusicPlayerActivity,
										holder.songTitle.getText()
												+ " is added to current playlist successfully!",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(
										mainMusicPlayerActivity,
										holder.songTitle.getText()
												+ " already exists in the current playlist!",
										Toast.LENGTH_SHORT).show();
							}

						} else {
							String[] whereValue = { holder.path };
							Singleton_PlayerInfoHolder.getInstance().currentList = new MediaList(
									mainMusicPlayerActivity,
									false,
									MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
									MediaStore.Audio.Media.IS_MUSIC
											+ " != 0 and "
											+ MediaStore.Audio.Media.DATA
											+ " =?", whereValue, null);
							Singleton_PlayerInfoHolder.getInstance().currentFile = holder.path;
							mainMusicPlayerActivity.startPlay(holder.path);
							Toast.makeText(
									mainMusicPlayerActivity,
									holder.songTitle.getText()
											+ " is added to current playlist successfully!",
									Toast.LENGTH_SHORT).show();

						}
					}
				}, true, // example : left action = dismiss
				false, // example : right action without dismiss animation
				false, true);
		getListView().setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		getListView().setOnScrollListener(touchListener.makeScrollListener());
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		MediaViewHolder holder = (MediaViewHolder) view.getTag();
		Singleton_PlayerInfoHolder.getInstance().currentFile = (String) holder.path;

		String[] whereVal = { (String) holder.path };
		Singleton_PlayerInfoHolder.getInstance().currentList = new MediaList(
				mainMusicPlayerActivity, false,
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Audio.Media.IS_MUSIC + " != 0 and "
						+ MediaStore.Audio.Media.DATA + " = ?", whereVal,
				MediaStore.Audio.Media.TITLE_KEY);
		mainMusicPlayerActivity.startPlay(Singleton_PlayerInfoHolder
				.getInstance().currentFile);
	}

}
