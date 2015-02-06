package com.eseo.tvshowtracker.UI.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.fragments.SeasonListFragment;
import com.eseo.tvshowtracker.managers.SQLiteManager;
import com.eseo.tvshowtracker.model.TvShow;

public class DetailedTvShowActivity extends ActionBarActivity  {

    ViewPager mViewPager;

    TvShow currentTvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_tv_show);

        final ActionBar actionBar = getSupportActionBar();


        Bundle b = getIntent().getExtras();

        currentTvShow = (TvShow)b.getSerializable("tvshow");
        actionBar.setTitle(currentTvShow.getName());
        if(!currentTvShow.getSeasons().isEmpty()) {
            if (currentTvShow.getSeasons().get(0).getSeason_number() == 0) {
                currentTvShow.getSeasons().remove(0);
            }
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.placeholder,SeasonListFragment.newInstance(currentTvShow));
        transaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailed_tv_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteManager sqLiteManager = new SQLiteManager(DetailedTvShowActivity.this);
                sqLiteManager.updateTvShow(currentTvShow);
                return null;
            }
        }.execute();
    }



}
