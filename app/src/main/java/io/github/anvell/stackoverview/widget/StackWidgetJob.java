package io.github.anvell.stackoverview.widget;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class StackWidgetJob extends JobService {

    private static final String JOB_ID = "StackWidgetJob.ID";

    public static void scheduleDefault(Context context) {

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job widgetJob = dispatcher.newJobBuilder()
                .setService(StackWidgetJob.class)
                .setTag(JOB_ID)
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(60, 600))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        dispatcher.mustSchedule(widgetJob);
    }

    public static void cancel(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        dispatcher.cancel(JOB_ID);
    }

    @Override
    public boolean onStartJob(JobParameters job) {

        AppWidget.notifyDataChanged(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}