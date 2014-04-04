package com.comp3111.pedometer;
/**
 * Class for calculating statistical information
 * @author Henry Chan
 *
 */
public class StatisticsInfo {

	static enum stepSize {
		LARGE, MEDIUM, SMALL
	};

	stepSize stepSizeVariable = stepSize.MEDIUM;
	int stepLength = 80; // in cm
	double metValue = 5.0;
	double calories;
	double distanceTravelled;
	double weight = 65;
	double stepDuration;
	double totalSteps;
	double timeLasted;// second
	
	public StatisticsInfo(double kilogram){
		setWeight(kilogram);
	}
	
	public void addTime(double unit){
		timeLasted += unit;
	}

	public void setWeight(double kilogram) {
		weight = kilogram;
	}

	void setStepSize(stepSize sS) {
		stepSizeVariable = sS;
		if(sS == stepSize.SMALL){
			stepLength = 40;
		}else if(sS == stepSize.MEDIUM){
			stepLength = 80;
		}else{
			stepLength = 160;
		}
	}
	
	public void setTotalSteps(int st){
		totalSteps = st;
	}

	double getWeight() {
		return weight;
	}

	public stepSize getStepsize() {
		return stepSizeVariable;
	}

	public double getCaloriesBurn() {
		return metValue * weight * (timeLasted / 360);
	}

	public double getDistanceTravelled() {
		// 1 mile ~= 160 cm
		distanceTravelled = (stepLength * totalSteps) / 160.0;	// in miles 
		return distanceTravelled;
	}

	public double getStepPerTime() {
		double stepPerTime = totalSteps / (timeLasted / 60);	// per min
		return stepPerTime;
	}

	public double getDistancePerTime() {
		double distancePerTime = distanceTravelled / (timeLasted / 3600);	// per hour
		return distancePerTime;
	}

	public double getTimeLasted() {
		return timeLasted;
	}
}