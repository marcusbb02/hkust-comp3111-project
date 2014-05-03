package com.comp3111.pedometer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView.GraphViewData;

/**
 * Class for calculating statistical information
 * @author Henry Chan
 *
 */
public class StatisticsInfo {

	static enum stepSize {
		LARGE, MEDIUM, SMALL
	};
	
	// pace distribution graph data
	public ArrayList<GraphViewData> pace_dist = new ArrayList<GraphViewData>();

	stepSize stepSizeVariable = stepSize.MEDIUM;
	int stepLength = 80; // in cm
	double metValue = 0.2;
	double distanceTravelled;
	double weight = 65;
	double stepDuration;
	int totalSteps;
	double timeLasted = 0.00001;// second
	String recordDateString;
	public String journeyType;		// goals title
	public String journeyTime;		// time elapsed / steps / etc
	public ArrayList<Integer> ach_list=new ArrayList<Integer>();
	
	public StatisticsInfo(double kilogram){
		setWeight(kilogram);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		recordDateString = df.format(new Date());
		Log.i("StatisticsInfo", "New record created with DateStr: " + recordDateString);
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
	
	public int getTotalSteps(){
		return totalSteps;
	}

	double getWeight() {
		return weight;
	}

	public stepSize getStepsize() {
		return stepSizeVariable;
	}

	public double getCaloriesBurn() {
		return (double) metValue * weight * (totalSteps / 360.0);
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
	
	public String getDateString() {
		return recordDateString;
	}
	
	public ArrayList<Integer> get_PA_list() {
		return ach_list;
	}
	
	public String toJSON(){
		String json = new Gson().toJson(this);
		Log.i("JSON record2", json);
		return json;
	}

}