package de.ring0.hackspace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.gson.Gson;

import de.ring0.hackspace.datatypes.Space;

public class HackspaceViewActivity extends SherlockFragmentActivity {

	Space ss;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Gson g = new Gson();
		ss = g.fromJson(sp.getString("status", ""), Space.class);
		
		setResult(RESULT_OK);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume () {
		super.onResume();
	}
}
