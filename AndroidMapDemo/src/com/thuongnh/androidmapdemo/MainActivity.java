package com.thuongnh.androidmapdemo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	MapView mapView;
	GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mapView = (MapView)findViewById(R.id.mapView);
        
        //#IMPORTANT: Without this line, your code will not work
		mapView.onCreate(savedInstanceState);

		map = mapView.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		//Map Settings
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setZoomControlsEnabled(false);
		
		
		
		
		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.d("GOOGLE PLAY SERVICES", "NOT AVAILABLE");
		}
		
		//Enable GPS
		map.setMyLocationEnabled(true);
		
		//Set the map to current location
		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
				//Get current location
				LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
				
				//Add a marker with an image to current location
				map.addMarker(new MarkerOptions().position(position)
												.title("My location")
												.snippet("Home sweet home")
												.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_big)));
				
				//Get address from location
				Geocoder geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());
				List<Address> addresses;
				try {
					addresses = geoCoder.getFromLocation(position.latitude, position.longitude, 1);
					if (addresses.size()>0){
						
						//Get the first address from the list and get its address lines
						Address address = addresses.get(0);
						String addressString = "";
						for (int i=0;i<address.getMaxAddressLineIndex();i++) {
							addressString+=address.getAddressLine(i)+ " ";
						}
						Toast.makeText(MainActivity.this, addressString, Toast.LENGTH_LONG).show();
					}
				} catch (IOException e) {
					Log.d("GEOCODER", e.getMessage(), e);
				}
				
				
				
				//Zoom parameter is set to 14
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 14);
				
				//Use map.animateCamera(update) if you want moving effect
				map.moveCamera(update);
				mapView.onResume();
			}
		});
		
		mapView.onResume();        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
