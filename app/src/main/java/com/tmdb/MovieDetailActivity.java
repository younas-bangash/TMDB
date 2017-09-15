package com.tmdb;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tmdb.databinding.ActivityMovieDetailBinding;
import com.tmdb.databinding.ContentMovieDetailBinding;
import com.tmdb.fragments.RViewAdapter;
import com.tmdb.interfaces.RetrofitInterface;
import com.tmdb.models.Movies;
import com.tmdb.models.MoviesCompleteDetails;
import com.tmdb.utils.CircleProgressDrawable;
import com.tmdb.utils.Logger;
import com.tmdb.utils.RetrofitBuilder;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {
    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private ContentMovieDetailBinding contentMovieDetailBinding;
    private String posterUrl = "http://image.tmdb.org/t/p/w500";
    private static final String API_KEY = "f7caf4a40a5accddacdad05cb1cdb792";
    private String movieID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding =  DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        contentMovieDetailBinding = activityMovieDetailBinding.contentMovieDetail;
        setSupportActionBar(activityMovieDetailBinding.toolbar);
        Bundle data = getIntent().getExtras();
        if(data != null){
            loadMoviePoster(posterUrl+data.getString("posterLink"));
            getSupportActionBar().setTitle(data.getString("title"));
            movieID = data.getString("id");
        }

        getMovieFullDetails(movieID);

    }

    private void getMovieFullDetails(String movieID) {
        RetrofitBuilder retrofit = new RetrofitBuilder();
        final RetrofitInterface service = retrofit.mApi;
        service.getMovieFullDetails(movieID,API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoviesCompleteDetails>() {
                    @Override
                    public void onCompleted() {
                        contentMovieDetailBinding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        contentMovieDetailBinding.progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MoviesCompleteDetails moviesCompleteDetails) {
                        contentMovieDetailBinding.movieOverview.setText(moviesCompleteDetails.overview);
                        contentMovieDetailBinding.movieTitle.setText(moviesCompleteDetails.originalTitle);
                        contentMovieDetailBinding.movieRating.setText(moviesCompleteDetails.voteAverage);
                        contentMovieDetailBinding.movieStatus.setText(moviesCompleteDetails.status);

                    }
                });

    }

    private void loadMoviePoster(String posterUrl){
        activityMovieDetailBinding.posterImageView.getHierarchy().setProgressBarImage(
                new CircleProgressDrawable());
        activityMovieDetailBinding.posterImageView.setImageURI(posterUrl);
    }
}
