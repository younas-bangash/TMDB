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
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
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
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tmdb.utils.Constant.API_KEY;
import static com.tmdb.utils.Constant.NOW_PLAYING_QUERY;
import static com.tmdb.utils.Constant.POPULAR_MOVIE_QUERY;
import static com.tmdb.utils.Constant.TOP_RATED_MOVIES;
import static com.tmdb.utils.Constant.UP_COMING_MOVIES;

public class MainScreenActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnMovieClickListner {
    private ActivityMainScreenBinding activityMainScreenBinding;
    private AppBarMainScreenBinding appBarMainScreenBinding;
    private ContentMainScreenBinding contentMainScreenBinding;
    private List<SearchItem> qeryResult = new ArrayList<>();
    private SearchAdapter searchAdapter = null;
    private SearchHistoryTable mHistoryDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainScreenBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main_screen);
        appBarMainScreenBinding = activityMainScreenBinding.appBarMainScreen;
        contentMainScreenBinding = appBarMainScreenBinding.contentMainScreen;
        setSupportActionBar(appBarMainScreenBinding.toolbar);
        mHistoryDatabase = new SearchHistoryTable(this);
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
        appBarMainScreenBinding.searchView.showProgress();
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                mHistoryDatabase.addItem(new SearchItem(text));
                appBarMainScreenBinding.searchView.close(false);
            }
        });

        initializeSearch();

    }

    private void initializeSearch() {

        final SearchView mSearchView = appBarMainScreenBinding.searchView;
        if (mSearchView != null) {
//            mSearchView.setVersionMargins(SearchView.VersionMargins.TOOLBAR_SMALL);
            mSearchView.setHint("search");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mHistoryDatabase.addItem(new SearchItem(query));
                    mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    getMoviesList(newText);
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {
//                    if (mFab != null) {
//                        mFab.hide();
//                    }
                    return true;
                }

                @Override
                public boolean onClose() {
//                    if (mFab != null && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                        mFab.show();
//                    }
                    return true;
                }
            });
        }

    }

    private void getMoviesList(final String query) {
        qeryResult.clear();
        RetrofitBuilder retrofit = new RetrofitBuilder();
        final RetrofitInterface service = retrofit.mApi;

        service.searchMovie(API_KEY,query)
                .subscribeOn(Schedulers.newThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainScreenActivity.this, "oncomplete call", Toast.LENGTH_SHORT).show();
                        appBarMainScreenBinding.searchView.hideProgress();
                        searchAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Movies movies) {
                        for (MovieDetails details :
                                movies.results) {
                            SearchItem searchItem = new SearchItem(details.original_title);
                            qeryResult.add(searchItem);
                        }
                    }
                });
    }

    private void updateFragment(int queryID) {
        MovieFragment searchingFragment = MovieFragment.newInstance(queryID);
        if (getSupportFragmentManager().findFragmentById(
                contentMainScreenBinding.container.getId()) != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager()
                    .findFragmentById(contentMainScreenBinding.container.getId())).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(
                contentMainScreenBinding.container.getId(), searchingFragment).commit();
    }

    private void startFragment(int movieQuery) {
        getSupportFragmentManager().beginTransaction()
                .remove(MovieFragment.newInstance(movieQuery)).commit();

        getSupportFragmentManager().beginTransaction()
                .detach(MovieFragment.newInstance(movieQuery))
                .attach(MovieFragment.newInstance(movieQuery))
                .add(contentMainScreenBinding.container.getId(),
                        MovieFragment.newInstance(movieQuery))
                .commitNow();
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
                updateFragment(POPULAR_MOVIE_QUERY);
                break;
            case R.id.nowPlaying:
                updateFragment(NOW_PLAYING_QUERY);
                break;
            case R.id.top:
                updateFragment(TOP_RATED_MOVIES);
                break;
            case R.id.upcoming:
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
        intent.putExtra("id", item.id);
        startActivity(intent);
    }

}
