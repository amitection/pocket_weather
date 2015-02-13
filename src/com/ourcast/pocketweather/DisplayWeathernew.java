package com.ourcast.pocketweather;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayWeathernew extends Activity {
	
	String city, date, maximumTemp, minimumTemp, description, weatherImageUrl;
	ListView weatherList;
	List <Bean> bean;
	Bitmap myBitmap, newBitmap;
	CustomBaseAdapter baseAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_weather);
		
		bean = new ArrayList<Bean>();
		weatherList = (ListView) findViewById(R.id.lvWeather);
		
		for(int i=0; i<WeatherHome.arrayList.size(); i++)
		{
			Toast.makeText(DisplayWeathernew.this, ">> "+WeatherHome.arrayList.size(), Toast.LENGTH_SHORT).show();
			city = WeatherHome.arrayList.get(i).getCity(); //WeatherHome.arrayList.get(index).getCity();
			date = WeatherHome.arrayList.get(i).getDate();
			maximumTemp = WeatherHome.arrayList.get(i).getMaximumTemprature();
			minimumTemp = WeatherHome.arrayList.get(i).getMinimumTemprature();
			description = WeatherHome.arrayList.get(i).getDescription();
			weatherImageUrl = WeatherHome.arrayList.get(i).getImageUrl();
			
			Toast.makeText(this, "City "+city+" Date "+date+" Minimum "+minimumTemp+" Maximum "+maximumTemp+" Desc "+description+" URL "+weatherImageUrl, Toast.LENGTH_SHORT).show();
		//	new ImageDownload().execute();
			bean.add(new Bean(date, maximumTemp, minimumTemp, description, city, weatherImageUrl));
			Toast.makeText(this, "City "+city+" Date "+date+" Minimum "+minimumTemp+" Maximum "+maximumTemp+" Desc "+description+" URL "+weatherImageUrl, Toast.LENGTH_SHORT).show();
			baseAdapter = new CustomBaseAdapter(this, bean);
		}
		
		
		weatherList.setAdapter(baseAdapter);
		//weatherList.setAdapter(baseAdapter);
		
		/*weatherList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String value = weatherList.getItemAtPosition(position).toString();
				Toast.makeText(DisplayWeathernew.this, "wethrList>> "+value, Toast.LENGTH_LONG).show();
				
			}
		});*/
	}
	
/*private class ImageDownload extends AsyncTask<String, Void, Bitmap>{
	
	protected Bitmap doInBackground(String... arg0){
		
		try{
			Log.e("src",weatherImageUrl);
        	URL url = new URL(weatherImageUrl);
        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	connection.setDoInput(true);
        	connection.connect();
        	InputStream input = connection.getInputStream();
        	myBitmap = BitmapFactory.decodeStream(input);    	
        	Log.e("Bitmap","returned");
        	return myBitmap;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	protected void onPostExecute(Bitmap result){
		 if(result!=null)
		 {
			img.setImageBitmap(result);
		 }
		 else
		 {
	        img.setImageResource(R.drawable.button_display_weather);
	     }
	}
}*/
		

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (baseAdapter.tts != null)
	    {
	        baseAdapter.tts.stop();
	        baseAdapter.tts.shutdown();
	    }
		super.onDestroy();
	}
		
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		WeatherHome.arrayList.clear();
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,0,0,"Exit");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==0)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
		
	}
}
