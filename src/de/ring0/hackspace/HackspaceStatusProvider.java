package de.ring0.hackspace;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import de.ring0.hackspace.HackerSpaceStatusAPI.APICallback;
import de.ring0.hackspace.HackerSpaceStatusAPI.SpaceStatus;

public class HackspaceStatusProvider extends AppWidgetProvider {

	public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i<appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
	}
	public static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String url;
		
		if(sp.getBoolean("custom_hackspace", false))
			url = sp.getString("custom_url", "http://localhost");
		else
			url = sp.getString("predefined_hackspace", "http://localhost");
		
		new HackerSpaceStatusAPI(url, new APICallback() {
			public void processNewStatus(SpaceStatus fresh) {
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
				views.setImageViewResource(R.id.imageView1, android.R.drawable.ic_media_pause);
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}).run();
        
	}
}
