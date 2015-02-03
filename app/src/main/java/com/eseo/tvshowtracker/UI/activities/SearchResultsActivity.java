package com.eseo.tvshowtracker.UI.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.SearchResultAdapter;
import com.eseo.tvshowtracker.managers.RESTService;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.SearchResultsPage;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchResultsActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{

    private ListView mListResults ;
    private SearchResultAdapter mAdapter ;
    private ArrayList<TvShow> mResults = new ArrayList<TvShow>();

    private ProgressBar mProgressBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mListResults = (ListView) findViewById(R.id.listTVShows);
        mProgressBar = (ProgressBar) findViewById(R.id.research_progress_bar);


        mAdapter = new SearchResultAdapter(this,mResults);
        mListResults.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search_tv_show).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_tv_show) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Callback<SearchResultsPage> mCallBack = new Callback<SearchResultsPage>() {
        @Override
        public void success(SearchResultsPage searchResultsPage, Response response) {
            mAdapter.addAll(searchResultsPage.getResults());
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(SearchResultsActivity.this,"Une erreur est survenue",Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.GONE);
        }
    };


    @Override
    public boolean onQueryTextSubmit(String query) {
        queryService(query);
        return true ;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    private void queryService(String query){
        mAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        RESTService service = TVShowManager.getInstance().getService();
        service.getTvShows(query,mCallBack);
    }


}
