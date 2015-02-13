package com.ourcast.pocketweather;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomBaseAdapter extends BaseAdapter implements OnInitListener {
	Context context;
	List<Bean> bean;
	ImageView weatherImage;
	TextView weatherDate, weatherCity, weatherMinimum, weatherMaximum, weatherDescription;
	Button buttonSpeak;
	String citySpeak, dateSpeak, descriptionSpeak, maximumSpeak, minimumSpeak, weatherURL;
	TextToSpeech tts;
	Bean userBean;
	Bitmap myBitmap;
	
	public CustomBaseAdapter(Context context, List<Bean> bean) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bean = bean;
		tts = new TextToSpeech(context, null);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bean.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bean.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return bean.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.custum_base_layout, null);
			weatherImage = (ImageView) convertView.findViewById(R.id.displayImage);
			weatherCity = (TextView) convertView.findViewById(R.id.displayCity);
			weatherDescription = (TextView) convertView.findViewById(R.id.displayDescription);
			weatherMaximum = (TextView) convertView.findViewById(R.id.displayMax);
			weatherMinimum = (TextView) convertView.findViewById(R.id.displayMin);
			weatherDate = (TextView) convertView.findViewById(R.id.displayDate);
			buttonSpeak = (Button) convertView.findViewById(R.id.Speak);
			
			userBean = (Bean) getItem(position);
			weatherCity.setText(userBean.getCity());
			weatherDescription.setText(userBean.getDescription());
			weatherMaximum.setText(userBean.getMaximumTemprature());
			weatherMinimum.setText(userBean.getMinimumTemprature());
			weatherDate.setText(userBean.getDate());
			weatherURL = userBean.getImageUrl();
			new ImageDownload().execute();
			
			Log.i("Executing Rest Line>>>", "Skippedddddd");
			buttonSpeak.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				String cityName = weatherCity.getText().toString();	
				String dateValue = weatherDate.getText().toString();
				String maximumValue = weatherMaximum.getText().toString();
				String minimumValue = weatherMinimum.getText().toString();
				String descriptionValue = weatherDescription.getText().toString();
				
				citySpeak = "Temprature for city "+cityName+"";
				dateSpeak = " on Date "+dateValue+"";
				maximumSpeak = "will be Maximum upto "+maximumValue+" degree ";
				minimumSpeak = " and Minimum upto"+minimumValue+" degree ";
				descriptionSpeak = "and The atmosphere seems to be "+descriptionValue+"";
				
				speakTempratureValues();
				}
			});
		}
		return convertView;
	}
	
	private class ImageDownload extends AsyncTask<String, Void, Bitmap>{
		
		protected Bitmap doInBackground(String... arg0){
			
			try{
				Log.e("src",weatherURL);
	        	URL url = new URL(weatherURL);
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
				Log.i("OnPost>>>", ""+result);
				weatherImage.setImageBitmap(result);
			 }
			
		}
	}

	protected void speakTempratureValues() {
		// TODO Auto-generated method stub
		tts.setSpeechRate(-4);
		tts.speak(citySpeak, TextToSpeech.QUEUE_FLUSH, null);
		tts.speak(dateSpeak, TextToSpeech.QUEUE_ADD, null);
		tts.speak(maximumSpeak, TextToSpeech.QUEUE_ADD, null);
		tts.speak(minimumSpeak, TextToSpeech.QUEUE_ADD, null);
		tts.speak(descriptionSpeak, TextToSpeech.QUEUE_ADD, null);
		tts.speak("Thank You", TextToSpeech.QUEUE_ADD, null);
	}


	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if(status==TextToSpeech.SUCCESS){
			int result = tts.setLanguage(Locale.getDefault());
			
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
			else{
				
				speakTempratureValues();
			}
		}
		else{
			Log.e("TTS", "Initialization Failed");
		}
	}

}
