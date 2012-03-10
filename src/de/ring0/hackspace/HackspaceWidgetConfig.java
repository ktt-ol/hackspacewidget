package de.ring0.hackspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.google.gson.Gson;

public class HackspaceWidgetConfig extends PreferenceActivity {
	private final static String VERSION = "0.1";
	//private final static String DIRECTORY = "http://chasmcity.sonologic.nl/spacestatusdirectory.php";
	private final static String DIRECTORY = "http://lhw.ring0.de/cgi-bin/spacedirectory.rb";

	private ListPreference lp;
	private SwitchPreference sp;
	private EditTextPreference ep;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		lp = (ListPreference) findPreference("predefined_hackspace");
		ep = (EditTextPreference) findPreference("custom_url");
		sp = (SwitchPreference) findPreference("custom_hackspace");

		new LoadLatestDirectory().execute(DIRECTORY);

		ep.setEnabled(sp.isChecked());

		sp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				ep.setEnabled((Boolean) newValue);
				return true;
			}
		});
		ep.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				try {
					@SuppressWarnings("unused")
					URL u = new URL((String) newValue);
				} catch (MalformedURLException e) {
					new AlertDialog.Builder(HackspaceWidgetConfig.this)
						.setMessage("Your specified URL is not a valid!")
						.setNeutralButton("Try Again", new OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.cancel();
							}
						}).create().show();
					return false;
				}
				return true;
			}
		});
	}
	
	private class LoadLatestDirectory extends AsyncTask<String, Void, String[][]> {
		protected String[][] doInBackground(String... arg0) {
			HttpEntity body;
			String line, content = "";
			HttpClient http = new DefaultHttpClient();
			HttpGet get = new HttpGet(arg0[0]);
			
			get.setHeader("Accept", "application/json");
			get.setHeader("User-Agent", String.format("Android Widget/%s", VERSION));
			
			try {
				HttpResponse response = http.execute(get);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && (body = response.getEntity()) != null) {
					BufferedReader bir = new BufferedReader(new InputStreamReader(body.getContent()));
					while((line = bir.readLine()) != null) {
						content += line;
					}
					bir.close();
					
					Gson g = new Gson();
					SpaceDirectory sd = g.fromJson(content, SpaceDirectory.class);
					String[][] keyValues = new String[2][sd.spaces.length];
					for(int i = 0; i < sd.spaces.length; i++) {
						keyValues[0][i] = sd.spaces[i].name;
						keyValues[1][i] = sd.spaces[i].url;
					}
					return keyValues;
						
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String[][] result) {
			if(result != null) {
				lp.setEntries(result[0]);
				lp.setEntryValues(result[1]);
				lp.setEnabled(true);
			}
		}
		
	}
	private class SpaceDirectory {
		public Space[] spaces;
	}
	private class Space {
		public String name;
		public String url;
	}
}