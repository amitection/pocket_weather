package com.ourcast.pocketweather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class JSONReader {
	
	JSONObject json = null;
	StringBuilder jsonStr = null;
	InputStream is = null;
	
	public JSONObject JSONRead(String url){
		
		try
		{
			jsonStr = new StringBuilder();
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            
            is = httpEntity.getContent();
            BufferedReader buffer=new BufferedReader(new InputStreamReader(is, "ISO-8859-1"), 8);
            
		    String line;
		       			
		    while((line = buffer.readLine()) != null){
			
		    	jsonStr.append(line);
		    }
		    
		    Log.i("JSONstr", jsonStr.toString());
		    
		    json = new JSONObject(jsonStr.toString());
			is.close();
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		// System.out.println(">>>>>" + json.toString());
		return json;
	}

}
