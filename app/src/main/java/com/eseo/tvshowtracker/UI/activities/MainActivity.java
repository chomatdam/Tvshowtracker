package com.eseo.tvshowtracker.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.MyFragmentPageAdapter;
import com.eseo.tvshowtracker.UI.fragments.MyShowsFragment;
import com.eseo.tvshowtracker.UI.fragments.PopularShowsFragment;
import com.eseo.tvshowtracker.UI.listeners.OnNotificationListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements OnNotificationListener, ActionBar.TabListener {

    private ViewPager mViewPager ;
    private MyFragmentPageAdapter mAdapter ;
    private MyShowsFragment myshowsFragment;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myshowsFragment = new MyShowsFragment();
        fragments.add(myshowsFragment);
        fragments.add(new PopularShowsFragment());

        mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

            /* Tabs */
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab().setText(R.string.tab_myShows).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_toptvshows).setTabListener(this));


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int arg0) {
                actionBar.setSelectedNavigationItem(arg0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onNotification() {
        myshowsFragment.update();
        mAdapter.notifyDataSetChanged();
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
