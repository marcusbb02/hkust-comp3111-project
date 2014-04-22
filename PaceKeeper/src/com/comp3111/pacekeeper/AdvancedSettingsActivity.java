package com.comp3111.pacekeeper;

import org.json.JSONObject;

import com.comp3111.local_database.JSONHandler;
import com.comp3111.pedometer.PedometerSettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class AdvancedSettingsActivity extends Activity implements OnClickListener {

	// JSONHandler consists of static methods for ease

	EditText et1,et2,et3,et4,et5,et6,et7,et8,et9,et10,et11;
	SeekBar s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11;
	Button okbtn,ccbtn,rsbtn;

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
		s9 = (SeekBar) findViewById(R.id.aas_s9);
		s10 = (SeekBar) findViewById(R.id.aas_s10);
		s11 = (SeekBar) findViewById(R.id.aas_s11);

		okbtn = (Button) findViewById(R.id.aas_ok); 
		ccbtn = (Button) findViewById(R.id.aas_cc);
		rsbtn = (Button) findViewById(R.id.aas_rs);
		okbtn.setOnClickListener(this);
		ccbtn.setOnClickListener(this);
		rsbtn.setOnClickListener(this);
		retrieve_saved_data();
		
	    s1.setProgress((int)(((Float.valueOf(et1.getText().toString())-0.5f)/1.5f)*100f));
	    s2.setProgress((int)(((Float.valueOf(et2.getText().toString())-0.1f))*100f));
	    s3.setProgress((int)(((Float.valueOf(et3.getText().toString())-0.5f)/1.5f)*100f));
	    s4.setProgress((int)(((Float.valueOf(et4.getText().toString())-0f))*100f));
	    s5.setProgress((int)(((Float.valueOf(et5.getText().toString())-0f))*100f));
	    s6.setProgress((int)(((Float.valueOf(et6.getText().toString())-5f)/20)*100f));
	    s7.setProgress((int)(((Float.valueOf(et7.getText().toString())-5f)/20f)*100f));
	    s9.setProgress((int)(((Float.valueOf(et9.getText().toString())-1f)/4f)*100f));
	    s10.setProgress((int)(((Float.valueOf(et10.getText().toString())-0.5f)/1.5f)*100f));
	    s11.setProgress((int)(((Float.valueOf(et11.getText().toString())-0.5f)/1.5f)*100f));
		
		// JSONHandler
		// Add a reset button for going back to original values
		// So just add a method to do
		// 1. replace the current values in PedometerSettings class (accessible
		// via ConsistentContents)
		// 2. make a toJSON method in PedometerSettings
		// 3. write the json string to file by creating method in JSONHandler
		// (see others as example)
	    
		  s1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {   

			    	et1.setText(String.valueOf(0.5f+1.5f * 0.01f * progress));
			    }       
			});            
		  s2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			             
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			             

			    	et2.setText(String.valueOf(0.1f+1.0f * 0.01f * progress));
			    }       
			});            
		  s3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			             
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et3.setText(String.valueOf(0.5f+1.5f * 0.01f * progress));
			    }       
			});            
		  s4.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			              
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et4.setText(String.valueOf(1.0f * 0.01f * progress));
			    }       
			});            
		  s5.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			              
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			             
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et5.setText(String.valueOf(1.0f * 0.01f * progress));
			    }       
			});            
		  s6.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			            
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et6.setText(String.valueOf(5f+20f * 0.01f * progress));
			    }       
			});            
		  s7.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			         
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et7.setText(String.valueOf(5f+20f * 0.01f * progress));
			    }       
			});            
		  s9.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			              
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			           

			    	et9.setText(String.valueOf(1f+4f * 0.01f * progress));
			    }       
			});            
		  s10.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			             
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et10.setText(String.valueOf(0.5f+1.5f * 0.01f * progress));
			    }       
			});   
		  
		  s11.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
			    @Override       
			    public void onStopTrackingTouch(SeekBar seekBar) {      
			              
			    }       
			    @Override       
			    public void onStartTrackingTouch(SeekBar seekBar) {     
			              
			    }       
			    @Override       
			    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
			              

			    	et11.setText(String.valueOf(0.5f+1.5f * 0.01f * progress));
			    }       
			});            

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aas_ok:
			OK_update_data();
			retrieve_saved_data();
			break;
		case R.id.aas_rs:
			retrieve_saved_data();
			break;
		case R.id.aas_cc:
			finish();
			break;
		}
	}
	
	public void OK_update_data(){
		PedometerSettings.pForceBaseThreshold=Float.valueOf(et1.getText().toString());
		PedometerSettings.pThreshold =Float.valueOf(et2.getText().toString());
		PedometerSettings.pUpperThreshold =Float.valueOf(et3.getText().toString());
		PedometerSettings.pUpperThresholdRetainProportion =Float.valueOf(et4.getText().toString());
		PedometerSettings.pLowerThresholdRetainProportion =Float.valueOf(et5.getText().toString());
		PedometerSettings.pStepDurationSample =Integer.valueOf(et6.getText().toString());
		PedometerSettings.pStepDurationDiscardThreshold =Integer.valueOf(et7.getText().toString());
		PedometerSettings.pStepDelayNumber =Integer.valueOf(et9.getText().toString());
		PedometerSettings.SA_LOWER_THRESHOLD_RATIO  =Float.valueOf(et10.getText().toString());
		PedometerSettings.SA_UPPER_THRESHOLD_RATIO =Float.valueOf(et11.getText().toString());
	}
	
	public void retrieve_saved_data(){
		et1.setText(String.valueOf(PedometerSettings.pForceBaseThreshold));
		et2.setText(String.valueOf(PedometerSettings.pThreshold));
		et3.setText(String.valueOf(PedometerSettings.pUpperThreshold));
		et4.setText(String.valueOf(PedometerSettings.pUpperThresholdRetainProportion));
		et5.setText(String.valueOf(PedometerSettings.pLowerThresholdRetainProportion));
		et6.setText(String.valueOf(PedometerSettings.pStepDurationSample));
		et7.setText(String.valueOf(PedometerSettings.pStepDurationDiscardThreshold));
		et9.setText(String.valueOf(PedometerSettings.pStepDelayNumber));
		et10.setText(String.valueOf(PedometerSettings.SA_LOWER_THRESHOLD_RATIO));
		et11.setText(String.valueOf(PedometerSettings.SA_UPPER_THRESHOLD_RATIO));
	
		
	}
	
	
	
	
	void dialog(String os){
		
		
	}
}