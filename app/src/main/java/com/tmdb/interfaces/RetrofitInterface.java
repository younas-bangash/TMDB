package com.tmdb.interfaces;

import com.tmdb.models.Movies;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YounasBangash on 9/14/2017.
 */

public interface RetrofitInterface {
    @GET("3/movie/now_playing?")
    Observable<Movies> getNowPlayingMovies(@Query("api_key") String api_key);

    // https://api.themoviedb.org/3/movie/118340?api_key=f7caf4a40a5accddacdad05cb1cdb792

}
