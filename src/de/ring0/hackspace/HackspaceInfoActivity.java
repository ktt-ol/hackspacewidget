package de.ring0.hackspace;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.QuickContactBadge;

import com.google.gson.Gson;

import de.ring0.hackspace.HackspaceStatusAPI.SpaceStatus;

public class HackspaceInfoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Gson g = new Gson();
		SpaceStatus ss = g.fromJson(sp.getString("status", ""), SpaceStatus.class);
		QuickContactBadge qcb = (QuickContactBadge)findViewById(R.id.quickContactBadge1);
		
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
