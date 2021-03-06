package org.nowxd.popularmovies.custom;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.database.MovieContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final String TAG = MovieAdapter.class.getSimpleName();

    private Cursor movieCursor;

    private Context context;
    private MoviePosterOnClickListener onClickListener;

    public MovieAdapter(Context context, MoviePosterOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);

        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        movieCursor.moveToPosition(position);

        int posterIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        String posterImageUrl = movieCursor.getString(posterIndex);

        holder.setPoster(posterImageUrl);

    }

    @Override
    public int getItemCount() {
        if (movieCursor == null) return 0;
        return movieCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        movieCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * OnClickListener
     */
    public interface MoviePosterOnClickListener {
        void onMoviePosterClick(long movieID);
    }

    /**
     * ViewHolder
    */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_movie_poster) ImageView posterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            posterImageView.setOnClickListener(this);
        }

        void setPoster(String posterUrl) {

            Picasso.with(context)
                    .load(posterUrl)
//                    .placeholder(R.drawable.placeholder_image)
                    .fit()
                    .into(posterImageView);

        }

        @Override
        public void onClick(View view) {

            movieCursor.moveToPosition(getAdapterPosition());

            // Retrieve the ID
            int idIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            long movieID = movieCursor.getLong(idIndex);

            onClickListener.onMoviePosterClick(movieID);

        }
    }
}
