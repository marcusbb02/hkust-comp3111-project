package com.comp3111.pedometer;

public class PedometerSettings {
	// various values for settings
	public static float 
			pForceBaseThreshold = 1.3f,				// Base value for g-force to be considered as a step
													// range 0.5f - 2.0f
			pThreshold = 0.5f,						// threshold for a step slope to be considered as a step
													// range 0.1f - 1.1f
			pUpperThreshold = 1.0f, 				// upper threshold for adjusting the step slope threshold
													// range 0.5f - 2.0f
			pUpperThresholdRetainProportion = 1.0f,	// the proportion to be retained when large step slope is detected
													// range 0.0f - 1.0f
			pLowerThresholdRetainProportion = 0.9f;	// the proportion to be retained when small step slope is detected
													// range 0.0f - 1.0f
	public static int 
			pStepDurationSample = 10, 				// number of steps to be considered for finding average step duration
													// range 5 - 25
			pStepDurationDiscardThreshold = 20;		// discard the last step data for average if it lasts more than this threshold
													// range 5 - 25
	public static float 
			pDefaultAverageStepDuration = 4.5f;		// default average step duration for the "constant" pace
													// not exist in advance setting
	public static int 
			pStepDelayNumber = 1;					// no. of least iteration(s) before it should be considered as a step
	 												// range 1 - 5
	public static float 
			SA_LOWER_THRESHOLD_RATIO = 1.5f;		// ratio for the pace to be considered as "fast"
													// range 0.5f - 2.0f
	public static float 	
			SA_UPPER_THRESHOLD_RATIO = 2.0f;		// ratio for the pace to be considered as "slow"
													// range 0.5f - 2.0f
	public static int 
			currentProfileID = 0;					// current profile id for the application
													// not exist in advance setting
}
