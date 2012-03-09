package de.ring0.hackspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class HackerSpaceStatusAPI implements ResponseHandler<Boolean>, Runnable {
//	private final static String TAG = HackerSpaceStatusAPI.class.getSimpleName();
	private final static String VERSION = "0.1";
	
	private APICallback apiCallback;
	private HttpClient client;
	private HttpGet get;
	
	public HackerSpaceStatusAPI(String url, APICallback callback) {
		apiCallback = callback;
		client = new DefaultHttpClient();
		get = new HttpGet(url);
		get.setHeader("Accept", "application/json");
		get.setHeader("User-Agent", String.format("Android Widget/%s", VERSION));
	}
	
	@Override
	public void run() {
		try {
			client.execute(get, this);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public Boolean handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		HttpEntity body;
		String line, content = "";

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && (body = response.getEntity()) != null) {
			BufferedReader bir = new BufferedReader(new InputStreamReader(body.getContent()));
			while((line = bir.readLine()) != null) {
				content += line;
			}
			bir.close();

 			Gson g = new Gson();
			SpaceStatus ss = g.fromJson(content, SpaceStatus.class);
			if(!ss.space.equals("")) {
				apiCallback.processNewStatus(ss);
				return true;
			}
		}
		return false;
	}
	
	public interface APICallback {
		public void processNewStatus(SpaceStatus fresh);
	}

	public static class SpaceStatus {
		public String api;
		public String space;
		public String logo;
		public SpaceIcons icon;
		public String url;
		public String address;
		public SpaceContact contact;
		public float lat;
		public float lon;
		public String[] cam;
		public HashMap<String,String> stream;
		public boolean open;
		public String status;
		public long lastchange;
		public SpaceEvent[] events;
		public Map<String, Map<String,String>>[] sensors;
		public SpaceFeed[] feeds;
		
		public SpaceStatus() {}
	}
	public static class SpaceIcons {
		public String open;
		public String closed;
		
		public SpaceIcons() {}
	}
	public static class SpaceContact {
		public String phone;
		public String sip;
		public String[] keymaster;
		public String irc;
		public String twitter;
		public String email;
		public String ml;
		public String jabber;
		
		public SpaceContact() {}
	}
	public static class SpaceEvent {
		public String name;
		public String type;
		public long t;
		public String extra;
		
		public SpaceEvent() {}
	}
	public static class SpaceFeed {
		public String name;
		public String type;
		public String url;
		
		public SpaceFeed() {}
	}
}
