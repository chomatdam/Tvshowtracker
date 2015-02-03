package com.eseo.tvshowtracker.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.MyFragmentPageAdapter;
import com.eseo.tvshowtracker.UI.fragments.MyShowsFragment;
import com.eseo.tvshowtracker.UI.fragments.PopularShowsFragment;
import com.eseo.tvshowtracker.managers.SQLiteManager;
import com.eseo.tvshowtracker.model.Episode;
import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager mViewPager ;
    private MyFragmentPageAdapter mAdapter ;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        fragments.add(new MyShowsFragment());
        fragments.add(new PopularShowsFragment());

        mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);


    /* Tabs */
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab().setText(R.string.tab_myShows).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_toptvshows).setTabListener(this));


        /*
                TEST ZONE
         */
        SQLiteManager db = new SQLiteManager(this);
        db.clear();

        TvShow show = new TvShow();
        show.setName("Alias");
        db.createTvShow(show);
        Log.e("DB_RESULT_TVSHOW", db.getTvShow("Alias").getName());

        Season season = new Season();
        season.setSeason_number(1);
        season.setEpisode_count(22);
        db.createSeason(db.getTvShow("Alias"), season);
      //  Log.e("DB_RESULT_SEASON", String.valueOf(db.getTvShow("Alias").getSeasons().get(0).getEpisode_count()));

        Episode episode = new Episode();
        episode.setName("Le premier épisode");
        //TODO: getSeasons() ne peut être retrouvé de la BDD, pas de lien tvshow -> saison  (saison -> tvshow only)
       // db.createEpisode(db.getTvShow("Alias").getSeasons().get(0), episode);
     //   Log.e("DB_RESULT_EPISODE", String.valueOf(db.getTvShow("Alias").getSeasons().get(0).getEpisodes().get(0).getName()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_tv_show) {
            startActivity(new Intent(this, SearchResultsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

}
