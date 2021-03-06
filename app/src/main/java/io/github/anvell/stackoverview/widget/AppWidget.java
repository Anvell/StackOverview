package io.github.anvell.stackoverview.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.view.MainActivity;

public class AppWidget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "AppWidget.EXTRA_ITEM";
    public static final String EXTRA_OPEN_ACTION = "AppWidget.OPEN_ACTION";
    public static final String EXTRA_CLICK = "AppWidget.CLICK";

    protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        rv.setRemoteAdapter(R.id.widgetList, intent);
        rv.setEmptyView(R.id.widgetList, R.id.widgetRefresh);
        rv.setOnClickPendingIntent(R.id.widgetRefresh, createSelfIntent(context, EXTRA_CLICK));

        Intent openActivityIntent = new Intent(context, AppWidget.class);
        openActivityIntent.setAction(EXTRA_OPEN_ACTION);
        openActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent openActivityPendingIntent = PendingIntent.getBroadcast(context, 0, openActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.widgetList, openActivityPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() != null) {
            if (intent.getAction().equals(EXTRA_OPEN_ACTION)) {
                int id = intent.getIntExtra(EXTRA_ITEM, 0);

                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.putExtra(MainActivity.INTENT_OPEN_QUESTION, id);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            }

            if (intent.getAction().equals(EXTRA_CLICK)) {
                notifyDataChanged(context);
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int id : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onEnabled(Context context) {
        StackWidgetJob.scheduleDefault(context);
    }

    @Override
    public void onDisabled(Context context) {
        StackWidgetJob.cancel(context);
    }

    protected PendingIntent createSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static void notifyDataChanged(Context context) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context, AppWidget.class);
        int[] widgetIds = widgetManager.getAppWidgetIds(component);
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widgetList);
    }
}

