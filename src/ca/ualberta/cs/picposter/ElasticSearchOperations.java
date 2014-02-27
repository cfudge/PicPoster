package ca.ualberta.cs.picposter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.cs.picposter.model.PicPostModel;

import com.google.gson.Gson;


public class ElasticSearchOperations{
	//takes picture, and sends to server.
	//cannot do networking in android's main UI thread
	public static void pushPicPostModel(final PicPostModel model){
		Thread thread = new Thread(){
			
			@Override
			public void run(){
				
				Gson gson = new Gson();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/cfudge/");
				
				try{
					request.setEntity(new StringEntity(gson.toJson(model)));
					HttpResponse response = client.execute(request);
					
					Log.e("ElasticSearch", response.getStatusLine().toString());
					
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					
					String output = reader.readLine();
					while(output != null){
						Log.e("ElasticSearch", output);
						output = reader.readLine();
					}
					
				} 
				catch (Exception e){
					e.printStackTrace();
				}
			}
			
		};
		
		thread.start();
	}
	
}
