package org.nowxd.popularmovies.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nowxd.popularmovies.R;
import org.nowxd.popularmovies.model.Review;
import org.w3c.dom.Text;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Review[] reviews;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bindView(reviews[position]);
    }

    @Override
    public int getItemCount() {
        if (reviews == null) return 0;
        return reviews.length;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuthorTextView;
        TextView reviewContentTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            reviewContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        void bindView(Review review) {
            reviewAuthorTextView.setText(review.getAuthor());
            reviewContentTextView.setText(review.getContent());
        }
    }

}
