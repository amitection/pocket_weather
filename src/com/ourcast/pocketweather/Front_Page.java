package com.ourcast.pocketweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Front_Page extends Activity {

	ImageView weatherhome, routehome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front_page);
	
		weatherhome = (ImageView) findViewById(R.id.imgweather);
		weatherhome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    Intent weather = new Intent(Front_Page.this, WeatherHome.class);
			    startActivity(weather);  
			}
		});
		
		routehome = (ImageView) findViewById(R.id.imgroute);
		routehome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent route = new Intent(Front_Page.this, RouteHome.class);
			    startActivity(route); 
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.front__page, menu);
		return true;
	}

}
