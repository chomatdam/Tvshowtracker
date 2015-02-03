package com.eseo.tvshowtracker.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

/**
 * Created by Fat on 03/02/2015.
 */
public class PopularShowsFragment extends ListFragment {

    private ArrayList<TvShow> popularShows = new ArrayList<TvShow>();
    private SearchResultAdapter mAdapter ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SearchResultAdapter(getActivity(),popularShows);

        RESTService service = TVShowManager.getInstance().getService();
        service.getPopularTvShows(mCallBack);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_shows,container,false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);


        return view ;
    }


    private Callback<SearchResultsPage> mCallBack = new Callback<SearchResultsPage>() {
        @Override
        public void success(SearchResultsPage searchResultsPage, Response response) {
            mAdapter.addAll(searchResultsPage.getResults());
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(PopularShowsFragment.this.getActivity(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
        }
    };


}
