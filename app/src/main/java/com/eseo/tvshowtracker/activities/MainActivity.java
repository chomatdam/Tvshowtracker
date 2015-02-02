package com.eseo.tvshowtracker.activities;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.managers.RESTService;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.TVShow;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Tabs */
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab().setText(R.string.tab_myShows).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_toptvshows).setTabListener(this));


        //TODO: TEST WEB SERVICE (RETROFIT)
       // testWS();

        //TODO: TEST INJECTION (ROBOBINDING)
        //testRB
    }

    public void testWS(){
        TVShow show = TVShowManager.getInstance().getService().getTVShow("Breaking Bad");
        show.getName();
    }

    public void testRB(){
        ArrayList<String> series = new ArrayList<String>(Arrays.asList("Breaking Bad", "Lost", "Arrow", "The 100", "Marco Polo", "Grimm", "Gotham", "House of Cards"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
