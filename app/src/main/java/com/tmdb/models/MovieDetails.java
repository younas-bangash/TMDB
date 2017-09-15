package com.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YounasBangash on 9/14/2017.
 */

public class MovieDetails {
    @SerializedName("original_title")
    @Expose
    public String original_title;
    @SerializedName("vote_count")
    @Expose
    public String vote_count;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("video")
    @Expose
    public String video;
    @SerializedName("vote_average")
    @Expose
    public String voteAverage;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("popularity")
    @Expose
    public String popularity;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("genre_ids")
    @Expose
    public List<String> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    public String backdrop_path;
    @SerializedName("adult")
    @Expose
    public String adult;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;




}
