package com.example.disco;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

public class CallServlet extends AsyncTask<String, Void, String> {
		private Exception exception;
	
		
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			InputStream stream = null;
			 try {
	        URL url = new URL(arg0[0]);
			System.out.println("URL: "+url.toString());
	        URLConnection connection = url.openConnection();
	        try {
	            HttpURLConnection httpConnection = (HttpURLConnection) connection;
	            httpConnection.setRequestMethod("GET");
	            httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
	            httpConnection.connect();

	            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                stream = httpConnection.getInputStream();
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
			 }

			 catch (Exception ex) {
		            ex.printStackTrace();
		            return null;
			 }
			 
			 StringBuffer output = new StringBuffer("");
		        try {
		            BufferedReader buffer = new BufferedReader(
		                    new InputStreamReader(stream));
		            String s = "";
		            while ((s = buffer.readLine()) != null)
		                output.append(s);
		        } catch (IOException e1) {
		            e1.printStackTrace();
		        }
		        
		        //System.out.println(output.toString());
		        
			 return output.toString();
			 
		}
	
	}