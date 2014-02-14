package com.example.servicetest3;

import java.util.Calendar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    
    protected LocationManager locationManager;    
    protected Button retrieveLocationButton;
    protected TextView displayView;
    
  //accelerometer vars
  	Sensor accelerometer;
  	SensorManager sm;
  	TextView acceleration;
  	TextView time;
  	float x, y, z;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        displayView = (TextView) findViewById(R.id.displayView);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
        
        //time
        time = (TextView) findViewById(R.id.time);
        		
		//set up Accelerometer service and its corresponding textViews
		acceleration = (TextView) findViewById(R.id.acceleration);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
    retrieveLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            	int hour = Calendar.getInstance().get(Calendar.HOUR);
            	int minute = Calendar.getInstance().get(Calendar.MINUTE);
            	int second = Calendar.getInstance().get(Calendar.SECOND);
            	time.setText(Integer.toString(hour) + " | " + Integer.toString(minute) + " | " + Integer.toString(second));
            	acceleration.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
            }
    });        
        
    }    

    protected void showCurrentLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {

            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s \n Bearing: %3$s",
                    location.getLongitude(), location.getLatitude(), location.getBearing()
            );
            displayView.setText(message);
            
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }

    }   

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s \n Bearing: %3$s",
                    location.getLongitude(), location.getLatitude(), location.getBearing()
            );
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(MainActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }
    
    //accelerometer
    public void onSensorChanged(SensorEvent event) {
 	   //acceleration.setText("X: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
 	   x = event.values[0];
 	   y = event.values[1];
 	   z = event.values[2];
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

    
}
