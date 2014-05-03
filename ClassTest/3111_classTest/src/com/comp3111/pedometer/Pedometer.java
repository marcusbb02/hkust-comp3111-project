package com.comp3111.pedometer;

import java.util.List;

public class Pedometer extends PedometerSettings{
	// for log message title
	private final static String module_name = "Pedometer";
	// sensor and its state
	//private SensorManager sensorManager;
	public boolean running = false;
	//thread handler
	//Handler pHandler = new Handler();
	//various values for estimation of steps
	public float pForce;
	float pLastForceValue = 0.0f;
	float pForceDiff = 0.0f;
	// thread interval, step counter, step-delay counter (all in terms of iteration)
	private int pInterval;
	int pStep = 0, pStepDelay = 0;
	// step duration related
	private int[] pStepDuration;
	int pStepDurationCounter = 0, pCurrentStepDuration = 0, pLastStepDurationArrayPos = 0;
	float pAverageStepDuration = pDefaultAverageStepDuration;
	
	public void resetAverageStepDuration(){
		pAverageStepDuration = pDefaultAverageStepDuration;
	}
	
	public Pedometer(int polling_interval){		//(Context context, int polling_interval){
		// search for acclerometer
		/*sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() == 0)
        {
        	Log.e(module_name, "Fail to acquire acclerometer from this device!");
        	assert(false);
        }*/
		// assign polling interval and other variables
		pInterval = polling_interval;
		pStep = 0;
		pStepDuration = new int[pStepDurationSample];
		// pre-fill pStepDuration to prevent sudden pace change on start
		for(int i = 0; i < pStepDurationSample; i++){
			pStepDuration[i] = (int)pDefaultAverageStepDuration;
		}
	}
	
	public void startSensor(){
		/*sensorManager.registerListener(this, 
				   					   sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				   					   SensorManager.SENSOR_DELAY_GAME);*/
		/*	More sensor speeds (taken from api docs)
	    SENSOR_DELAY_FASTEST get sensor data as fast as possible
	    SENSOR_DELAY_GAME	rate suitable for games
	 	SENSOR_DELAY_NORMAL	rate (default) suitable for screen orientation changes
		 */
		SpeedAdjuster.setStepDurationThreshold(this, 0.5f);
		//pHandler.postDelayed(PedoThread, pInterval);
		//pStep = 0;
		running = true;
	}

	public void stopSensor(){
		//sensorManager.unregisterListener(this);
		//pHandler.removeCallbacks(PedoThread);
		running = false;
	}
	/*
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	 */
	//@Override
	public void onSensorChanged(float x, float y, float z) {
		// check sensor type
		//if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			// assign directions and calculate g-force
			/*
			float x=event.values[0];
			float y=event.values[1];
			float z=event.values[2];*/
			pForce = (x*x + y*y + z*z) / (9.8f * 9.8f);
			// returns g-force to callback method
			onSensorChangedCallback(pForce);
		//}
	}
	
	public void onSensorChangedCallback(float g){	
	}
	
	// separated thread for polling action 
	//final Runnable PedoThread = new Runnable() {
			public void pStepDurationCount(){
				// first it must be "active" steps
				// if "active", record the time (by replacing oldest time) and calculate its average
				if(pCurrentStepDuration < pStepDurationDiscardThreshold){
						// just minus the farest value and add the most recent one
						pAverageStepDuration *= pStepDurationSample;
						pAverageStepDuration += pCurrentStepDuration - pStepDuration[pLastStepDurationArrayPos];
						//Log.i("msg", "Current: " + pCurrentStepDuration + "; laststep: " + pStepDuration[pLastStepDurationArrayPos]);
						pAverageStepDuration /= pStepDurationSample;
					//}
					// store last duration value and do array pointer rotation
					pStepDuration[pLastStepDurationArrayPos] = pCurrentStepDuration;
					pLastStepDurationArrayPos++;
					if(pLastStepDurationArrayPos >= pStepDurationSample){
						pLastStepDurationArrayPos = 0;
					}
				}
				// it this state, just invalidate and recount from next step
				pCurrentStepDuration = 0;
			}
		
		   //@Override
		   public void run() {
			    // step estimation algorithm by comparing diff to situations
				pForceDiff = pForce - pLastForceValue;
				// Firstly, g-force must exceed noise level and it is a positive slope/diff
				if(pForce > pForceBaseThreshold && pForceDiff > 0){
					// steps only count if exceed "moving threshold" determined by force from walking
					if(pForceDiff > pThreshold){
						// ... and it is human-possible
						if(pStepDelay <= 0){
							// ... it could also be a pace change to fast (and exert more force), so try to re-adjust the threshold by stepping up :)
							if(pForceDiff > pUpperThreshold){
								pThreshold = pUpperThresholdRetainProportion * pThreshold + (1-pUpperThresholdRetainProportion) * pForceDiff;
							}
							pStep++;
							pStepDelay = pStepDelayNumber;	
							// calculate new average for step duration value			
							pStepDurationCount();
						}else{
							// ... but it could then be the user being too violent, so let him calm before recounting a step
							pStepDelay = pStepDelayNumber;
						}
					}else{
						// ... it could also be a pace change to slow (and exert less force), so try to re-adjust the threshold by watering down :)
						pThreshold = pLowerThresholdRetainProportion * pThreshold + (1-pLowerThresholdRetainProportion) * pForceDiff;
						pStepDelay--;
					}
				}else{
					// still reduce the delay, otherwise delay counter only decrease as "spike" occurs
					pStepDelay--;
				}
				// update last values for next iteration
				pLastForceValue = pForce;
				pCurrentStepDuration++;
				// callback function and continuation
				PedoThreadCallback(pStep, pThreshold, pAverageStepDuration);
				//pHandler.postDelayed(this, pInterval);
		   }
	//};
	
	public void PedoThreadCallback(int st, float threshold, float s_duration){
	}
	
	public float getAverageStepDuration(){
		return pAverageStepDuration;
	}
	
	public float getDefaultAverageStepDuration(){
		resetAverageStepDuration();	// to ensure the value is updated
		return pDefaultAverageStepDuration;
	}

}