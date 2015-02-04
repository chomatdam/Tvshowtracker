package com.eseo.tvshowtracker.UI.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.SearchResultAdapter;
import com.eseo.tvshowtracker.managers.GetTvShowThread;
import com.eseo.tvshowtracker.managers.RESTService;
import com.eseo.tvshowtracker.managers.SQLiteManager;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.SearchResultsPage;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchResultsActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener{

    private ListView mListResults ;
    private SearchResultAdapter mAdapter ;
    private ArrayList<TvShow> mResults = new ArrayList<TvShow>();
    private SQLiteManager mSqLiteManager ;

    private ProgressBar mProgressBar ;

    private MenuItem mSearchItem ;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTIVITY LIFE CYCLE
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mSqLiteManager = new SQLiteManager(this);

        mListResults = (ListView) findViewById(R.id.listTVShows);
        mProgressBar = (ProgressBar) findViewById(R.id.research_progress_bar);


        mAdapter = new SearchResultAdapter(this,mResults);
        mListResults.setAdapter(mAdapter);
        mListResults.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        mSearchItem = menu.findItem(R.id.action_search_tv_show);
        SearchView searchView = (SearchView)mSearchItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        return true;

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LISTENERS
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        RESTService service = TVShowManager.getInstance().getService();
        service.getTvShows(query,mSearchCallBack);
        mSearchItem.collapseActionView();
        return false ;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TvShow tvShow = (TvShow) parent.getItemAtPosition(position);
        new GetTvShowThread(tvShow.getId(),mResultHandler).start();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // RETROFIT CALLBACKS
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Callback<SearchResultsPage> mSearchCallBack = new Callback<SearchResultsPage>() {
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


    private Handler mResultHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GetTvShowThread.TV_SHOW_ANSWER:
                    TvShow selectedTvShow = (TvShow)msg.obj ;
                    mSqLiteManager.createTvShow(selectedTvShow);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };



}
