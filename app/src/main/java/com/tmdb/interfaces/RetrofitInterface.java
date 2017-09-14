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
    @GET("3/genre/878/movies?")
    Observable<Movies> getAllMovies(@Query("api_key") String api_key,
                                    @Query("language") String language,
                                    @Query("include_adult") String include_adult,
                                    @Query("sort_by") String sort_by);

}
