package com.comp3111.pacekeeper.musicplayerpackage;

import com.comp3111.pacekeeper.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumsCursorAdapter extends SimpleCursorAdapter {

	private Activity mainActivity;
	private Context context;
	private Cursor cursor;
	private LayoutInflater inflater;

	protected class AlbumsViewHolder {
		TextView artistName;
		TextView albumTitle;
		TextView numberofAlbums;
		ImageView albumArt;

	}

	public AlbumsCursorAdapter(Activity activity, Context context, int layout,
			Cursor c) {
		super(context, layout, c, new String[] { MediaStore.Audio.Albums.ALBUM,
				MediaStore.Audio.Albums.ARTIST,
				MediaStore.Audio.Albums.NUMBER_OF_SONGS }, new int[] {
				R.id.displayname, R.id.title, R.id.duration },2);

		this.mainActivity = activity;
		this.context = context;
		this.cursor = c;
		this.inflater = LayoutInflater.from(context);
	}

	// @Override
	// public void bindView(View view, Context context, Cursor cursor) {
	// //Toast.makeText(mainActivity , "I am in bindView", LENGTH_SHORT).show();
	//
	// ViewHolder holder=(ViewHolder) view.getTag();
	//
	// holder.albumArt.setImageDrawable(context.getResources().getDrawable(
	// R.drawable.ic_expandplayer_placeholder));
	//
	// String currentAlbum = cursor.getString(cursor
	// .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
	//
	// holder.name.setText(currentAlbum);
	//
	// holder.title.setText(cursor.getString(cursor
	// .getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
	//
	// int numberOfSongs = (cursor.getInt(cursor
	// .getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)));
	//
	// holder.numberofAlbums.setText("" + numberOfSongs);
	//
	// int listPosition = MusicPlayer_with_SongsLists.songsList
	// .matchWithAlbum(currentAlbum);
	//
	// if (listPosition >= 0) {
	// String file = MusicPlayer_with_SongsLists.songsList
	// .getPath(listPosition);
	//
	// ((MusicPlayer_with_SongsLists) mainActivity).setAlbumArt(holder.albumArt,
	// file);
	// }
	//
	// // view.setTag(cursor.getString(cursor
	// // .getColumnIndex(MediaStore.Audio.Media.DATA)));
	// }

	// @Override
	// public View newView(Context context, Cursor cursor, ViewGroup parent) {
	// LayoutInflater inflater = LayoutInflater.from(context);
	// View v = inflater.inflate(R.layout.listitem, parent, false);
	//
	// bindView(v, context, cursor);
	//
	// return v;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AlbumsViewHolder holder;
		super.getView(position, convertView, parent);
		if (convertView == null) {
			//convertView = inflater.inflate(R.layout.listitem, parent, false);
			convertView = inflater.inflate(R.layout.musicplayer_listitem, null);
			holder = new AlbumsViewHolder();
			holder.artistName = (TextView) convertView.findViewById(R.id.title);
			holder.albumTitle = (TextView) convertView.findViewById(R.id.displayname);
			holder.numberofAlbums = (TextView) convertView
					.findViewById(R.id.duration);
			holder.albumArt = (ImageView) convertView
					.findViewById(R.id.smallalbumart);
			convertView.setTag(holder);

		} else {
			holder = (AlbumsViewHolder) convertView.getTag();
		}
		String currentAlbum = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Albums.ALBUM));

		holder.albumTitle.setText(currentAlbum);

		holder.artistName.setText(cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));

		int numberOfSongs = (cursor.getInt(cursor
				.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)));

		holder.numberofAlbums.setText("#Songs: " + numberOfSongs);

		String file = Singleton_PlayerInfoHolder.getInstance().allSongsList
		.matchWithAlbum(currentAlbum);
		
		
			Singleton_PlayerInfoHolder.setAlbumArt(
					holder.albumArt, file ,true);
				
		return convertView;
	}

}