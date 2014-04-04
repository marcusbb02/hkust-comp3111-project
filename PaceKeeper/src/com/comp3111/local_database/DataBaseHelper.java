package com.comp3111.local_database;

import static android.provider.BaseColumns._ID;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = Environment.getExternalStorageDirectory().toString() + "/.PaceKeeper/Database/"; 
    private static String DB_NAME = "personal_profile"; 
    private SQLiteDatabase myDataBase;  
    private final Context myContext;    
	private final static String TAG = "SQLiteHelper";
	
	//constants
    private static String ACH_TABLE = "acheivement"; 
    private static String AID = "acheivement_id"; 
    private static String ACH = "acheivement_name"; 
    private static String ISS = "is_succeed"; 
    private static String REC = "record"; 
    
    private static String STA_TABLE = "statistic"; 
    private static String AT = "app_times"; 
    
    private static String SET_TABLE = "setting"; 
    
	//
	public DataBaseHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
		this.myContext = context;
    	try {     		 
    		openDataBase();     
     	}catch(SQLException sqle){     
     		throw sqle;     
     	}
	}	
	
	public boolean copyDbToSDCard() {
        boolean success = false;
        //this.getReadableDatabase();
        File directory = new File(DB_PATH);
        if (!directory.exists())
            directory.mkdir();
        close();

        try {
            InputStream mInput = myContext.getAssets().open(DB_NAME);
            OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInput.read(buffer)) > 0) {
                mOutput.write(buffer, 0, length);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
            success = true;
        } catch (Exception e) {
            Toast.makeText(myContext,
                    "copyDbToSDCard Error : " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.fillInStackTrace();
        }
        return success;
    }

    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
        Log.i(TAG,"Fetching from " + myPath);
    	// test database existence or version
    	try{
        	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	        Log.d(TAG,"Database EXISTS");
	         // if version is old, DB exits then delete
	        /*
	        if(myDataBase.getVersion() < myContext.getResources().getInteger(R.integer.databaseVersion)){
		        Log.d(TAG,"Database version is old, replacing");
		        myDataBase.close();
		        File file = new File(myPath);
		        boolean deleted = file.delete();
		    	copyDbToSDCard();
	        }else{
	        	myDataBase.close();
	        }
	        */
	    }
	    catch(SQLException se)
	    {
	         // DB not exits code to copy database
	    	copyDbToSDCard();
	    }
    	//Open the database
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }

    public Cursor getSingleRoomInfo(String SQLITE_TABLE, String Room_number) throws SQLException {  	 
    	String parameters[] = { Room_number };
    	// parameterized query
        Cursor mCursor = 
        		myDataBase.rawQuery("SELECT * FROM " + SQLITE_TABLE + " WHERE ROOM_ID=?"
        							, parameters);
        return mCursor; 
    }

    public String[] getBuildingInfo() throws SQLException { 
    	String[] to_return = null;
    	// parameterized query
        Cursor mCursor = 
        		myDataBase.rawQuery("SELECT * FROM building", null);

		// fetch result or toast error
		if(mCursor.getCount() > 0){
			to_return = new String[mCursor.getCount()];
			Integer i = 0;
			while (mCursor.moveToNext()) {
				to_return[i] = mCursor.getString(1);
				i++;
			}
		}
		mCursor.close();
		return to_return;
    }
    
    public String[] getLiftInfo() throws SQLException { 
    	String[] to_return = null;
    	// parameterized query
        Cursor mCursor = 
        		myDataBase.rawQuery("SELECT * FROM lift", null);

		// fetch result or toast error
		if(mCursor.getCount() > 0){
			to_return = new String[mCursor.getCount()];
			Integer i = 0;
			while (mCursor.moveToNext()) {
				to_return[i] = mCursor.getString(1);
				i++;
			}
		}
		mCursor.close();
		return to_return;
    }
    
    /*
    public ArrayList<Room_Info> getRoomInfo(String SQLITE_TABLE, String Room_number) throws SQLException {  	 
        ArrayList<Room_Info> orderDetailList = new ArrayList<Room_Info>();
        Cursor mCursor = 
        		myDataBase.query(true, SQLITE_TABLE, new String[] {
			                    KEY_ROWID,
			                    KEY_COMPANY,
			                    KEY_ORDER,
			                    KEY_SEQ,
			                    KEY_ITEM,
			                    KEY_DESCRIPTION,
			                    KEY_QUANTITY,
			                    KEY_PRICE,}, 
			                    KEY_COMPANY + "=?" + " and "  +
			                    KEY_ORDER + "=?", 
			                    new String[] {company,orderNumber},
			                    null, null, KEY_ITEM , null);
 
 
        if (mCursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail(); 
                orderDetail.setItem(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ITEM)));
                orderDetail.setDescription(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                orderDetail.setQuantity(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_QUANTITY)));
                orderDetail.setPrice(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_PRICE)));
                orderDetailList.add(orderDetail);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        ArrayList<Room_Info> orderDetailList = new ArrayList<Room_Info>();
    	Room_Info orderDetail = new Room_Info(); 
        orderDetailList.add(orderDetail);
        return orderDetailList; 
    }
    */

	@Override
	public void onCreate(SQLiteDatabase db) {
		
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + ACH_TABLE + "("
                + AID + " INTEGER PRIMARY KEY," 
        		+ ACH + " CHAR,"
                + ISS + " INTEGER" 
                + REC + " CHAR" 
        		+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        
		// TODO Auto-generated method stub
//		Log.e(TAG, "SQLitehelper onCreate!");
//		try {
//			db.execSQL("Create TABLE  Data( " +
//					"ID integer Primary Key AUTOINCREMENT, " +
//					"UserName varchar(50) " +
//					")");
//			Log.e(TAG, "createDataTable OK!");
//		} catch (SQLException se) {
//			se.printStackTrace();
//		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.e(TAG, "SQLiteHelper on Open!");
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.e(TAG, "SQLitehelper onUpgrade!");
	}
	
	//FUNCTION
	
	public void add_achievement(String name) {		//acheivement constructor
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(ACH, name); 
	    values.put(ISS, 0); 
	    values.put(REC, "0"); 
	 
	    db.insert(ACH_TABLE, null ,values);
	    db.close(); // Closing database connection
	}
	

	public void update_achievement(int ID,String record) {		//update record
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(ISS, 1); 
	    values.put(REC, record); 

	    db.update(ACH_TABLE, values,  AID + " = " +ID,null );
	    db.close(); // Closing database connection
	}
	
	public String get_achievement_record(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(ACH, new String[] { AID,
					ISS, REC }, AID + "=" + id, null, null, null,
					null);
	        if (cursor != null)
	            cursor.moveToFirst();

			return cursor.getString(3);

		
	}
	
    @Override
	public synchronized void close() {
	    if(myDataBase != null)
		    myDataBase.close(); 
	    super.close(); 
	}

}


/**
http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
*/
