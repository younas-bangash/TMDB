package com.tmdb.fragments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmdb.R;
import com.tmdb.databinding.FragmentItemBinding;
import com.tmdb.interfaces.OnMovieClickListner;
import com.tmdb.models.MovieDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MovieDetails} and makes a call to the
 * specified {@link OnMovieClickListner}.
 *
 */
public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.ViewHolder> {

    private List<MovieDetails> movieDetails = new ArrayList<>();
    private final OnMovieClickListner mListener;
    private FragmentItemBinding fragmentItemBinding;

    public RViewAdapter(List<MovieDetails> items, OnMovieClickListner listener) {
        movieDetails = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        fragmentItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.fragment_item, parent, false);
        return new ViewHolder(fragmentItemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = movieDetails.get(position);
        fragmentItemBinding = holder.getViewDataBinding();

        fragmentItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMovieClick(holder.mItem);
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
        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            fragmentItemBinding = binding;
            binding.executePendingBindings();
        }

        public FragmentItemBinding getViewDataBinding() {
            return fragmentItemBinding;
        }
    }
}
