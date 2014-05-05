package com.comp3111.pacekeeper.musicplayerpackage;
import com.comp3111.pacekeeper.R;
import java.math.BigDecimal;

import com.comp3111.pacekeeper.musicplayerpackage.AlbumsCursorAdapter.AlbumsViewHolder;
import com.comp3111.pacekeeper.musicplayerpackage.MediaCursorAdapter.MediaViewHolder;

import android.app.Activity;
import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyArrayAdaptor extends ArrayAdapter<String> {

	private Activity assoActivity;
	private MediaList playList;
	private LayoutInflater inflater;
	private String[] songTitleList;
	private boolean containItem;
	
	class InstantListViewHolder {
		TextView artistName ;
		TextView songTitle ;
		TextView songDuration ;
		ImageView albumArt ;
		String path;
	}
	public MyArrayAdaptor(Context context, int resource, String[] list, boolean containItem) {
		
		super(context, resource ,R.id.displayname,list);
		this.containItem=containItem;
		this.songTitleList=list;
		this.assoActivity = (Activity) context;
		this.playList = Singleton_PlayerInfoHolder
				.getInstance().currentList;
		this.inflater = LayoutInflater.from(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		InstantListViewHolder holder;

		super.getView(position, convertView, parent);
		
		if (convertView == null) {
			// convertView = inflater.inflate(R.layout.listitem, parent, false);
			convertView = inflater.inflate(R.layout.musicplayer_listitem, null);
			
			holder =new InstantListViewHolder();

			holder.artistName = (TextView) convertView.findViewById(R.id.title);
			holder.songTitle = (TextView) convertView.findViewById(R.id.displayname);
			holder.songDuration = (TextView) convertView
					.findViewById(R.id.duration);
			holder.albumArt = (ImageView) convertView
					.findViewById(R.id.smallalbumart);
			convertView.setTag(holder);
		} else {
			holder = (InstantListViewHolder) convertView.getTag();
		}
		
		if(!containItem){
			
			holder.artistName.setText(" ");//(this.playList.getTitle(position));
			holder.songTitle.setText(songTitleList[position]);
			holder.songDuration.setText(" ");
			holder.albumArt.setImageDrawable(assoActivity.getResources().getDrawable(
					R.drawable.ic_expandplayer_placeholder));
			holder.path=null;
			return convertView;
		}
		
		holder.artistName.setText(this.playList.getArtist(position));//(this.playList.getTitle(position));

		holder.songTitle.setText(songTitleList[position]);

		double duration= this.playList.getDuration(position);

		holder.songDuration.setText("" + duration);
		
		String filePath= this.playList.getPath(position);
		
		holder.path=filePath;
		
		Singleton_PlayerInfoHolder.setAlbumArt(holder.albumArt,filePath,true);
		
		return convertView;
		
	}
}
