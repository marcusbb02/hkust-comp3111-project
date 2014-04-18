package com.comp3111.local_database;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.comp3111.pedometer.ConsistentContents;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class RecordHandler {
    private static String RECORD_PATH_01 = Environment.getExternalStorageDirectory().toString() + "/pacekeeper/"; 
    private static String RECORD_PATH_02 = Environment.getExternalStorageDirectory().toString() + "/pacekeeper/records/"; 
    
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
			fos = new FileOutputStream(RECORD_PATH_02+"record.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","fail");
		}
		DataOutputStream dos = new DataOutputStream(fos);
		
		try {
			dos.writeChars(ConsistentContents.currentStatInfo.toJSON());
			dos.flush();
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("JSONTest","fail");
		}
		success = true;
        return success;
    }
}
