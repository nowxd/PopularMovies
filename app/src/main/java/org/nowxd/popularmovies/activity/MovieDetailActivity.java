package org.nowxd.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.data.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView movieTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);

        Movie movie = getIntent().getParcelableExtra(getString(R.string.parcelable_movie_key));

        setUpViews(movie);

    }

    private void setUpViews(Movie movie) {

        movieTitleTextView.setText(movie.getTitle());

    }

}
