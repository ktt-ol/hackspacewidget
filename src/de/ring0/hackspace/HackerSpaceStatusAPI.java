package de.ring0.hackspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class HackerSpaceStatusAPI {
//	private final static String TAG = HackerSpaceStatusAPI.class.getSimpleName();
	private final static String VERSION = "0.1";
	
	private HttpClient client;
	private HttpGet get;
	
	public HackerSpaceStatusAPI(String url) {
		client = new DefaultHttpClient();
		get = new HttpGet(url);
		get.setHeader("Accept", "application/json");
		get.setHeader("User-Agent", String.format("Android Widget/%s", VERSION));
	}
	
	public SpaceStatus run() throws ClientProtocolException, IOException {
		HttpEntity body;
		String line, content = "";
		
		HttpResponse response = client.execute(get);

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && (body = response.getEntity()) != null) {
			BufferedReader bir = new BufferedReader(new InputStreamReader(body.getContent()));
			while((line = bir.readLine()) != null) {
				content += line;
			}
			bir.close();

 			Gson g = new Gson();
			SpaceStatus ss = g.fromJson(content, SpaceStatus.class);
			if(!ss.space.equals("")) {
				return ss;
			}
		}
		return null;	
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
		public SpaceSensors sensors;
		public SpaceFeed[] feeds;
		
		public SpaceStatus() {}
	}
	
	/* Sensors */
	public static class SpaceSensors {
		public TemperatureSensor[] temperature;
		public HumiditySensor[] humidity;
		public Barometer[] barometer;
		public WindSensor[] wind;
		public WifiConnection[] wifi;
	}
	public static class TemperatureSensor {
		public String name;
		public Float value;
		public String unit;
	}
	public static class HumiditySensor {
		public String name;
		public Float value;
	}
	public static class Barometer {
		public String name;
		public Float value;
		public String unit;
	}
	public static class WindSensor {
		public String name;
		public Float speed;
		public Float gust;
		public String unit;
	}
	public static class WifiConnection {
		public String name;
		public Integer connections;
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
