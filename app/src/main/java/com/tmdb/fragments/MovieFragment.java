package com.tmdb.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMovieClickListner}
 * interface.
 */
public class MovieFragment extends Fragment {


    private static final String API_KEY = "f7caf4a40a5accddacdad05cb1cdb792";
    private static final String language = "en-US";
    private static final String include_adult = "false";
    private static final String sort_by = "created_at.desc";
    private List<MovieDetails> movieDetailses = new ArrayList<>();
    private OnMovieClickListner mListener;
    private FragmentItemListBinding fragmentItemListBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    @SuppressWarnings("unused")
    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        getResults();
    }

    private void getResults() {
        RetrofitBuilder retrofit = new RetrofitBuilder();
        final RetrofitInterface service = retrofit.mApi;
        service.getAllMovies(API_KEY,language,include_adult,sort_by)
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

        fragmentItemListBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_item_list, container, false);
        return fragmentItemListBinding.getRoot();
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
}
