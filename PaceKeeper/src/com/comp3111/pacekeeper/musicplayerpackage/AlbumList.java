package com.comp3111.pacekeeper.musicplayerpackage;

import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class AlbumList {
	private Activity mainActivity;
	private Hashtable<String, String> albumsList;
	public AlbumList(Activity activity) {
		this.mainActivity=activity;
		
		this.albumsList=new Hashtable<String,String>();
		new Hashtable<String, String>();
		
		String[] projection = {
		MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART };
		
		Cursor cursor = activity.managedQuery(
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection,
				null, null, MediaStore.Audio.Albums.ALBUM_KEY);
		
		while (cursor.moveToNext()) {
			String album=cursor.getString(0);
			String art= cursor.getString(1);
			
			if (!this.albumsList.containsKey(album)){
				if(album!=null&&art!=null){		
				
				this.albumsList.put(album,art);
				}
			}

				
		}
		
		
	}
	public String getAlbumArt(String key){
		
		
		return this.albumsList.get(key);
	}

	
}
