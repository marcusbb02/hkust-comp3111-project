package com.comp3111.pedometer;

import java.util.ArrayList;
import java.util.ListIterator;

import android.util.Log;

import com.comp3111.local_database.JSONHandler;
import com.google.gson.Gson;

public class AggregatedRecords {
	public int totalSteps = 0;
	public double totalMiles = 0;
	public double avgSPM = 0;
	public double avgMPH = 0;
	public double calories = 0;
	public ArrayList<String> recordStr = new ArrayList<String>();
	
	public void rescanRecords(){
		ListIterator<String> listIterator = recordStr.listIterator();
		while(listIterator.hasNext()){
			if(!JSONHandler.ifRecordExists(listIterator.next())){
				listIterator.remove();
			}
		}
	}
	
	public void recalculateStat(){
		//preserve current record before recalculating the stat
		StatisticsInfo tmpInfo = ConsistentContents.currentStatInfo;
		//rescanRecords();
		Log.i("recalculateStatInit", ""+recordStr.size());
		totalSteps=0;
		totalMiles = 0;
		avgSPM = 0;
		avgMPH = 0;
		calories = 0;
		// TODO: fetch each of the JSON record, and recalculate all stat info
		ListIterator<String> listIterator = recordStr.listIterator();
		while(listIterator.hasNext()){
			String nextDate = listIterator.next();
			if(JSONHandler.ifRecordExists(nextDate)){
				JSONHandler.readFromRecord(nextDate);
				//recordStr.size()
				//ConsistentContents.currentStatInfo.totalSteps
				Log.i("recalculateStat", ""+ConsistentContents.currentStatInfo.getCaloriesBurn());
				totalSteps+=ConsistentContents.currentStatInfo.totalSteps;
				//ConsistentContents.currentStatInfo.distanceTravelled
				totalMiles+=ConsistentContents.currentStatInfo.distanceTravelled;
				//ConsistentContents.currentStatInfo.getStepPerTime()
				avgSPM=avgSPM+ConsistentContents.currentStatInfo.getDistancePerTime();
				//ConsistentContents.currentStatInfo.getDistancePerTime()
				avgMPH=avgMPH+ConsistentContents.currentStatInfo.getDistancePerTime();
				//ConsistentContents.currentStatInfo.calories
				calories=calories+ConsistentContents.currentStatInfo.getCaloriesBurn();
			}
		}			
		if(recordStr.size() != 0){
			avgSPM=avgSPM/recordStr.size();
			avgMPH=avgMPH/recordStr.size();
		}
		// resume preserved currentStatInfo
		ConsistentContents.currentStatInfo = tmpInfo;

	}
	
	public void addCurrentRecord(){
		// create current record to file and recalculate the statistics, also update this aggregated record file
		if(JSONHandler.createCurrentRecord()){
			// order is VERY IMPORTANT in here since recalculateStat does not reserve current record
			recordStr.add(ConsistentContents.currentStatInfo.recordDateString);
			recalculateStat();
			JSONHandler.writeAggregatedRecords();
		}
		
		// log all records for testing
		ListIterator<String> listIterator = recordStr.listIterator();
		while(listIterator.hasNext()){
			Log.i("Current Record List",listIterator.next());
		}
	}

	public String toJSON() {
		String json = new Gson().toJson(this);
		Log.i("JSON record", json);
		return json;
	}
	
}
