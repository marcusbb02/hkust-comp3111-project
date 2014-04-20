package com.comp3111.local_database;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.comp3111.pedometer.AggregatedRecords;
import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.StatisticsInfo;
import com.google.gson.Gson;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class JSONHandler {
	// path for storing records
    private static final String PATH_ROOT = Environment.getExternalStorageDirectory().toString() + "/pacekeeper/"; 
    private static final String PATH_RECORDS = Environment.getExternalStorageDirectory().toString() + "/pacekeeper/records/";
    private static final String FILE_ADVANCED = "advanced_setting.dat";
	private static final String FILE_AGG_RECORDS = "all-records.dat";
    
    private static void writeToJSON(String filePath, String jsonStr) throws FileNotFoundException, IOException{
		FileOutputStream fos = null;
		// write json string to dat file
		fos = new FileOutputStream(filePath);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeBytes(jsonStr);
		dos.flush();
		dos.close();
    }
    
    // create record data file from ConsistentContents.currentStatInfo
	public static boolean createCurrentRecord() {
        boolean success = false;
        // create folder if it is not exist
        File directory = new File(PATH_ROOT);
        if (!directory.exists()){
            directory.mkdir();
        }
        directory = new File(PATH_RECORDS);
        if (!directory.exists()){
            directory.mkdir();
        }
        
        String filePath = PATH_RECORDS+"record-"+ConsistentContents.currentStatInfo.getDateString()+".dat";
        String jsonStr = ConsistentContents.currentStatInfo.toJSON();    
        
		try {
			writeToJSON(filePath, jsonStr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","FileNotFoundException");
	        return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","IOException");
	        return success;
		}
		success = true;
        return success;
    }
	
	// read from record with name specified in parameter
	public static boolean readFromRecord(String dateStr) {
        boolean success = false;
        String filePath = PATH_RECORDS+"record-"+dateStr+".dat";
        ifFileExists(filePath);
        
        BufferedReader bf = null;
		try {
			// read json object from dat file
			bf = new BufferedReader(new FileReader(filePath));
			Gson gson = new Gson();
			ConsistentContents.currentStatInfo = gson.fromJson(bf, StatisticsInfo.class);
			bf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","FileNotFoundException");
	        return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","IOException");
	        return success;
		}
		success = true;
        return success;
    }

	public static boolean ifFileExists(String filePath) {
		// fails it if no file exist
        File recordFile = new File(filePath);
        if (!recordFile.exists()){
        	return false;
        }
		return true;
	}

	public static boolean writeAggregatedRecords() {
        boolean success = false;
        // create folder if it is not exist
        File directory = new File(PATH_ROOT);
        if (!directory.exists()){
            directory.mkdir();
        }
        
        String filePath = PATH_ROOT+FILE_AGG_RECORDS;
        String jsonStr = ConsistentContents.aggRecords.toJSON();    
        
		try {
			writeToJSON(filePath, jsonStr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","FileNotFoundException");
	        return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","IOException");
	        return success;
		}
		success = true;
        return success;
	}
	
	public static boolean readAggregatedRecord() {
        boolean success = false;
        String filePath = PATH_ROOT+FILE_AGG_RECORDS;
        // reinitialize the file if not found
        if(!ifFileExists(filePath)){
        	writeAggregatedRecords();
        }
        
        BufferedReader bf = null;
		try {
			// read json object from dat file
			bf = new BufferedReader(new FileReader(filePath));
			Gson gson = new Gson();
			ConsistentContents.aggRecords = gson.fromJson(bf, AggregatedRecords.class);
			bf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","FileNotFoundException");
	        return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","IOException");
	        return success;
		}
		success = true;
        return success;
    }
	
}
