package com.comp3111.pacekeeper.musicplayerpackage;
import com.comp3111.pacekeeper.R;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.Toast;

public class Singleton_PlayerInfoHolder {
	private static Singleton_PlayerInfoHolder uniqueInstance;

	static boolean isStarted = false;
	static boolean isMoveingSeekBar = false;
	static boolean isShuffle = false;
	/**@param 0:No repeat
	 * @param 1:Single repeat
	 * @param 2:List repeat
	 */
	static int repeatMode = 2;
	public static STMediaPlayer player = null;
	public static String currentFile = null;
	public static MediaList allSongsList;
	public static MediaList currentList;
	public static AlbumList albumsList;

	public static Singleton_PlayerInfoHolder getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Singleton_PlayerInfoHolder();
		}
		return uniqueInstance;
	}
	
	public static void setAlbumArt(ImageView imageview, String file, boolean compress) {

		String albumArtpath = allSongsList
				.getAlbumArt(file);

		if (albumArtpath != null) {
			File albumArtFile = new File(albumArtpath);
			Bitmap bm = null;
			InputStream iStream1 = null;
			InputStream iStream2 = null;

			try {

				iStream1 = new BufferedInputStream(new FileInputStream(
						albumArtFile));
				if (!compress) {
					bm = BitmapFactory.decodeStream(iStream1);
				} else {
					iStream2 = new BufferedInputStream(new FileInputStream(
							albumArtFile));
					bm = decodeFile2(iStream1, iStream2, 100, 100);
				}
				imageview.setImageBitmap(bm);

			} catch (FileNotFoundException e) {

				MediaMetadataRetriever md = new MediaMetadataRetriever();
				md.setDataSource(file);
				byte[] art = md.getEmbeddedPicture();
				if (art != null) {

					iStream1 = new ByteArrayInputStream(md.getEmbeddedPicture());

					if (!compress) {

						bm = BitmapFactory.decodeStream(iStream1);
					} else {
						iStream2 = new ByteArrayInputStream(
								md.getEmbeddedPicture());
						bm = decodeFile2(iStream1, iStream2, 100, 100);
					}
					imageview.setImageBitmap(bm);
				} else {
					imageview.setImageDrawable(imageview.getContext().getResources().getDrawable(
							R.drawable.ic_expandplayer_placeholder));
				}

			}
		} else {
			imageview.setImageDrawable(imageview.getContext().getResources().getDrawable(
					R.drawable.ic_expandplayer_placeholder));
		}
	}
	
	public static Bitmap decodeFile2(InputStream iStream1, InputStream iStream2,
			int WIDTH, int HIGHT) {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(iStream1, null, o);

		// The new size we want to scale to
		final int REQUIRED_WIDTH = WIDTH;
		final int REQUIRED_HIGHT = HIGHT;
		// Find the correct scale value. It should be the power of 2.
		int scale = 1;

		while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
				&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
			scale *= 2;

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap B = BitmapFactory.decodeStream(iStream2, null, o2);

		return B;

	}
}
