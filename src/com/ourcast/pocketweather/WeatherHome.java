package com.ourcast.pocketweather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WeatherHome extends Activity {

	protected static final int DATE_DIALOG = 0;

	public static List<Bean> arrayList = new ArrayList<Bean>();
	
	private String DATA = "data";
	private String WEATHER = "weather";
	private String DATE = "date";
	private String MINTEMPC = "tempMinC";
	private String MAXTEMPC = "tempMaxC";
	private String MINTEMPF = "tempMinF";
	private String MAXTEMPF = "tempMaxF";
	private String W_DESC = "weatherDesc";
	private String VAL = "value";
	private String W_IMG = "weatherIconUrl";
	private String VAL_IMG = "value";
	private String CITY = "";
	private EditText days, city;
	private RadioGroup rg ;
	private Button search, datePick;
	private ImageView imageUrl;
	private	String value, valueimg, date, cityName, daysValue, itemSelected, TempMAX, TempMin, radioSelected, valueList;
	StringBuilder completeDate;
	private int  month, day, year;
	
	Spinner tempFormat, updateInterval;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_home);
		
		//tempFormat = (Spinner) findViewById(R.id.spinner);
		city = (EditText) findViewById(R.id.EdtCityName);
		days = (EditText) findViewById(R.id.editText1);
		datePick = (Button) findViewById(R.id.btnGetDate);
		//updateInterval = (Spinner) findViewById(R.id.spinner1);
		search = (Button) findViewById(R.id.BtnCityName);
		rg = (RadioGroup)findViewById(R.id.rgTempFormat);
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = rg.getCheckedRadioButtonId();
				RadioButton rbSelected = (RadioButton) findViewById(id);  
				radioSelected =  rbSelected.getText().toString();
			}
		});
		
		datePick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}
		});
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		Log.i("System Daye Date", ""+year+" "+month+" "+day);

		updateDate();
		
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 daysValue = days.getText().toString();
				cityName = city.getText().toString();
				
				new JSONParse().execute();
			}
		});
		
	} // OnCreate Ends
	
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DATE_DIALOG: 
			return new DatePickerDialog(this, dateListener, year, month, day);
		}
		return null;
	}
	
	private void updateDate(){
		
		completeDate = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day);
		Log.i("Updated Date in Update", ""+completeDate);
	}
	
	private DatePickerDialog.OnDateSetListener dateListener = new OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int yr, int mOfYr,
				int dayOfM) {
			// TODO Auto-generated method stub
			year = yr;
			month = mOfYr;
			day = dayOfM;
			
			Log.i("Updated Date", ""+year+" "+month+" "+day);
			updateDate();
		}
	};

	@SuppressLint("NewApi")
private class JSONParse extends AsyncTask<String, String, JSONObject> 
{

		private ProgressDialog pDialog;

		String url = "http://api.worldweatheronline.com/free/v1/weather.ashx?q="+cityName+"&format=json&num_of_days="+daysValue+"&key=vh988wvnqkre5a5w3ywm4sp9";
		String URL = "http://api.worldweatheronline.com/free/v1/weather.ashx?q="+cityName+"&format=json&date="+completeDate+"&key=vh988wvnqkre5a5w3ywm4sp9";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(WeatherHome.this);
			pDialog.setMessage("Processing your request ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			JSONReader readerJSON = new JSONReader();
			
			if(daysValue.equals("1")||daysValue.equals("2")||daysValue.equals("3")||daysValue.equals("4")||daysValue.equals("5"))
			{
				JSONObject readerDaysJSONObj = readerJSON.JSONRead(url);
				Log.i("First URL", "Okay you are here");
				return readerDaysJSONObj;
			}
			else if(completeDate!=null){
				JSONObject readerDateJSONObj = readerJSON.JSONRead(URL);
				Log.i("Second URL", "Okay you are here now");
				return readerDateJSONObj;
			}
			else{
				if(daysValue!=null && completeDate!=null) {
				Toast.makeText(WeatherHome.this, "Please select either number of days or a date", Toast.LENGTH_LONG).show();
				Log.i("No Where", "Okay you are at wrong area");
				
			}
				return null;
			}			
		}

		@Override
		protected void onPostExecute(JSONObject json) 
		{
			pDialog.dismiss();

			try 
			{
				Log.i("Complete date", ">>>>>>>..."+completeDate);
				
				JSONObject dataObj = json.getJSONObject(DATA);
				JSONArray weatherArr = dataObj.getJSONArray(WEATHER);

				for (int i = 0; i < weatherArr.length(); i++) {
					
					JSONObject json1 = weatherArr.getJSONObject(i);
				
					 date = json1.getString(DATE);
					
					if(radioSelected.equals("Celcius")){
					
						TempMAX = json1.getString(MAXTEMPC);
						TempMin = json1.getString(MINTEMPC);
					}
					else{
						
						TempMAX = json1.getString(MAXTEMPF);
						TempMin = json1.getString(MINTEMPF);
					}
					
					JSONArray descriptionArr = json1.getJSONArray(W_DESC);

					for (int j = 0; j < descriptionArr.length(); j++) {
						
						JSONObject json2 = descriptionArr.getJSONObject(j);
						value = json2.getString(VAL).toString();
					
					}	
					 
					JSONArray image = json1.getJSONArray(W_IMG);

					for (int k = 0; k < image.length(); k++) {
						JSONObject jObjImage = image.getJSONObject(k);
						valueimg = jObjImage.getString(VAL_IMG);
					}

			/*	Intent intent = new Intent(WeatherHome.this, DisplayWeather.class);
				Bundle b = new Bundle();
				b.putString("CITY", cityName);
				b.putString("DATE",date);
				b.putString("MAXTEMP",TempMAX);
				b.putString("MINTEMP",TempMin);
				b.putString("DESCRIPTION",value);
				intent.putExtras(b);
				startActivity(intent);*/
				
				Bean weatherHomeBean = new Bean();
				weatherHomeBean.setCity(cityName);
				weatherHomeBean.setDate(date);
				weatherHomeBean.setMaximumTemprature(TempMAX);
				weatherHomeBean.setMinimumTemprature(TempMin);
				weatherHomeBean.setDescription(value);
				weatherHomeBean.setImageUrl(valueimg);
				
				arrayList.add(weatherHomeBean);
				
			}
				
				Intent intent = new Intent(WeatherHome.this, DisplayWeathernew.class);
				startActivity(intent);
				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
					
	}
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
