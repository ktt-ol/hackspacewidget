package de.ring0.hackspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class HackerSpaceStatusAPI implements ResponseHandler<String> {
	private final static String TAG = HackerSpaceStatusAPI.class.getSimpleName();
	private final static String VERSION = "0.1";
	
	private APICallback apiCallback;
	
	public HackerSpaceStatusAPI(String url, APICallback callback) throws ClientProtocolException, IOException {
		apiCallback = callback;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept", "application/json");
		get.setHeader("User-Agent", String.format("Android Widget/%s", VERSION));
		client.execute(get, this);
	}

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		HttpEntity body;
		JSONObject job;
		String line = null, content = null;
		
		if((body = response.getEntity()) != null) {
			BufferedReader bir = new BufferedReader(new InputStreamReader(body.getContent()));
			while((line = bir.readLine()) != null) {
				content += line;
			}
			bir.close();
			
			try {
				job = new JSONObject(content);
				HSSAPIHandler handler = null;
				if(job.has("api")) {
					if(job.getString("api").equals("0.12")) {
						handler = new v012Callback();
					}
					
					if(handler != null)
						apiCallback.processNewStatus(handler.processJSON(job));
				}
			}
			catch(JSONException e) {
				throw new IOException(e);
			}
		}
		return null;
	}
	
	private class v012Callback implements HSSAPIHandler {
		@Override
		public SpaceStatus processJSON(JSONObject job) throws JSONException {
			SpaceStatus ss = new SpaceStatus();
			JSONObject tmp = null;
			
			ss.api = job.getString("api");
			ss.space = job.getString("space");
			ss.logo = job.getString("logo");
			ss.icon = new String[] {
				job.getJSONObject("icon").getString("open"),
				job.getJSONObject("icon").getString("closed")
			};
			ss.url = job.getString("url");
			ss.address = job.optString("address");
			if(job.has("contact")) {
				ss.contact = new SpaceContact();
				tmp = job.getJSONObject("contact");
				ss.contact.phone = tmp.optString("phone");
				ss.contact.sip = tmp.optString("sip");
				if(tmp.has("keymaster")) {
					String s = tmp.optString("keymaster");
					if(s != "") {
						
					}
				}
				ss.contact.irc = tmp.optString("irc");
				ss.contact.twitter = tmp.optString("twitter");
				ss.contact.email = tmp.optString("email");
				ss.contact.ml = tmp.optString("ml");
				ss.contact.jabber = tmp.optString("jabber");
			}
			
			return ss;
		}
		
	}
	
	private interface HSSAPIHandler {
		public SpaceStatus processJSON(JSONObject job) throws JSONException;
	}
	
	public interface APICallback {
		public void processNewStatus(SpaceStatus fresh);
	}
	public interface SpaceSensor {}
	
	public class SpaceStatus {
		public String api;
		public String space;
		public String logo;
		public String[] icon;
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
		public SpaceSensor sensors;
		public SpaceFeed feeds;
	}
	public class SpaceContact {
		public String phone;
		public String sip;
		public String[] keymaster;
		public String irc;
		public String twitter;
		public String email;
		public String ml;
		public String jabber;
	}
	public class SpaceEvent {
		public String name;
		public SpaceEventType type;
		public long t;
		public String extra;
	}
	public class SpaceFeed {
		public String name;
		public String type;
		public String url;
	}
	public enum SpaceEventType {
		CHECK_IN, CHECK_OUT
	}
	
}
