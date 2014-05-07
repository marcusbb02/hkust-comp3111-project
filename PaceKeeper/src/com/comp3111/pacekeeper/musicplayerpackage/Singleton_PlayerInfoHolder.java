package com.comp3111.pacekeeper.musicplayerpackage;
import com.comp3111.pacekeeper.R;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.provider.MediaStore;
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
	
	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
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

	public static void setAlbumBackground(ImageView albumBg, Bitmap buffer, Activity mActivity) {
		// TODO Auto-generated method stub
		if(buffer != null){
			albumBg.setVisibility(View.VISIBLE);
			albumBg.setImageBitmap(buffer);
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
