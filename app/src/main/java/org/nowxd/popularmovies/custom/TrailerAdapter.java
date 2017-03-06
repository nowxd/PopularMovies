package org.nowxd.popularmovies.custom;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.model.Trailer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Trailer[] trailers;

    private TrailerOnClickListener trailerOnClickListener;

    public TrailerAdapter(TrailerOnClickListener trailerOnClickListener) {
        this.trailerOnClickListener = trailerOnClickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bindTrailerData(trailers[position]);
    }

    @Override
    public int getItemCount() {
        if (trailers == null) return 0;
        return trailers.length;
    }

    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public interface TrailerOnClickListener {
        void onTrailerClick(Trailer trailer);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trailer_name) TextView trailerNameTextView;

        TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindTrailerData(Trailer trailer) {
            trailerNameTextView.setText(trailer.getName());
        }

        @Override
        public void onClick(View view) {
            trailerOnClickListener.onTrailerClick(trailers[getAdapterPosition()]);
        }
    }

}

