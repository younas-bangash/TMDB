package com.tmdb.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tmdb.MainScreenActivity;
import com.tmdb.R;
import com.tmdb.databinding.FragmentItemListBinding;
import com.tmdb.interfaces.OnMovieClickListner;
import com.tmdb.interfaces.RetrofitInterface;
import com.tmdb.models.MovieDetails;
import com.tmdb.models.Movies;
import com.tmdb.utils.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tmdb.utils.Constant.API_KEY;
import static com.tmdb.utils.Constant.LANGUAGE;
import static com.tmdb.utils.Constant.NOW_PLAYING_QUERY;
import static com.tmdb.utils.Constant.PAGE_NUMBER;
import static com.tmdb.utils.Constant.POPULAR_MOVIE_QUERY;
import static com.tmdb.utils.Constant.TOP_RATED_MOVIES;
import static com.tmdb.utils.Constant.UP_COMING_MOVIES;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMovieClickListner}
 * interface.
 */
public class MovieFragment extends Fragment {
    private List<MovieDetails> movieDetailses = new ArrayList<>();
    private OnMovieClickListner mListener;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int movieQuery;
    private RetrofitBuilder retrofit;
    private GridLayoutManager gridLayoutManager;
    private FragmentItemListBinding fragmentItemListBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    @SuppressWarnings("unused")
    public static MovieFragment newInstance(int movieQuery) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt("movieQuery",movieQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new RetrofitBuilder(getActivity().getApplicationContext());
        movieDetailses.clear();
        if (getArguments() != null) {
            movieQuery = getArguments().getInt("movieQuery");
        }
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);

    }


    private void getNowPlayingMovies() {

        final RetrofitInterface service = retrofit.mApi;
        service.getNowPlayingMovies(API_KEY,LANGUAGE,PAGE_NUMBER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onCompleted() {
                        fragmentItemListBinding.list.setLayoutManager(gridLayoutManager);
                        fragmentItemListBinding.list.setAdapter(
                                new RViewAdapter(movieDetailses, mListener));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Movies movies) {
                        movieDetailses.addAll(movies.results);
                    }
                });

    }

    private void getTopRatedMovies(){

        final RetrofitInterface service = retrofit.mApi;
        service.getTopRatedMovies(API_KEY,LANGUAGE,PAGE_NUMBER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onCompleted() {
                        fragmentItemListBinding.list.setLayoutManager(
                                new GridLayoutManager(getActivity().getApplicationContext(), 2));
                        fragmentItemListBinding.list.setAdapter(
                                new RViewAdapter(movieDetailses, mListener));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Movies movies) {
                        movieDetailses.addAll(movies.results);
                    }
                });

    }

    private void getPopluarMovies() {

        final RetrofitInterface service = retrofit.mApi;
        service.getPopluarMovies(API_KEY,LANGUAGE,PAGE_NUMBER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onCompleted() {
                        fragmentItemListBinding.list.setLayoutManager(
                                new GridLayoutManager(getActivity().getApplicationContext(), 2));
                        fragmentItemListBinding.list.setAdapter(
                                new RViewAdapter(movieDetailses, mListener));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Movies movies) {
                        movieDetailses.addAll(movies.results);
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(fragmentItemListBinding == null)
            fragmentItemListBinding = DataBindingUtil.inflate(inflater,
                    R.layout.fragment_item_list, container, false);
        switch (movieQuery){
            case NOW_PLAYING_QUERY:
                ((MainScreenActivity)getActivity()).getSupportActionBar()
                        .setTitle(getActivity().getString(R.string.now_playing_box_office));
                if(isNetworkConnected())
                    getNowPlayingMovies();
                else
                    Toast.makeText(getActivity(), "Internet Connection Required",
                            Toast.LENGTH_SHORT).show();
                break;
            case POPULAR_MOVIE_QUERY:
                ((MainScreenActivity)getActivity()).getSupportActionBar()
                        .setTitle(getActivity().getString(R.string.popular_movies));
                if(isNetworkConnected())
                    getPopluarMovies();
                else
                    Toast.makeText(getActivity(), "Internet Connection Required",
                            Toast.LENGTH_SHORT).show();
                break;
            case TOP_RATED_MOVIES:
                ((MainScreenActivity)getActivity()).getSupportActionBar()
                        .setTitle(getActivity().getString(R.string.top_movies));
                if(isNetworkConnected())
                    getTopRatedMovies();
                else
                    Toast.makeText(getActivity(), "Internet Connection Required",
                            Toast.LENGTH_SHORT).show();

                break;
            case UP_COMING_MOVIES:
                ((MainScreenActivity)getActivity()).getSupportActionBar()
                        .setTitle(getActivity().getString(R.string.upcoming_movies));
                if(isNetworkConnected())
                    getUpComingMovies();
                else
                    Toast.makeText(getActivity(), "Internet Connection Required",
                            Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
        return fragmentItemListBinding.getRoot();
    }

    private void getUpComingMovies() {

        final RetrofitInterface service = retrofit.mApi;
        service.getUpcomingMovies(API_KEY,LANGUAGE,PAGE_NUMBER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onCompleted() {
                        fragmentItemListBinding.list.setLayoutManager(
                                new GridLayoutManager(getActivity().getApplicationContext(), 2));
                        fragmentItemListBinding.list.setAdapter(
                                new RViewAdapter(movieDetailses, mListener));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Movies movies) {
                        movieDetailses.addAll(movies.results);
                    }
                });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieClickListner) {
            mListener = (OnMovieClickListner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
