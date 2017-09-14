package com.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YounasBangash on 9/14/2017.
 */

public class Movies {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("page")
    @Expose
    public String page;
    @SerializedName("results")
    @Expose
    public List<MovieDetails> results = null;
    @SerializedName("total_pages")
    @Expose
    public String totalPages;
    @SerializedName("total_results")
    @Expose
    public String totalResults;
}
