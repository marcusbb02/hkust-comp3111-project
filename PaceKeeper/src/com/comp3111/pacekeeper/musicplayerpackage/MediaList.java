package com.comp3111.pacekeeper.musicplayerpackage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class MediaList {

	private class Song {
		String _ID = "";
		String title = "";
		String artist = "";
		String duration = "";
		String path = "";
		String album = "";
		String albumArt = "";

		public Song(String _ID, String title, String artist, String duration,
				String path, String album, String albumArt) {
			this._ID = _ID;
			this.title = title;
			this.artist = artist;
			this.duration = duration;
			this.path = path;
			this.album = album;
			this.albumArt = albumArt;
		}
	}

	private class AssociatedKey implements Comparable<AssociatedKey> {
		public AssociatedKey(String path, int key) {
			this.filePath = path;
			this.key = key;
		}

		public AssociatedKey(AssociatedKey another) {
			this.filePath = another.filePath;
			this.key = another.key;
		}

		String filePath;
		int key;

		@Override
		public int compareTo(AssociatedKey another) {
			return this.filePath.compareTo(another.filePath);
		}

	}

	private ArrayList<AssociatedKey> songsList;
	private ArrayList<AssociatedKey> backUpList;
	private Song song;
	private HashMap<Integer, Song> cachedSongs;
	private Activity mainActivity;
	private boolean sortable;

	public MediaList(Activity activity, boolean sortable, Uri from,
			String where, String[] whereVal, String orderBy) {
		this.mainActivity = activity;
		this.sortable = sortable;

		String[] projection = { MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM };

		Cursor cursor = activity.getContentResolver().query(from, projection,
				where, whereVal, orderBy);

		this.songsList = new ArrayList<AssociatedKey>();
		this.backUpList = new ArrayList<AssociatedKey>();
		this.cachedSongs = new HashMap<Integer, Song>();
		// this.cachedSongs = new HashMap<String, Song>();
		int count = 0;
		while (cursor.moveToNext()) {
			String path = cursor.getString(0);
			String currentAlbum = cursor.getString(5);
			String currentAlbumArt = "";

			this.songsList.add(new AssociatedKey(path, songsList.size()));
			this.backUpList.add(new AssociatedKey(path, backUpList.size()));

			if (currentAlbum != null)
				currentAlbumArt = Singleton_PlayerInfoHolder.getInstance().albumsList
						.getAlbumArt(currentAlbum);

			song = new Song(cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4), path,
					currentAlbum, currentAlbumArt);

			// int key = this.songsList.indexOf(songsList.size()-1);
			int key = songsList.size() - 1;
			if (!cachedSongs.containsKey(key))

				cachedSongs.put(key, song);
		}

		// // Sorting
		if (sortable) {
			Collections.sort(songsList, new Comparator<AssociatedKey>() {

				@Override
				public int compare(AssociatedKey arg0, AssociatedKey arg1) {
					return arg0.filePath.compareTo(arg1.filePath);
				}
			});
		}

	}

	public void randomize() {
		long seed = System.nanoTime();
		Collections.shuffle(songsList, new Random(seed));
	}

	public void unrandomize() {
		songsList = new ArrayList<AssociatedKey>();
		for (int i = 0; i < backUpList.size(); i++) {
			songsList.add(new AssociatedKey(backUpList.get(i)));
		}
	}
	public int getSize() {
		return songsList.size();
	}
	
	public int findKeybyPath(String path) {
		// if sortable, then use binery search
		int pos; 
		if (sortable) {
			pos = Collections.binarySearch(songsList, new AssociatedKey(
					path, 1));
			if (pos < 0)
				return -1;
		}
		else{
			pos = positionOf(path);
		}
		return songsList.get(pos).key;

	}

	public int positionOf(String path) {
		for (int i = 0; i < songsList.size(); i++) {
			if (songsList.get(i).filePath.equals(path)) {
				return i;
			}
		}
		return -1;
	}

	public boolean addSong(String filePath) {
		String _ID = Singleton_PlayerInfoHolder.getInstance().allSongsList
				.getID(filePath);
		String title = Singleton_PlayerInfoHolder.getInstance().allSongsList
				.getTitle(filePath);
		String artist = Singleton_PlayerInfoHolder.getInstance().allSongsList
				.getArtist(filePath);
		String duration = Singleton_PlayerInfoHolder.getInstance().allSongsList
				.getDuration2String(filePath);
		String album = Singleton_PlayerInfoHolder.getInstance().allSongsList
				.getAlbum(filePath);
		String albumArt = Singleton_PlayerInfoHolder.getInstance().allSongsList
				.getAlbumArt(filePath);

		if (positionOf(filePath)==-1) {
			songsList.add(new AssociatedKey(filePath, songsList.size()));
			cachedSongs.put(songsList.size() - 1, new Song(_ID, title, artist,
					duration, filePath, album, albumArt));
			backUpList.add(new AssociatedKey(filePath, backUpList.size()));
			cachedSongs.put(backUpList.size() - 1, new Song(_ID, title, artist,
					duration, filePath, album, albumArt));
			return true;
		}
		return false;
	}
	
	public boolean deleteSong(String filePath) {
		int pos=positionOf(filePath);
		if (pos>-1) {
			songsList.remove(pos);
			
			for (int i = 0; i < backUpList.size(); i++) {
				if (backUpList.get(i).filePath.equals(filePath)) {
					pos=i;
				}
			}
			backUpList.remove(pos);
			return true;
		}
		return false;
	}
	
	public boolean deleteSong(int pos) {
		
		if (pos>-1) {
			AssociatedKey assoKey=songsList.get(pos);
			songsList.remove(pos);
			
			for (int i = 0; i < backUpList.size(); i++) {
				if (backUpList.get(i).equals(assoKey)) {
					pos=i;
				}
			}
			backUpList.remove(pos);
			return true;
		}
		return false;
	}

	public String matchWithAlbum(String album) {
		String gottenAlbum = null;
		for (int i = 0; i < songsList.size(); i++) {
			gottenAlbum = cachedSongs.get(songsList.get(i).key).album;
			if (gottenAlbum.equals(album)) {
				return songsList.get(i).filePath;
			}
		}
		return null;
	}

	private int adjustPos(int position) {
		if (position < 0)
			position = songsList.size() - 1;
		else
			position %= songsList.size();
		return position;
	}

	public String[] getTitleArray() {
		String[] titleArray = new String[songsList.size()];
		for (int i = 0; i < songsList.size(); i++) {
			titleArray[i] = getTitle(i);
		}
		return titleArray;
	}

	public String getID(int position) {
		return cachedSongs.get(songsList.get(position).key)._ID;
	}

	public String getTitle(int position) {
		return cachedSongs.get(songsList.get(position).key).title;
	}

	public String getArtist(int position) {
		return cachedSongs.get(songsList.get(position).key).artist;
	}

	public double getDuration(int position) {
		String dur = cachedSongs.get(songsList.get(position).key).duration;
		Long durationInMs = (long) 0.0;
		if (dur != null)
			durationInMs = Long.parseLong(dur);

		double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

		durationInMin = new BigDecimal(Double.toString(durationInMin))
				.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		return durationInMin;
	}

	public String getPath(int position) {
		return this.songsList.get(position).filePath;
	}

	//
	// public String getAlbum(int position) {
	// return cachedSongs.get(position).album;
	// }
	//
	// public String getAlbumArt(int position) {
	// return cachedSongs.get(position).albumArt;
	// }

	public boolean existKey(String path) {
		if (findKeybyPath(path) != -1)
			return true;
		else
			return false;
	}

	public String getID(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos)._ID;
		else
			return null;
	}

	public String getTitle(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).title;
		else
			return null;
	}

	public String getArtist(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).artist;
		else
			return null;
	}

	public double getDuration(String path) {
		int pos = findKeybyPath(path);
		if (pos == -1)
			return 0;
		else {
			String dur = cachedSongs.get(pos).title;
			Long durationInMs = (long) 0.0;
			if (dur != null)
				durationInMs = Long.parseLong(dur);

			double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

			durationInMin = new BigDecimal(Double.toString(durationInMin))
					.setScale(2, BigDecimal.ROUND_UP).doubleValue();
			return durationInMin;
		}
	}
	
	public String getDuration2String(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).duration;
		else
			return null;
	}

	public String getPath(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).path;
		else
			return null;
	}

	public String getAlbum(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).album;
		else
			return null;
	}

	public String getAlbumArt(String path) {
		int pos = findKeybyPath(path);
		if (pos != -1)
			return cachedSongs.get(pos).albumArt;
		else
			return null;
	}

	public String nextFileLoop(String currentFile) {
		int pos = adjustPos(positionOf(currentFile) + 1);
		return songsList.get(pos).filePath;
	}

	public String nextFile(String currentFile) {
		if (positionOf(currentFile) >= songsList.size() - 1
				|| positionOf(currentFile) < 0) {
			return null;
		}
		int pos = positionOf(currentFile) + 1;
		return songsList.get(pos).filePath;
	}

	public String prevFileLoop(String currentFile) {
		int pos = adjustPos(positionOf(currentFile) - 1);
		return songsList.get(pos).filePath;
	}

	public String prevFile(String currentFile) {
		if (positionOf(currentFile) > songsList.size() - 1
				|| positionOf(currentFile) <= 0) {
			return null;
		}
		int pos = positionOf(currentFile) - 1;
		return songsList.get(pos).filePath;
	}

}