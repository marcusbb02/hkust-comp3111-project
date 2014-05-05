package com.comp3111.pacekeeper.musicplayerpackage;

import java.io.IOException;

import com.smp.soundtouchandroid.SoundTouchPlayable;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class STMediaPlayer {
	private SoundTouchPlayable stPlayable;
	
	public STMediaPlayer(Activity context) throws IOException{
		//AssetFileDescriptor assetFd = context.getAssets().openFd("test.mp3");
		//stPlayable = SoundTouchPlayable.createSoundTouchPlayable(assetFd, 0, 1.0f, 1.0f);
	}

	public void setOnErrorListener(OnErrorListener onError) {
		// TODO Auto-generated method stub
		
	}

	public void setOnCompletionListener(OnCompletionListener onCompletion) {
		// TODO Auto-generated method stub
		
	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if(stPlayable != null)
			return !stPlayable.isPaused();
		else return false;
	}

	public int getDuration() {
		// TODO Auto-generated method stub
		if(stPlayable != null)
			return (int) stPlayable.getDuration();
		else return 0;
	}

	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		if(stPlayable != null)
			return (int) stPlayable.getPlayedDuration();
		else return 0;
	}

	public void stop() {
		// TODO Auto-generated method stub
		if(stPlayable != null)
			stPlayable.stop();
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void setDataSource(String file) throws IOException {
		// TODO Auto-generated method stub
		stop();
		stPlayable = SoundTouchPlayable.createSoundTouchPlayable(file, 0, 2.0f, 1.0f);
	}

	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		if(stPlayable != null){
			Log.i("STMediaPlayer", "Play is invoked!");
			stPlayable.play();
		}
	}

	public void pause() {
		// TODO Auto-generated method stub
		if(stPlayable != null)
			stPlayable.pause();
	}

	public void seekTo(int progress) {
		// TODO Auto-generated method stub
		if(stPlayable != null){
			stPlayable.seekTo((progress / (double) stPlayable.getDuration()), true);
			pause();
			start();
		}
	}

	public void release() {
		// TODO Auto-generated method stub
		
	}
	
	
}
