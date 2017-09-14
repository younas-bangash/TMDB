package com.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YounasBangash on 9/14/2017.
 */

public class MovieDetails {
    @SerializedName("adult")
    @Expose
    public String adult;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("genre_ids")
    @Expose
    public List<String> genreIds = null;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("popularity")
    @Expose
    public String popularity;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("video")
    @Expose
    public String video;
    @SerializedName("vote_average")
    @Expose
    public String voteAverage;
    @SerializedName("vote_count")
    @Expose
    public String voteCount;

}
