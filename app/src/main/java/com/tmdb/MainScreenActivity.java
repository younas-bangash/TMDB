package com.tmdb;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.tmdb.databinding.ActivityMainScreenBinding;
import com.tmdb.databinding.AppBarMainScreenBinding;
import com.tmdb.databinding.ContentMainScreenBinding;
import com.tmdb.fragments.MovieFragment;
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
import static com.tmdb.utils.Constant.SELECTED_QUERY;
import static com.tmdb.utils.Constant.TOP_RATED_MOVIES;
import static com.tmdb.utils.Constant.UP_COMING_MOVIES;

public class MainScreenActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnMovieClickListner {
    private ActivityMainScreenBinding activityMainScreenBinding;
    private AppBarMainScreenBinding appBarMainScreenBinding;
    private ContentMainScreenBinding contentMainScreenBinding;
    private List<SearchItem> qeryResult = new ArrayList<>();
    private List<MovieDetails> movieDetailsTemp = new ArrayList<>();
    private SearchAdapter searchAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainScreenBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main_screen);
        appBarMainScreenBinding = activityMainScreenBinding.appBarMainScreen;
        contentMainScreenBinding = appBarMainScreenBinding.contentMainScreen;
        setSupportActionBar(appBarMainScreenBinding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityMainScreenBinding.drawerLayout, appBarMainScreenBinding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainScreenBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        updateFragment(NOW_PLAYING_QUERY);

        activityMainScreenBinding.navView.setNavigationItemSelectedListener(this);
        activityMainScreenBinding.navView.setCheckedItem(R.id.nowPlaying);
        searchAdapter = new SearchAdapter(this, qeryResult);
        appBarMainScreenBinding.searchView.setAdapter(searchAdapter);

        searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(View view, int position) {
                TextView textView = view.findViewById(R.id.search_text);
                String query = textView.getText().toString();
                appBarMainScreenBinding.searchView.close(false);

                Intent intent = new Intent(MainScreenActivity.this, MovieDetailActivity.class);
                intent.putExtra("posterLink", movieDetailsTemp.get(position).backdrop_path);
                intent.putExtra("title", query);
                intent.putExtra("id", movieDetailsTemp.get(position).id);
                startActivity(intent);


            }
        });

        initializeSearch();

    }

    private void initializeSearch() {

        final SearchView mSearchView = appBarMainScreenBinding.searchView;
        if (mSearchView != null) {
            mSearchView.setVersionMargins(SearchView.VersionMargins.TOOLBAR_SMALL);
            mSearchView.setHint("search");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    appBarMainScreenBinding.searchView.showProgress();
                    getMoviesList(newText);
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {
                    return true;
                }

                @Override
                public boolean onClose() {
                    return true;
                }
            });
        }

    }

    private void getMoviesList(final String query) {
        qeryResult.clear();
        movieDetailsTemp.clear();
        RetrofitBuilder retrofit = new RetrofitBuilder(getApplicationContext());
        final RetrofitInterface service = retrofit.mApi;

        service.searchMovie(API_KEY,query,LANGUAGE,PAGE_NUMBER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onCompleted() {
                        appBarMainScreenBinding.searchView.hideProgress();
//                        searchAdapter.notifyDataSetChanged();
                        appBarMainScreenBinding.searchView.setSuggestionsList(qeryResult);
                        appBarMainScreenBinding.searchView.showSuggestions();
                    }

                    @Override
                    public void onError(Throwable e) {
                        appBarMainScreenBinding.searchView.hideProgress();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Movies movies) {
                        movieDetailsTemp.addAll(movies.results);
                        for (MovieDetails details : movies.results) {
                            SearchItem searchItem = new SearchItem(details.original_title);
                            searchItem.setTag(movies.id);
                            qeryResult.add(searchItem);
                        }
                    }
                });
    }

    private void updateFragment(int queryID) {
        SELECTED_QUERY = queryID;
        MovieFragment searchingFragment = MovieFragment.newInstance(queryID);
        if (getSupportFragmentManager().findFragmentById(
                contentMainScreenBinding.container.getId()) != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager()
                    .findFragmentById(contentMainScreenBinding.container.getId())).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(
                contentMainScreenBinding.container.getId(), searchingFragment).commit();
    }


    @Override
    public void onBackPressed() {
        if (activityMainScreenBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainScreenBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_search){
            appBarMainScreenBinding.searchView.open(true); // enable or disable animation
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                PAGE_NUMBER = 1 ;
                updateFragment(POPULAR_MOVIE_QUERY);
                break;
            case R.id.nowPlaying:
                PAGE_NUMBER = 1 ;
                updateFragment(NOW_PLAYING_QUERY);
                break;
            case R.id.top:
                PAGE_NUMBER = 1 ;
                updateFragment(TOP_RATED_MOVIES);
                break;
            case R.id.upcoming:
                PAGE_NUMBER = 1 ;
                updateFragment(UP_COMING_MOVIES);
                break;
            default:
                break;
        }
        activityMainScreenBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);

        MenuItem item = menu.findItem(R.id.action_search);
//        appBarMainScreenBinding.searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onMovieClick(MovieDetails item) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("posterLink", item.backdrop_path);
        intent.putExtra("title", item.original_title);
        intent.putExtra("fromMainActivity",true);
        intent.putExtra("id", item.id);
        startActivity(intent);
    }
}
