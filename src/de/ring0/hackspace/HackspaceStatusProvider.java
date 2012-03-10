package de.ring0.hackspace;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import de.ring0.hackspace.UpdateWidgetTask.TaskParameters;

public class HackspaceStatusProvider extends AppWidgetProvider {

	public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i<appWidgetIds.length; i++) {
        	new UpdateWidgetTask().execute(new TaskParameters(context, appWidgetManager, appWidgetIds[i]));
        }
	}
}
