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

    public synchronized static void scheduleMovieSyncJob(Context context) {

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job syncMoviesJob = dispatcher.newJobBuilder()
                .setService(MovieFireBaseJobService.class)
                .setTag(JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(10, 60))
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        dispatcher.mustSchedule(syncMoviesJob);
        dispatcher.schedule(syncMoviesJob);

    }

}
