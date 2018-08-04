package io.github.anvell.stackoverview.base;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.analytics.StackAnalytics;
import io.github.anvell.stackoverview.repository.StackOverflowRepository;

public class StackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StackOverflowRepository.initialize(this);
        StackAnalytics.initialize(this);
        MobileAds.initialize(this, getString(R.string.ads_id));
    }
}