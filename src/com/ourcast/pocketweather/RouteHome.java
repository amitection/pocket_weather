package com.ourcast.pocketweather;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RouteHome extends Activity{

	EditText source, destination;
	Button find, currentAddress;
	Location currentLocation;
	
	double latSource, latDestination, longSource, longDestination;
	private double currentLatitude;
	private double currentLongitude;
	static String address = "Vastrapur Lake, Gujarat 380015, India";
	static String address2 = "Santej - Vadsar Rd, Gujarat 382165, India";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_home);
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		LocationListener locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
		
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				updateLocation(location);
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		currentAddress = (Button) findViewById(R.id.btnroutecuraddress);
		currentAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getAddressFromLatLong();
			}
		});
		
		find = (Button) findViewById(R.id.btnroutefind);
		find.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLatLongFromAddress(address, address2);
				Toast.makeText(RouteHome.this, "Clicked find", Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(RouteHome.this, DisplayRoute.class);
				i.putExtra("LatSource", latSource);
				i.putExtra("LongSource", longSource);
				i.putExtra("LatDest", latDestination);
				i.putExtra("LongDest", longDestination);
				startActivity(i);
			}
		});
		
		
	}
	
	public void getAddressFromLatLong()
	{
		try
		{
			Geocoder coder = new Geocoder(this, Locale.UK);
			List<Address> address = coder.getFromLocation(currentLatitude, currentLongitude, 1);
			
			if(address.size()>0)
			{
				StringBuilder result = new StringBuilder();
				for(int i = 0; i < address.size(); i++)
				{
					Address addressFinal = address.get(i);
					int maxIndex = addressFinal.getMaxAddressLineIndex();
					for(int k = 0; k<=maxIndex; k++)
					{
						result.append(addressFinal.getAddressLine(k));
					}
				}
				Toast.makeText(RouteHome.this, "here address >"+result, Toast.LENGTH_LONG).show();
				source.setText(result);
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void getLatLongFromAddress(String sourceAddress, String destinationAddress)
	{
		//String sourceAddress = address;
		//String destinationAddress = address2;
		latSource = 0.0;
		longSource = 0.0;
		
		Geocoder geocoder = new Geocoder(this, Locale.UK);
		Toast.makeText(RouteHome.this, "Inside Method", Toast.LENGTH_SHORT).show();
		
		try
		{
			List<Address> addressSource = geocoder.getFromLocationName(sourceAddress, 1);
			List<Address> addressDestination = geocoder.getFromLocationName(destinationAddress, 1);
			
			Log.i("After List Address...", "Working");
			if(addressSource.size() >0)
			{
				Log.i("Inside If...", "Working");
				latSource = (addressSource.get(0).getLatitude());
				longSource = (addressSource.get(0).getLongitude());
				Log.i("...", "Working");
				//Toast.makeText(RouteHome.this, "Lattitude "+latSource + " Longitude "+longSource, Toast.LENGTH_LONG).show();
				
			}
			
			if(addressDestination.size() >0)
			{
				Log.i("Inside If...", "Working");
				latDestination = (addressDestination.get(0).getLatitude());
				longDestination = (addressDestination.get(0).getLongitude());
				Log.i("...", "Working");
				//Toast.makeText(RouteHome.this, "Lattitude "+latSource + " Longitude "+longSource, Toast.LENGTH_LONG).show();
				
			}
			Log.i("...", "Working");
			Toast.makeText(RouteHome.this, "SourceLattitude::>  "+latSource + " SourceLongitude::>  "+longSource+" DestinationLattitude>>:  "+latDestination+" DestinationLongitude>>:  "+longDestination, Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			Log.e("GeoCoder", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void updateLocation(Location location)
	{
		currentLocation = location;
		currentLatitude = currentLocation.getLatitude();
		currentLongitude = currentLocation.getLongitude();
		
		//Toast.makeText(RouteHome.this, "Location Update::"+currentLatitude+" >>>>> "+currentLongitude, Toast.LENGTH_SHORT).show();
		
	}
}
