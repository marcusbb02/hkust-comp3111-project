package com.comp3111.pacekeeper.musicplayerpackage;
import com.comp3111.pacekeeper.R;
import com.comp3111.pacekeeper.musicplayerpackage.ArtistsCursorAdapter.ArtistsViewHolder;

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

public class PlayListsCursorAdapter extends SimpleCursorAdapter {
	private Activity mainActivity;
	private Context context;
	private Cursor cursor;
	private LayoutInflater inflater;
	
	protected class PlayListsViewHolder {
		TextView playListName;
		TextView numberofSongs;
		ImageView albumArt;
		Long listID;
	}
	
	public PlayListsCursorAdapter(Activity activity,Context context, int layout, Cursor c) {
		
		super(context, layout, c, new String[] {
				MediaStore.Audio.Playlists.NAME,
				MediaStore.Audio.Playlists._ID
				}, new int[] {
				R.id.displayname});
		
		this.mainActivity = activity;
		this.context = context;
		this.cursor = c;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PlayListsViewHolder holder;
		super.getView(position, convertView, parent);
		if (convertView == null) {
			//convertView = inflater.inflate(R.layout.listitem, parent, false);
			convertView = inflater.inflate(R.layout.musicplayer_listitem, null);
			
			holder = new PlayListsViewHolder();
			
			holder.numberofSongs = (TextView) convertView.findViewById(R.id.title);
			holder.playListName = (TextView) convertView.findViewById(R.id.displayname);
			
			holder.albumArt = (ImageView) convertView
					.findViewById(R.id.smallalbumart);
			convertView.setTag(holder);

		} else {
			holder = (PlayListsViewHolder) convertView.getTag();
		}
		
		holder.albumArt.setImageDrawable(context.getResources().getDrawable(
				R.drawable.ic_expandplayer_placeholder));

		holder.playListName.setText(cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.NAME)));

		holder.listID = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Playlists._ID));
		
		Cursor cursor2 = mainActivity.managedQuery(MediaStore.Audio.Playlists.Members.getContentUri("external",holder.listID ), null,
				MediaStore.Audio.Media.IS_MUSIC +" != 0 ", null,null);
		
		holder.numberofSongs.setText("#Songs: " + cursor2.getCount());

		return convertView;
	}
}
