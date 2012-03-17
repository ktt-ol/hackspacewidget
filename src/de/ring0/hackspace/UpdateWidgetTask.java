package de.ring0.hackspace;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
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

			if(result.open)
				views.setImageViewBitmap(R.id.imageView1, openIcon);
			else
				views.setImageViewBitmap(R.id.imageView1, closedIcon);

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
