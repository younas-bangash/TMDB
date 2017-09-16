package com.tmdb;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.tmdb.databinding.ActivityMovieDetailBinding;
import com.tmdb.databinding.ContentMovieDetailBinding;
import com.tmdb.interfaces.RetrofitInterface;
import com.tmdb.models.MoviesCompleteDetails;
import com.tmdb.utils.CircleProgressDrawable;
import com.tmdb.utils.RetrofitBuilder;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tmdb.utils.Constant.API_KEY;
import static com.tmdb.utils.Constant.BANNER_URL;

public class MovieDetailActivity extends AppCompatActivity {
    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private ContentMovieDetailBinding contentMovieDetailBinding;
    private String movieID = null;
    private RetrofitBuilder retrofit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding =  DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        contentMovieDetailBinding = activityMovieDetailBinding.contentMovieDetail;
        setSupportActionBar(activityMovieDetailBinding.toolbar);
        retrofit = new RetrofitBuilder(getApplicationContext());
        //noinspection ConstantConditions
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle data = getIntent().getExtras();
        if(data != null){
            loadMoviePoster(BANNER_URL+data.getString("posterLink"));
            getSupportActionBar().setTitle(data.getString("title"));
            movieID = data.getString("id");
        }

        getMovieFullDetails(movieID);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    private void getMovieFullDetails(String movieID) {

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
