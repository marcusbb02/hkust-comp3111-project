package com.comp3111.pacekeeper;

import com.comp3111.local_database.JSONHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;

public class AdvancedSettingsActivity extends Activity {

	JSONHandler jsonhandler;
	
	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	EditText et5;
	EditText et6;
	EditText et7;
	EditText et8;
	EditText et9;
	EditText et10;
	EditText et11;
	SeekBar s1;
	SeekBar s2;
	SeekBar s3;
	SeekBar s4;
	SeekBar s5;
	SeekBar s6;
	SeekBar s7;
	SeekBar s8;
	SeekBar s9;
	SeekBar s10;
	SeekBar s11;
	Button okbtn;
	Button ccbtn;
	
	// abundant for later use
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advancedsetting);

		et1 = (EditText) findViewById(R.id.aas_et1);
		et2 = (EditText) findViewById(R.id.aas_et2);
		et3 = (EditText) findViewById(R.id.aas_et3);
		et4 = (EditText) findViewById(R.id.aas_et4);
		et5 = (EditText) findViewById(R.id.aas_et5);
		et6 = (EditText) findViewById(R.id.aas_et6);
		et7 = (EditText) findViewById(R.id.aas_et7);
		et8 = (EditText) findViewById(R.id.aas_et8);
		et9 = (EditText) findViewById(R.id.aas_et9);
		et10 = (EditText) findViewById(R.id.aas_et10);
		et11 = (EditText) findViewById(R.id.aas_et11);
	
		s1 = (SeekBar) findViewById(R.id.aas_s1);
		s2 = (SeekBar) findViewById(R.id.aas_s2);
		s3 = (SeekBar) findViewById(R.id.aas_s3);
		s4 = (SeekBar) findViewById(R.id.aas_s4);
		s5 = (SeekBar) findViewById(R.id.aas_s5);
		s6 = (SeekBar) findViewById(R.id.aas_s6);
		s7 = (SeekBar) findViewById(R.id.aas_s7);
		s8 = (SeekBar) findViewById(R.id.aas_s8);
		s9 = (SeekBar) findViewById(R.id.aas_s9);
		s10 = (SeekBar) findViewById(R.id.aas_s10);
		s11 = (SeekBar) findViewById(R.id.aas_s11);


		okbtn = (Button) findViewById(R.id.aas_ok);
		ccbtn = (Button) findViewById(R.id.aas_cc);
		
		setstate();
		
	}
	void setstate(){
		
	}
}