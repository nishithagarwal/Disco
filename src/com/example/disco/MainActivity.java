package com.example.disco;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void clickSearch(View view)
	{
		TextView m_title = (TextView) findViewById(R.id.editText1);
		Spinner m_type = (Spinner)findViewById(R.id.spinner1);
		
		String title=m_title.getText().toString();
		title = title.replaceAll(" ", "+");
		String type=m_type.getSelectedItem().toString().toLowerCase();

		try {
			title = URLEncoder.encode(title, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String urlString = "http://cs-server.usc.edu:22375/examples/servlet/HelloWorldExample?title="+title+"&type="+type;

		
		CallServlet getdata = new CallServlet();
		
		String jsonString ="default";
		try {
			jsonString = getdata.execute(urlString).get();
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		Intent intent = new Intent("android.intent.action.DISPLAY");
		intent.putExtra("JSONString", jsonString);
		intent.putExtra("type", type);
		startActivity(intent);
		 
	}
	


}
