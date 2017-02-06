package org.nowxd.popularmovies.custom;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final String TAG = MovieAdapter.class.getSimpleName();

    private Movie[] movieData;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);

        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setPoster(movieData[position].getPosterImageUrl());
    }

    @Override
    public int getItemCount() {
        if (movieData == null) return 0;
        return movieData.length;
    }

    public void setMovieData(Movie[] movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView posterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }

        void setPoster(String posterUrl) {

            Picasso.with(context)
                    .load(posterUrl)
                    .fit()
                    .into(posterImageView);

        }

    }

}
