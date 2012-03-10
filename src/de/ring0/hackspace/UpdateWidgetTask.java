package de.ring0.hackspace;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import de.ring0.hackspace.HackerSpaceStatusAPI.SpaceStatus;
import de.ring0.hackspace.UpdateWidgetTask.TaskParameters;

public class UpdateWidgetTask extends AsyncTask<TaskParameters, Void, SpaceStatus> {
	private TaskParameters tp;

	@Override
	protected SpaceStatus doInBackground(TaskParameters... tps) {
		tp = tps[0];
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(tp.context);
		String url;
		
		if(sp.getBoolean("custom_hackspace", false))
			url = sp.getString("custom_url", "http://localhost");
		else
			url = sp.getString("predefined_hackspace", "http://localhost");
		
		try {
			HackerSpaceStatusAPI hss = new HackerSpaceStatusAPI(url);
			SpaceStatus ss = hss.run();
			return ss;
		} catch (ClientProtocolException e) {
			Log.e("UpdateWidgetTask", e.getMessage());
		} catch (IOException e) {
			Log.e("UpdateWidgetTask", e.getMessage());
		}
		return null;
	}
	@Override
	protected void onPostExecute (SpaceStatus result) {
		if(result != null) {
			RemoteViews views = new RemoteViews(tp.context.getPackageName(), R.layout.widget_layout);
			int resId;
			if(result.open)
				resId = android.R.drawable.btn_star_big_on;
			else
				resId = android.R.drawable.btn_star_big_off;
			
			views.setImageViewResource(R.id.imageView1, resId);
			tp.appWidgetManager.updateAppWidget(tp.appWidgetId, views);
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

}
