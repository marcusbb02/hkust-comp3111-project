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
			if(!JSONHandler.ifFileExists(listIterator.next())){
				listIterator.remove();
			}
		}
	}
	
	public void recalculateStat(){
		// TODO: fetch each of the JSON record, and recalculate all stat info
	}
	
	public void addCurrentRecord(){
		// create current record to file and recalculate the statistics, also update this aggregated record file
		if(JSONHandler.createCurrentRecord()){
			recalculateStat();
			recordStr.add(ConsistentContents.currentStatInfo.recordDateString);
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
