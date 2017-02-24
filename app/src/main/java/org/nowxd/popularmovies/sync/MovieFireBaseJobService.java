package org.nowxd.popularmovies.sync;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.ArrayList;

public class MovieFireBaseJobService extends JobService {

    private static final String TAG = MovieFireBaseJobService.class.getSimpleName();
    
    private ArrayList<MovieTask> movieTasks;
    private static final String[] sortTypes = {"popular", "top_rated"};

    @Override
    public boolean onStartJob(JobParameters job) {

        Log.d(TAG, "onStartJob: STARTING JOBS");

        movieTasks = new ArrayList<>();

        for (String sortType : sortTypes) {

            MovieTask currentTask = new MovieTask(getApplicationContext());

            movieTasks.add(currentTask);

            currentTask.execute(sortType);

        }

        return true;

    }

    @Override
    public boolean onStopJob(JobParameters job) {

        if (movieTasks != null) {

            for (MovieTask movieTask : movieTasks) {

                if (movieTask != null) {
                    movieTask.cancel(true);
                }

            }

            movieTasks.clear();

        }

        return true;

    }
}
