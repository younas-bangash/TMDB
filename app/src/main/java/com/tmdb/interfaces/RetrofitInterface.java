package com.tmdb.interfaces;

import com.tmdb.models.Movies;
import com.tmdb.models.MoviesCompleteDetails;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YounasBangash on 9/14/2017.
 */

public interface RetrofitInterface {
    @GET("3/movie/now_playing?")
    Observable<Movies> getNowPlayingMovies(@Query("api_key") String api_key);

    @GET("3/movie/popular?")
    Observable<Movies> getPopluarMovies(@Query("api_key") String api_key);

    @GET("3/movie/{movieID}?")
    Observable<MoviesCompleteDetails> getMovieFullDetails(
            @Path("movieID") String movieID, @Query("api_key") String api_key);

}
