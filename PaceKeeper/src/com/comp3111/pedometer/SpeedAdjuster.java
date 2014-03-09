package com.comp3111.pedometer;

import android.os.Handler;
import android.util.Log;

import com.smp.soundtouchandroid.SoundTouchPlayable;

public class SpeedAdjuster {
	
	// thread handler
	private static Handler saHandler = new Handler();
	// current state, tempo and pitch
	private static boolean saChanging = false;
	private static int saState, saLastState = 99;
	private static float saTempo, saPitch, saFinalTempo, saFinalPitch, saGradient;
	// threshold for speed change
	private static float saStepDurationLowerThreshold, saStepDurationUpperThreshold;
	// constants for speed levels
	public static final int SA_FAST = 1;
	public static final int SA_NORMAL = 0;
	public static final int SA_SLOW = -1;
	public static final int SA_NOCHANGE = 99;
	public static final float SA_TEMPO_FAST = 1.5f;
	public static final float SA_PITCH_FAST = 1.0f;
	public static final float SA_TEMPO_NORMAL = 1.0f;
	public static final float SA_PITCH_NORMAL = 0.0f;
	public static final float SA_TEMPO_SLOW = 0.5f;
	public static final float SA_PITCH_SLOW = -1.0f;
	
	public static void setStepDurationThreshold(Pedometer pedo_obj, float f){
		saStepDurationLowerThreshold = pedo_obj.getDefaultAverageStepDuration() - 0.5f*f;
		saStepDurationUpperThreshold = pedo_obj.getDefaultAverageStepDuration() + 2*f;
	}
	
	public static int react(Pedometer pedo_obj, final SoundTouchPlayable st_obj){
		Log.i("SpeedAdjustor", "Lower: " + saStepDurationLowerThreshold);
		Log.i("SpeedAdjustor", "Upper: " + saStepDurationUpperThreshold);
		// only change if it is not changing music speed and last state is different
		if(!saChanging){
			Log.i("SpeedAdjuster", "Rate set start");
			// lock the reaction
			saChanging = true;
			if(pedo_obj.getAverageStepDuration() < saStepDurationLowerThreshold){
				saState = SA_FAST;
				if(saState != saLastState)	rampToSpeed(st_obj, SA_FAST);
				return SA_FAST;
			}else if(pedo_obj.getAverageStepDuration() > saStepDurationUpperThreshold){
				saState = SA_SLOW;
				if(saState != saLastState)	rampToSpeed(st_obj, SA_SLOW);
				return SA_SLOW;
			}else{
				saState = SA_NORMAL;
				if(saState != saLastState)	rampToSpeed(st_obj, SA_NORMAL);
				return SA_NORMAL;
			}
		}
		Log.i("SpeedAdjuster", "Rate set end");
		return SA_NOCHANGE;
	}
	
	public static void rampToSpeed(final SoundTouchPlayable st_obj, final int speed_level){
		// interpolate from current
		saTempo = st_obj.getTempo();
		saPitch = st_obj.getPitchSemi();
		// speed level flag cases
		switch(speed_level){
			case	SA_FAST:
				saFinalTempo = SA_TEMPO_FAST;
				saFinalPitch = SA_PITCH_FAST;
				Log.i("SpeedAdjuster", "Rate set to Fast");
				break;
			case	SA_NORMAL:
				saFinalTempo = SA_TEMPO_NORMAL;
				saFinalPitch = SA_PITCH_NORMAL;
				Log.i("SpeedAdjuster", "Rate set to Normal");
				break;
			case	SA_SLOW:
				saFinalTempo = SA_TEMPO_SLOW;
				saFinalPitch = SA_PITCH_SLOW;
				Log.i("SpeedAdjuster", "Rate set to Slow");
				break;			
		}
		
		// see if need to increase or decrease
		if(saFinalTempo - saTempo >= 0){
			saGradient = 0.2f;
		}else{
			saGradient = -0.2f;
		}
		
		// deploy thread for smooth transition
		saHandler.postDelayed(new Runnable(){
			@Override
			public void run() {
				Log.i("SpeedAdjuster", "Iterating to " + saTempo);
				// TODO run itself until constant reached
				if(saTempo - saFinalTempo < 0.001 && saTempo - saFinalTempo > -0.001){
					st_obj.setTempo(saFinalTempo);
					st_obj.setPitchSemi(saFinalPitch);
					Log.i("SpeedAdjuster", "End iterating");
					// release the lock and set it as last state
					saLastState = SA_NOCHANGE;
					saChanging = false;
					saHandler.removeCallbacks(this);
				}else{
					saTempo += saGradient/2;
					st_obj.setTempo(saTempo);
					saPitch += saGradient;
					st_obj.setPitchSemi(saTempo);
					// repeat itself
					saHandler.postDelayed(this, 250);
				}
			}		
		}, 50);
	}

}
