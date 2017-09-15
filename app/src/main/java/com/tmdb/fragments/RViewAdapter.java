package com.tmdb.fragments;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tmdb.R;
import com.tmdb.databinding.FragmentItemBinding;
import com.tmdb.interfaces.OnMovieClickListner;
import com.tmdb.models.MovieDetails;
import com.tmdb.utils.CircleProgressDrawable;
import com.tmdb.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.tmdb.utils.Constant.POSTER_URL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MovieDetails} and makes a call to the
 * specified {@link OnMovieClickListner}.
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

    private void loadMoviePoster(String posterUrl, SimpleDraweeView simpleDraweeView) {
        simpleDraweeView.getHierarchy().setProgressBarImage(new CircleProgressDrawable());
        simpleDraweeView.setImageURI(posterUrl);
    }

    public static void loadImageCircle(String url, SimpleDraweeView targetView) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(false)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(false)
                .build();

        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        targetView.getHierarchy().setRoundingParams(roundingParams);

        targetView.setController(controller);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = movieDetails.get(position);
        fragmentItemBinding = holder.getViewDataBinding();
        loadMoviePoster(POSTER_URL + movieDetails.get(position).posterPath,holder.simpleDraweeView);
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
