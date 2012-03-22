package de.ring0.hackspace;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import de.ring0.hackspace.HackspaceStatusAPI.SpaceStatus;
import de.ring0.hackspace.UpdateWidgetTask.TaskParameters;

public class UpdateWidgetTask extends AsyncTask<TaskParameters, Void, SpaceStatus> {
	private static final String TAG = UpdateWidgetTask.class.getSimpleName();
	private Bitmap openIcon, closedIcon;
	private TaskParameters tp;

	@Override
	protected SpaceStatus doInBackground(TaskParameters... tps) {
		tp = tps[0];
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(tp.context);
		String url;
		
		Gson g = new Gson();
    	RefreshStatus rs = null;
    	
    	/* stop the widget from rendering before the configuration is completed */
    	if(sp.contains("refreshStatus")) {
    		rs = g.fromJson(sp.getString("refreshStatus", ""), RefreshStatus.class);
        	if(!rs.refreshWidget.containsKey(tp.appWidgetId)) {
        		rs.refreshWidget.put(tp.appWidgetId, false);
                sp.edit().putString("refreshStatus", g.toJson(rs)).commit();
        		return null;
        	}
       	}
    	else {
    		rs = new RefreshStatus();
            sp.edit().putString("refreshStatus", g.toJson(rs)).commit();
            return null;
    	}        
		
		if(sp.getBoolean("custom_hackspace", false))
			url = sp.getString("custom_url", "http://localhost");
		else
			url = sp.getString("predefined_hackspace", "http://localhost");
		
		try {
			HackspaceStatusAPI hss = new HackspaceStatusAPI(url);
			SpaceStatus ss = hss.run();
			
			URL open = new URL(ss.icon.open);
			URL closed = new URL(ss.icon.closed);
			URLConnection urlOpen = open.openConnection();
			URLConnection urlClosed = closed.openConnection();
			urlOpen.setUseCaches(true);
			urlClosed.setUseCaches(true);
			openIcon = BitmapFactory.decodeStream((InputStream) urlOpen.getContent());
			closedIcon = BitmapFactory.decodeStream((InputStream) urlClosed.getContent());

			return ss;
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return null;
	}
	@Override
	protected void onPostExecute (SpaceStatus result) {
		if(result != null) {
			RemoteViews views = new RemoteViews(tp.context.getPackageName(), R.layout.widget_layout);
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(tp.context);
			Gson g = new Gson();

			if(result.open)
				views.setImageViewBitmap(R.id.imageView1, openIcon);
			else
				views.setImageViewBitmap(R.id.imageView1, closedIcon);

        	Intent intent = new Intent(tp.context, HackspaceInfoActivity.class);
        	PendingIntent pendingIntent = PendingIntent.getActivity(tp.context, 0, intent, 0);
        	views.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        	
        	
			tp.appWidgetManager.updateAppWidget(tp.appWidgetId, views);
			
			sp.edit().putString("status", g.toJson(result)).commit();
		}
	}
	public static class TaskParameters {
		public Context context;
		public AppWidgetManager appWidgetManager;
		public int appWidgetId;
		public TaskParameters(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
			this.context = context;
			this.appWidgetManager = appWidgetManager;
			this.appWidgetId = appWidgetId;
		}
	}
	
	public static class RefreshStatus {
		public HashMap<Integer,Boolean> refreshWidget = new HashMap<Integer,Boolean>();
	}
}
