package com.comp3111.pacekeeper.musicplayerpackage;
import com.comp3111.pacekeeper.R;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Singleton_PlayerInfoHolder {
	private static Singleton_PlayerInfoHolder uniqueInstance;
	
	private static boolean isStarted = false;
	static boolean isMoveingSeekBar = false;
	static boolean isShuffle = false;
	/**@param 0:No repeat
	 * @param 1:Single repeat
	 * @param 2:List repeat
	 */
	public static int repeatMode = 2;
	public static STMediaPlayer player = null;
	public static String currentFile = null;
	public static MediaList allSongsList;
	public static MediaList currentList;
	public static AlbumList albumsList;
	// album blur
	public static Bitmap blurredArt;
	public static boolean usingPlayer = false;
	
	public static void loadLists(Activity mActivity){
		if(Singleton_PlayerInfoHolder.getInstance().albumsList == null)
			Singleton_PlayerInfoHolder.getInstance().albumsList = new AlbumList(
					mActivity);
			if(Singleton_PlayerInfoHolder.getInstance().allSongsList == null)
			Singleton_PlayerInfoHolder.getInstance().allSongsList = new MediaList(
					mActivity, true, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					MediaStore.Audio.Media.IS_MUSIC + " != 0", null,
					MediaStore.Audio.Media.TITLE_KEY);
			if(Singleton_PlayerInfoHolder.getInstance().currentList == null)
			Singleton_PlayerInfoHolder.getInstance().currentList = new MediaList(
					mActivity, false, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					MediaStore.Audio.Media.IS_MUSIC + " != 0", null,
					MediaStore.Audio.Media.TITLE_KEY);
			Log.i("Singleton_PlayerInfoHolder", "Lists loaded");
	}

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
	
	// two extra methods for async-loading of images
	public static Bitmap decodeAlbumArt(String file, boolean compress) {
		
		Bitmap toReturn = null;

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
				}

			}
			toReturn = bm;
		}
		return toReturn;
	}

	@SuppressLint("NewApi")
	public static void setAlbumBackground(ImageView albumBg, Bitmap buffer, Activity mActivity) {
		Bitmap buffer_output = null;
		if(buffer != null){
			// TODO Auto-generated method stub
			int img_width = buffer.getHeight();
			int img_height = buffer.getWidth();
			float ratio = (float) img_width / img_height;
			if(ratio < 1)	ratio= 1/ratio;
			if(ratio > 2)	albumBg.setVisibility(View.INVISIBLE);
			buffer_output = Bitmap.createBitmap(buffer);
			final RenderScript rs = RenderScript.create( mActivity );
			final Allocation input = Allocation.createFromBitmap( rs, buffer, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT );
			final Allocation output = Allocation.createTyped( rs, input.getType() );
			final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
			script.setRadius( 20.0f );
			script.setInput( input );
			script.forEach( output );
			output.copyTo( buffer_output );
		}
		if(buffer_output != null){
			albumBg.setVisibility(View.VISIBLE);
			albumBg.setImageBitmap(buffer_output);
		} else {
			albumBg.setVisibility(View.INVISIBLE);
		}
	}
	
	public static void applyAlbumArt(ImageView albumArt, Bitmap buffer) {
		// TODO Auto-generated method stub
		if(buffer != null){
			albumArt.setImageBitmap(buffer);
		} else {
			albumArt.setImageDrawable(albumArt.getContext().getResources().getDrawable(
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

	public static boolean isStarted() {
		return isStarted;
	}

	public static void setStarted(boolean isStarted) {
		Singleton_PlayerInfoHolder.isStarted = isStarted;
	}

}
