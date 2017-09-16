package com.tmdb.fragments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tmdb.R;
import com.tmdb.databinding.FragmentItemBinding;
import com.tmdb.interfaces.OnMovieClickListner;
import com.tmdb.models.MovieDetails;
import com.tmdb.utils.CircleProgressDrawable;

import java.util.ArrayList;
import java.util.List;

import static com.tmdb.utils.Constant.POSTER_URL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MovieDetails} and makes a call to the
 * specified {@link OnMovieClickListner}.
 */
public class RViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TOP_POSITION_VIEW_HOLDER = 1;
    private List<MovieDetails> movieDetails = new ArrayList<>();
    private final OnMovieClickListner mListener;
    private FragmentItemBinding fragmentItemBinding;

    public RViewAdapter(List<MovieDetails> items, OnMovieClickListner listener) {
        movieDetails = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        fragmentItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_item, parent, false);
        return new ViewHolder(fragmentItemBinding);

    }

    private void loadMoviePoster(String posterUrl, SimpleDraweeView simpleDraweeView) {
        simpleDraweeView.getHierarchy().setProgressBarImage(new CircleProgressDrawable());
        simpleDraweeView.setImageURI(posterUrl);
    }

    @Override
    public int getItemViewType(int position) {
        return TOP_POSITION_VIEW_HOLDER;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mItem = movieDetails.get(position);
        fragmentItemBinding = viewHolder.getViewDataBinding();
        loadMoviePoster(POSTER_URL + movieDetails.get(position).posterPath,viewHolder.simpleDraweeView);
        fragmentItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMovieClick(viewHolder.mItem);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return movieDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MovieDetails mItem;
        public SimpleDraweeView simpleDraweeView;
        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            fragmentItemBinding = binding;
            simpleDraweeView = fragmentItemBinding.posterImageView;
            binding.executePendingBindings();
        }
        public FragmentItemBinding getViewDataBinding() {

            return fragmentItemBinding;
        }
    }
}
