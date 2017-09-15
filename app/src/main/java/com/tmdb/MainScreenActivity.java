package com.tmdb;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tmdb.databinding.ActivityMainScreenBinding;
import com.tmdb.databinding.AppBarMainScreenBinding;
import com.tmdb.databinding.ContentMainScreenBinding;
import com.tmdb.fragments.MovieFragment;
import com.tmdb.interfaces.OnMovieClickListner;
import com.tmdb.models.MovieDetails;

import static com.tmdb.utils.Constant.NOW_PLAYING_QUERY;
import static com.tmdb.utils.Constant.POPULAR_MOVIE_QUERY;

public class MainScreenActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,OnMovieClickListner {
    private ActivityMainScreenBinding activityMainScreenBinding;
    private AppBarMainScreenBinding appBarMainScreenBinding;
    private ContentMainScreenBinding contentMainScreenBinding;


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

        startFragment(NOW_PLAYING_QUERY);

        activityMainScreenBinding.navView.setNavigationItemSelectedListener(this);
        activityMainScreenBinding.navView.setCheckedItem(R.id.nowPlaying);
    }

    private void startFragment(int movieQuery) {
        getSupportFragmentManager().beginTransaction()
                .detach(MovieFragment.newInstance(movieQuery))
                .attach(MovieFragment.newInstance(movieQuery))
                .add(contentMainScreenBinding.container.getId(),
                        MovieFragment.newInstance(movieQuery))
                .commitNow();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.popular) {
            startFragment(POPULAR_MOVIE_QUERY);
        }
// else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMovieClick(MovieDetails item) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("posterLink",item.backdrop_path);
        intent.putExtra("title",item.original_title);
        intent.putExtra("id",item.id);
        startActivity(intent);
    }
}
