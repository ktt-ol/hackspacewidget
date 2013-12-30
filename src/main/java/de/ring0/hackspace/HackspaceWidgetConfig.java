package de.ring0.hackspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.ring0.hackspace.UpdateWidgetTask.RefreshStatus;
import de.ring0.hackspace.UpdateWidgetTask.TaskParameters;

public class HackspaceWidgetConfig extends PreferenceActivity {
	private final static String VERSION = "0.1";
	private final static String DIRECTORY = "http://spaceapi.net/directory.json";
    private final static Type jsonHashMap = new TypeToken<Map<String, String>>(){}.getType();

    protected SharedPreferences sp;
	protected RefreshStatus rs = null;
	protected Gson g;

	private ListPreference lp;
	private CheckBoxPreference cp;
	private EditTextPreference ep;
	
	int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		setResult(RESULT_CANCELED);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        	g = new Gson();
        	rs = g.fromJson(sp.getString("refreshStatus", ""), RefreshStatus.class);
        }

        
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        
		lp = (ListPreference) findPreference("predefined_hackspace");
		ep = (EditTextPreference) findPreference("custom_url");
		cp = (CheckBoxPreference) findPreference("custom_hackspace");

        new LoadLatestDirectory().execute(DIRECTORY);

		ep.setEnabled(cp.isChecked());

		lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
		        new UpdateWidgetTask().execute(new TaskParameters(getBaseContext(), appWidgetManager, widgetId)); 
				
	            Intent resultValue = new Intent();
	            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
	            setResult(RESULT_OK, resultValue);
	            rs.refreshWidget.put(widgetId, true);
	            sp.edit().putString("refreshStatus", g.toJson(rs)).commit();
	            finish();
				return true;
			}
		});
		
		cp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
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
				
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
		        new UpdateWidgetTask().execute(new TaskParameters(getBaseContext(), appWidgetManager, widgetId));
				
	            Intent resultValue = new Intent();
	            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
	            setResult(RESULT_OK, resultValue);
	            rs.refreshWidget.put(widgetId, true);
	            sp.edit().putString("refreshStatus", g.toJson(rs)).commit();
	            finish();
				return true;
			}
		});
	}

	private class LoadLatestDirectory extends AsyncTask<String, Void, Map<String,String>> {
		protected Map<String,String> doInBackground(String... arg0) {
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
                    Map<String,String> keyValues = g.fromJson(content, jsonHashMap);
					return keyValues;
						
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
            }
			return null;
		}
		
		protected void onPostExecute(Map<String, String> result) {
			if(result != null) {
				lp.setSummary(R.string.predefined_hackspace_summary_done);
				lp.setEntries(result.keySet().toArray(new CharSequence[result.keySet().size()]));
				lp.setEntryValues(result.values().toArray(new CharSequence[result.keySet().size()]));
				lp.setEnabled(true);
			}
		}
		
	}
}