package org.nowxd.popularmovies.utils;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.nowxd.popularmovies.sync.MovieFireBaseJobService;

public class SyncUtils {

    private static final String TAG = SyncUtils.class.getSimpleName();

    private static final String JOB_TAG = "movies_api_call";

    private static final int START_WINDOW = 0;
    private static final int DELTA_WINDOW = 50;
    private static final int END_WINDOW = START_WINDOW + DELTA_WINDOW;

    public synchronized static void scheduleMovieSyncJob(Context context) {

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job syncMoviesJob = dispatcher.newJobBuilder()
                .setService(MovieFireBaseJobService.class)
                .setTag(JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(START_WINDOW, END_WINDOW))
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        dispatcher.mustSchedule(syncMoviesJob);
        dispatcher.schedule(syncMoviesJob);

    }

}
