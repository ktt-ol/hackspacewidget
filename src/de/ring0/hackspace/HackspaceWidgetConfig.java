package de.ring0.hackspace;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import de.ring0.hackspace.HackerSpaceStatusAPI.APICallback;
import de.ring0.hackspace.HackerSpaceStatusAPI.SpaceStatus;

public class HackspaceWidgetConfig extends Activity implements APICallback {
	HackerSpaceStatusAPI http;
	SpaceStatus status = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        StrictMode.setThreadPolicy(ThreadPolicy.LAX);
        
        http = new HackerSpaceStatusAPI("http://status.kreativitaet-trifft-technik.de/status.json", this);
        http.run();
    }

	@Override
	public void processNewStatus(SpaceStatus fresh) {
		status = fresh;
	}
}