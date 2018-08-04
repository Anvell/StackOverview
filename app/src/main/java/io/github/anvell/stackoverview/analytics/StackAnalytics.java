package io.github.anvell.stackoverview.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.github.anvell.stackoverview.R;
import io.reactivex.annotations.NonNull;

public class StackAnalytics {

    private static Tracker tracker;
    private static Application app;

    public static synchronized void initialize(@NonNull Application application) {
        app = application;
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(application);
        tracker = analytics.newTracker(R.xml.global_tracker);
    }

    public static void onSearchResultOpen(String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(app.getString(R.string.analytics_category_search))
                .setAction(app.getString(R.string.analytics_action_open))
                .setLabel(label)
                .build());
    }

    public static void onCollectionOpen(String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(app.getString(R.string.analytics_category_collection))
                .setAction(app.getString(R.string.analytics_action_open))
                .setLabel(label)
                .build());
    }
}
