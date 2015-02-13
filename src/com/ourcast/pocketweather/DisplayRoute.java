package com.ourcast.pocketweather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DisplayRoute extends Activity{

	GoogleMap mMap;
	Context context;
	String lang, URL;
	private Location location;
	String distance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_route);
	
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		
		Intent receiveLatLang = getIntent();
		Bundle bundle = receiveLatLang.getExtras();
		double sourceLat = bundle.getDouble("LatSource");
		double destLat = bundle.getDouble("LatDest");
		double sourceLong = bundle.getDouble("LongSource");
		double destLong = bundle.getDouble("LongDest");
		//double destLats = receiveLatLang.getDoubleExtra("LatDest", (Double) null);
		//Camera Functionality to focus on a point
		CameraPosition cameraPos = new CameraPosition.Builder().target(new LatLng(sourceLat, sourceLong)).zoom(12).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
		
		//Adding Marker to both beginning and ending point
		MarkerOptions markerSource = new MarkerOptions().position(new LatLng(sourceLat, sourceLong)).title(RouteHome.address);
		MarkerOptions markerDest = new MarkerOptions().position(new LatLng(destLat, destLong)).title(RouteHome.address2);
		mMap.addMarker(markerSource);
		mMap.addMarker(markerDest);
		
		float[] results = new float[5];
		location.distanceBetween(sourceLat, sourceLong, destLat, destLong, results);
		distance = Float.toString(results[0]/1000);
		
		
		Toast.makeText(DisplayRoute.this, "LatS: "+sourceLat +" LatD: "+destLat+" LongS:: "+sourceLong+" LongD:: "+destLong , Toast.LENGTH_SHORT).show();
		URL = makeURL(sourceLat, sourceLong, destLat, destLong, "driving");
		new connectAsyncTask(URL).execute();
		
	}
	
	private String makeURL (double sourcelat, double sourcelog, double destlat, double destlog,String mode){
		StringBuilder urlString = new StringBuilder();

		/*if(mode == null)
			mode = "driving";
*/
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString( sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString( destlat));
		urlString.append(",");
		urlString.append(Double.toString( destlog));
		urlString.append("&sensor=false&mode="+mode+"&alternatives=true&language="+lang);
		return urlString.toString();
	}
	
	public void drawPath(String  result) {

	    try {
	            //Tranform the string into a json object
	           final JSONObject json = new JSONObject(result);
	           JSONArray routeArray = json.getJSONArray("routes");
	           JSONObject routes = routeArray.getJSONObject(0);
	           JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
	           String encodedString = overviewPolylines.getString("points");
	           List<LatLng> list = decodePoly(encodedString);

	           Log.i("List > ", list.toString());
	           
	           for(int z = 0; z<list.size()-1;z++){
	                LatLng src= list.get(z);
	                Log.i("SecondList > ", ""+list.size());
	                Log.i("List > ", ""+list.get(z));
	                LatLng dest= list.get(z+1);
	                Log.i("List > ", ""+list.get(z+1));
	                Polyline line = mMap.addPolyline(new PolylineOptions()
	                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
	                .width(4f)
	                .color(Color.MAGENTA).geodesic(true));
	            }

	    } 
	    catch (JSONException e) {

	    }
	} 
	
	private List<LatLng> decodePoly(String encoded) {

	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        LatLng p = new LatLng( (((double) lat / 1E5)),
	                 (((double) lng / 1E5) ));
	        poly.add(p);
	    }

	    return poly;
	}

	private class connectAsyncTask extends AsyncTask<Void, Void, String>{
	    private ProgressDialog progressDialog;
	    String url;
	    connectAsyncTask(String urlPass){
	        url = urlPass;
	    }
	    @Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(DisplayRoute.this);
	        progressDialog.setMessage("Fetching route, Please wait...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.show();
	    }
	    @Override
	    protected String doInBackground(Void... params) {
	        JSONParser jParser = new JSONParser();
	        String json = jParser.getJSONFromUrl(url);
	        return json;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);   
	        progressDialog.hide();        
	        if(result!=null){
	            drawPath(result);
	        }
	        
	        Toast.makeText(DisplayRoute.this, "Distance is approx  >>>  "+distance+" Kilometers", 30000000).show();
	    }
	}

	

}
