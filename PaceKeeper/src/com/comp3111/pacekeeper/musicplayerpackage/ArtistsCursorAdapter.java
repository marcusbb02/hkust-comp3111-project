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

public class ArtistsCursorAdapter extends SimpleCursorAdapter {
	private Activity mainActivity;
	private Context context;
	private Cursor cursor;
	private LayoutInflater inflater;
	
	protected class ArtistsViewHolder {
		TextView artistName;
		TextView numberofSongs;
		TextView numberofAlbums;
		ImageView albumArt;

	}
	
	public ArtistsCursorAdapter(Activity activity,Context context, int layout, Cursor c) {
		super(context, layout, c, new String[] {
				MediaStore.Audio.Artists.ARTIST,
				MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
				MediaStore.Audio.Artists.NUMBER_OF_TRACKS }, new int[] {
				R.id.displayname, R.id.title, R.id.duration });
		
		this.mainActivity = activity;
		this.context = context;
		this.cursor = c;
		this.inflater = LayoutInflater.from(context);
	}

//	@Override
//	public void bindView(View view, Context context, Cursor cursor) {
//		TextView artistName = (TextView) view.findViewById(R.id.displayname);
//		TextView numberofAlbums = (TextView) view.findViewById(R.id.title);
//		TextView numberofSongs = (TextView) view.findViewById(R.id.duration);
//		ImageView albumArt = (ImageView) view.findViewById(R.id.smallalbumart);
//
//		albumArt.setImageDrawable(context.getResources().getDrawable(
//				R.drawable.ic_expandplayer_placeholder));
//
//		artistName.setText(cursor.getString(cursor
//				.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
//
//		int numberOfAlbums = (cursor.getInt(cursor
//				.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
//
//		numberofAlbums.setText("#Albums: " + numberOfAlbums);
//
//		int numberOfSongs = (cursor.getInt(cursor
//				.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
//
//		numberofSongs.setText("#Songs: " + numberOfSongs);
//
//	}
//
//	@Override
//	public View newView(Context context, Cursor cursor, ViewGroup parent) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.listitem, parent, false);
//
//		bindView(v, context, cursor);
//
//		return v;
//	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ArtistsViewHolder holder;
		super.getView(position, convertView, parent);
		if (convertView == null) {
			//convertView = inflater.inflate(R.layout.listitem, parent, false);
			convertView = inflater.inflate(R.layout.musicplayer_listitem, null);
			holder = new ArtistsViewHolder();
			holder.numberofSongs = (TextView) convertView.findViewById(R.id.title);
			holder.artistName = (TextView) convertView.findViewById(R.id.displayname);
			holder.numberofAlbums = (TextView) convertView
					.findViewById(R.id.duration);
			holder.albumArt = (ImageView) convertView
					.findViewById(R.id.smallalbumart);
			convertView.setTag(holder);

		} else {
			holder = (ArtistsViewHolder) convertView.getTag();
		}
		
		holder.albumArt.setImageDrawable(context.getResources().getDrawable(
				R.drawable.ic_expandplayer_placeholder));

		holder.artistName.setText(cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));

		int numberOfAlbums = (cursor.getInt(cursor
				.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));

		holder.numberofAlbums.setText("#Albums: " + numberOfAlbums);

		int numberOfSongs = (cursor.getInt(cursor
				.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));

		holder.numberofSongs.setText("#Songs: " + numberOfSongs);
		return convertView;
	}
}