package com.example.stayzillahackthon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.splash_screen);
	        
	         //Create Thread that will sleep for 5 seconds       
	        Thread background = new Thread() {
	            public void run() {            
	                try {
	                	
	                    sleep(5*1000);
	                    Intent i=new Intent(getBaseContext(),MainActivity.class);
	                    startActivity(i);
	                    finish();
	                     
	                	} catch (Exception e) {
	                 
	                }
	            }
	        };   
	        // start thread
	        background.start();      
	    }     
	    @Override
	    protected void onDestroy() {         
	        super.onDestroy();
	         
	    }
}
