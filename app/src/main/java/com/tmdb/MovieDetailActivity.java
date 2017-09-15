package com.tmdb;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tmdb.databinding.ActivityMovieDetailBinding;
import com.tmdb.utils.CircleProgressDrawable;
import com.tmdb.utils.Logger;

public class MovieDetailActivity extends AppCompatActivity {
    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private String posterUrl = "http://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding =  DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        setSupportActionBar(activityMovieDetailBinding.toolbar);
        Bundle data = getIntent().getExtras();
        if(data != null){
            loadMoviePoster(posterUrl+data.getString("posterLink"));
            getSupportActionBar().setTitle(data.getString("title"));
        }

        getMovieFullDetails();

    }

    private void getMovieFullDetails() {

    }

    private void loadMoviePoster(String posterUrl){
        activityMovieDetailBinding.posterImageView.getHierarchy().setProgressBarImage(
                new CircleProgressDrawable());
        activityMovieDetailBinding.posterImageView.setImageURI(posterUrl);
    }
}
