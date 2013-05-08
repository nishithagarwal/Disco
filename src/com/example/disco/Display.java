package com.example.disco;

import java.io.IOException;
import java.io.InputStream;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;


import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class Display extends Activity {

	JSONArray resultArray;
	int id;
	String type;


	private void publishFeedDialog() throws JSONException {
		Bundle params = new Bundle();

		if(type.equals("artists"))
		{
			params.putString("name", resultArray.getJSONObject(id).getString("name").trim());
			params.putString("caption", "I like "+resultArray.getJSONObject(id).getString("name").trim()+" who is active since "+resultArray.getJSONObject(id).getString("year").trim());
			params.putString("description", "Genre of Music is: "+resultArray.getJSONObject(id).getString("genre").trim());
			params.putString("link", resultArray.getJSONObject(id).getString("details"));
			if(!resultArray.getJSONObject(id).getString("cover").equals("N/A"))
				params.putString("picture", resultArray.getJSONObject(id).getString("cover").trim());

			if(!resultArray.getJSONObject(id).getString("details").equals("N/A")){
				JSONObject properties = new JSONObject();
				JSONObject prop1 = new JSONObject();
				prop1.put("text", "here");
				prop1.put("href", resultArray.getJSONObject(id).getString("details"));
				properties.put("Look at details", prop1);
				params.putString("properties", properties.toString());
			}
		}
		if(type.equals("albums"))
		{
			params.putString("name", resultArray.getJSONObject(id).getString("title").trim());
			params.putString("caption", "I like "+resultArray.getJSONObject(id).getString("title").trim()+" released in "+resultArray.getJSONObject(id).getString("year").trim());
			params.putString("description", "Artist: "+resultArray.getJSONObject(id).getString("artist").trim()+"  Genre: "+resultArray.getJSONObject(id).getString("genre").trim());
			params.putString("link", resultArray.getJSONObject(id).getString("details"));
			if(!resultArray.getJSONObject(id).getString("cover").equals("N/A"))
				params.putString("picture", resultArray.getJSONObject(id).getString("cover").trim());
			else
				params.putString("picture", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzuydwsmhl0aFXwzMgMEBdP2tD7j8riieVlVc8tfRucnOYYCW1");
			

			if(!resultArray.getJSONObject(id).getString("details").equals("N/A")){
				JSONObject properties = new JSONObject();
				JSONObject prop1 = new JSONObject();
				prop1.put("text", "here");
				prop1.put("href", resultArray.getJSONObject(id).getString("details"));
				properties.put("Look at details", prop1);
				params.putString("properties", properties.toString());
			}
		}
		if(type.equals("songs"))
		{
			params.putString("name", resultArray.getJSONObject(id).getString("title").trim());
			params.putString("caption", "I like "+resultArray.getJSONObject(id).getString("title").trim()+" composed by "+resultArray.getJSONObject(id).getString("composer").trim());
			params.putString("description", "Performer: "+resultArray.getJSONObject(id).getString("performer").trim());
			params.putString("link", resultArray.getJSONObject(id).getString("details"));
			params.putString("picture", "http://static.tumblr.com/jn9hrij/20Ul2zzsr/albumart.jpg");

			if(!resultArray.getJSONObject(id).getString("details").equals("N/A")){
				JSONObject properties = new JSONObject();
				JSONObject prop1 = new JSONObject();
				prop1.put("text", "here");
				prop1.put("href", resultArray.getJSONObject(id).getString("details"));
				properties.put("Look at details", prop1);
				params.putString("properties", properties.toString());
			}
		}


		WebDialog feedDialog = (
				new WebDialog.FeedDialogBuilder(Display.this,
						Session.getActiveSession(),
						params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									// When the story is posted, echo the success
									// and the post Id.
									final String postId = values.getString("post_id");
									if (postId != null) {
										Toast.makeText(Display.this,
												"Posted story, id: "+postId,
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
										Toast.makeText(Display.this.getApplicationContext(), 
												"Publish cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(Display.this.getApplicationContext(), 
											"Publish cancelled", 
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(Display.this.getApplicationContext(), 
											"Error posting story", 
											Toast.LENGTH_SHORT).show();
								}
							}

						})
						.build();
		feedDialog.show();
	}


	@Override
	protected Dialog onCreateDialog(int id1) {
		Dialog dialog = null;
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Post to Facebook");

		builder.setPositiveButton("Facebook", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id1) {
				
				

				Session session = Session.openActiveSession(Display.this, true, new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state, Exception exception) {

					}
				});

				if (session.isOpened()) {
					try {
						publishFeedDialog();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		
		try {
			if(type.equals("songs") && !resultArray.getJSONObject(id).getString("cover").equals("N/A"))
			{
				builder.setNeutralButton("Sample Music", new DialogInterface.OnClickListener() {
				//builder.setPositiveButton("Sample Music", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id1) {

						
						 MediaPlayer mediaPlayer = new MediaPlayer();
					        try {
					        	mediaPlayer.setDataSource(resultArray.getJSONObject(id).getString("cover"));
					            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					            mediaPlayer.prepare();
					        } catch (IOException e) {
					            e.printStackTrace();
					        } catch (JSONException e) {
								e.printStackTrace();
							}

					        mediaPlayer.start();

					}
				});
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		dialog = builder.create();

		return dialog;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		
		PackageInfo info;
		try {
		    info = getPackageManager().getPackageInfo("com.example.disco", PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md;
		        md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        String something = new String(Base64.encode(md.digest(), 0));
		        //String something = new String(Base64.encodeBytes(md.digest()));
		        Log.e("hash key", something);
		    }
		} catch (NameNotFoundException e1) {
		    Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
		    Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
		    Log.e("exception", e.toString());
		}


		String jsonString = getIntent().getExtras().getString("JSONString");
		type = getIntent().getExtras().getString("type");
		TextView output = (TextView) findViewById(R.id.textView10);

		System.out.println(jsonString);
		//System.out.println(type);



		try {
			JSONObject results = new JSONObject(jsonString);
			JSONObject innerObject = results.getJSONObject("results");
			resultArray = innerObject.getJSONArray("result");

			JSONObject o = new JSONObject();

			TableLayout tableLayout = new TableLayout(getApplicationContext());
			//final TableRow tableRow;
			TextView textView;
			ImageView imageView;

			String name=null;

			for (int i = 0; i<resultArray.length();i++)
			{
				final TableRow tableRow = new TableRow(getApplicationContext());
				tableRow.setId(i);
				o = resultArray.getJSONObject(i);

				if(type.equals("artists"))
				{
					//IMAGE
					imageView = new ImageView(Display.this);
					if(!o.getString("cover").equals("N/A")){
						Bitmap bmp = new DownloadImageTask(imageView).execute(o.getString("cover")).get();
						//bmp=Bitmap.createScaledBitmap(bmp, 50,50, true);
						imageView.setImageBitmap(bmp);
						imageView.setLayoutParams(new TableRow.LayoutParams(100,100));
						tableRow.addView(imageView);
					}
					else{
						Bitmap bmp = new DownloadImageTask(imageView).execute("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464").get();
						//bmp=Bitmap.createScaledBitmap(bmp, 5,5, true);
						imageView.setImageBitmap(bmp);
						imageView.setLayoutParams(new TableRow.LayoutParams(100,100));
						tableRow.addView(imageView);

					}
					//NAME
					textView = new TextView(Display.this);
					textView.setText("Name: "+o.getString("name").trim());
					textView.setPadding(5,0,0,0);
					textView.setTextSize(12);
					textView.setWidth(125);
					tableRow.addView(textView);
					//GENRE
					textView = new TextView(Display.this);
					textView.setText("Genre: "+o.getString("genre").trim());
					textView.setPadding(5,0,0,0);
					textView.setTextSize(12);
					textView.setWidth(125);
					tableRow.addView(textView);
					//YEAR
					textView = new TextView(Display.this);
					textView.setText("Year: "+o.getString("year").trim());
					textView.setPadding(5,0,5,0);
					textView.setTextSize(12);
					textView.setWidth(140);
					tableRow.addView(textView);
				}

				if(type.equals("albums"))
				{
					//IMAGE
					imageView = new ImageView(Display.this);
					if(!o.getString("cover").equals("N/A")){
						Bitmap bmp = new DownloadImageTask(imageView).execute(o.getString("cover")).get();
						//bmp=Bitmap.createScaledBitmap(bmp, 50,50, true);
						imageView.setImageBitmap(bmp);
						imageView.setLayoutParams(new TableRow.LayoutParams(80,80));
						tableRow.addView(imageView);
					}
					else{
						Bitmap bmp = new DownloadImageTask(imageView).execute("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzuydwsmhl0aFXwzMgMEBdP2tD7j8riieVlVc8tfRucnOYYCW1").get();
						//bmp=Bitmap.createScaledBitmap(bmp, 5,5, true);
						imageView.setImageBitmap(bmp);
						imageView.setLayoutParams(new TableRow.LayoutParams(80,80));
						tableRow.addView(imageView);

					}
					//TITLE
					textView = new TextView(Display.this);
					textView.setText("Title: "+o.getString("title").trim());
					textView.setPadding(5,0,0,0);
					textView.setTextSize(12);
					textView.setWidth(100);
					tableRow.addView(textView);
					//ARTIST
					textView = new TextView(Display.this);
					textView.setText("Artist: "+o.getString("artist").trim());
					textView.setPadding(5,0,0,0);
					textView.setTextSize(12);
					textView.setWidth(100);
					tableRow.addView(textView);
					//GENRE
					textView = new TextView(Display.this);
					textView.setText("Genre: "+o.getString("genre").trim());
					textView.setPadding(5,0,0,0);
					textView.setTextSize(12);
					textView.setWidth(100);
					tableRow.addView(textView);
					//YEAR
					textView = new TextView(Display.this);
					textView.setText("Year: "+o.getString("year").trim());
					textView.setPadding(5,0,5,0);
					textView.setTextSize(12);
					textView.setWidth(80);
					tableRow.addView(textView);
				}
				if(type.equals("songs"))
				{
					//IMAGE
					imageView = new ImageView(Display.this);
					
						Bitmap bmp = new DownloadImageTask(imageView).execute("http://static.tumblr.com/jn9hrij/20Ul2zzsr/albumart.jpg").get();
						//bmp=Bitmap.createScaledBitmap(bmp, 50,50, true);
						imageView.setImageBitmap(bmp);
						imageView.setLayoutParams(new TableRow.LayoutParams(80,80));
						imageView.setPadding(0, 0, 5, 0);
						tableRow.addView(imageView);

					//TITLE
					textView = new TextView(Display.this);
					textView.setText("Title: "+o.getString("title").trim());
					textView.setPadding(5, 5, 5, 5);
					textView.setTextSize(10);
					textView.setWidth(125);
					tableRow.addView(textView);
					//PERFORMER
					textView = new TextView(Display.this);
					textView.setText("Performer: "+o.getString("performer").trim());
					textView.setPadding(5, 5, 5, 5);
					textView.setTextSize(10);
					textView.setWidth(125);
					tableRow.addView(textView);
					//COMPOSER
					textView = new TextView(Display.this);
					textView.setText("Composer: "+o.getString("composer").trim());
					textView.setPadding(5, 5, 5, 5);
					textView.setTextSize(10);
					textView.setWidth(150);
					tableRow.addView(textView);
				}


				tableRow.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						Log.e("TAG", " clicked ID: "+tableRow.getId());

						id = tableRow.getId();

						showDialog(tableRow.getId());


					}
				});

				tableLayout.addView(tableRow);

			}
			setContentView(tableLayout);

			output.append("2");




		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
