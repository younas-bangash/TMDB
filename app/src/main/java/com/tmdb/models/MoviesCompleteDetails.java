package com.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YounasBangash on 9/15/2017.
 */

public class MoviesCompleteDetails {
    @SerializedName("adult")
    @Expose
    public String adult;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;

    /*@SerializedName("belongs_to_collection")
    @Expose
    public BelongsToCollection belongsToCollection;*/

    @SerializedName("budget")
    @Expose
    public String budget;
    @SerializedName("genres")
    @Expose
    public List<Genre> genres = null;
    @SerializedName("homepage")
    @Expose
    public String homepage;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("imdb_id")
    @Expose
    public String imdbId;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("popularity")
    @Expose
    public String popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;

   /* @SerializedName("production_companies")
    @Expose
    public List<ProductionCompany> productionCompanies = null;
    @SerializedName("production_countries")
    @Expose
    public List<ProductionCountry> productionCountries = null;*/

    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("revenue")
    @Expose
    public String revenue;
    @SerializedName("runtime")
    @Expose
    public String runtime;

   /* @SerializedName("spoken_languages")
    @Expose
    public List<SpokenLanguage> spokenLanguages = null;*/

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("tagline")
    @Expose
    public String tagline;
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
