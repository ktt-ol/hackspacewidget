package de.ring0.hackspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import de.ring0.hackspace.datatypes.Space;

public class HackspaceStatusAPI {
//	private final static String TAG = HackerSpaceStatusAPI.class.getSimpleName();
	private final static String VERSION = "0.1";
	
	private HttpClient client;
	private HttpGet get;
	
	public HackspaceStatusAPI(String url) {
		client = new DefaultHttpClient();
		get = new HttpGet(url);
		get.setHeader("Accept", "application/json");
		get.setHeader("User-Agent", String.format("Android Widget/%s", VERSION));
	}
	
	public Space run() throws ClientProtocolException, IOException {
		HttpEntity body;
		String line, content = "";
		Space ss = null;
		
		HttpResponse response = client.execute(get);

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && (body = response.getEntity()) != null) {
			BufferedReader bir = new BufferedReader(new InputStreamReader(body.getContent()));
			while((line = bir.readLine()) != null) {
				content += line;
			}
			bir.close();

 			Gson g = new Gson();
			ss = g.fromJson(content, Space.class);
			if(!ss.api.equals("0.12")) {
				return ss;
			}
		}
		return ss;	
	}
}
