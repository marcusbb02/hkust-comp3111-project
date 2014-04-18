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

import com.comp3111.pedometer.ConsistentContents;
import com.comp3111.pedometer.StatisticsInfo;
import com.google.gson.Gson;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class RecordHandler {
	// path for storing records
    private static String RECORD_PATH_01 = Environment.getExternalStorageDirectory().toString() + "/pacekeeper/"; 
    private static String RECORD_PATH_02 = Environment.getExternalStorageDirectory().toString() + "/pacekeeper/records/"; 
    
    // create record data file from ConsistentContents.currentStatInfo
	public static boolean createCurrentRecord() {
        boolean success = false;
        // create folder if it is not exist
        File directory = new File(RECORD_PATH_01);
        if (!directory.exists()){
            directory.mkdir();
        }
        directory = new File(RECORD_PATH_02);
        if (!directory.exists()){
            directory.mkdir();
        }
        
		FileOutputStream fos = null;
		try {
			// write json string to dat file
			fos = new FileOutputStream(RECORD_PATH_02+"record.dat");
			//fos = new FileOutputStream(RECORD_PATH_02+"record-"+ConsistentContents.currentStatInfo.getDateString()+".dat");
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeBytes(ConsistentContents.currentStatInfo.toJSON());
			dos.flush();
			dos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","FileNotFoundException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","IOException");
		}
		success = true;
        return success;
    }
	
	// read from record with name specified in parameter
	public static boolean readFromRecord(String dateStr) {
        boolean success = false;
        // fails it if no file exist
        File recordFile = new File(RECORD_PATH_02+"record-"+dateStr+".dat");
        if (!recordFile.exists()){
            recordFile.mkdir();
        }
        
        BufferedReader bf = null;
		try {
			// write json string to dat file
			bf = new BufferedReader(new FileReader(RECORD_PATH_02+"record-"+dateStr+".dat"));
			//bf = new BufferedReader(new FileReader(RECORD_PATH_02+"record.dat"));
			Gson gson = new Gson();
			ConsistentContents.currentStatInfo = gson.fromJson(bf, StatisticsInfo.class);
			//dos.readChars(ConsistentContents.currentStatInfo.toJSON());
			bf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","FileNotFoundException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","IOException");
		}
		success = true;
        return success;
    }
}
