package com.eseo.tvshowtracker.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.ListSeasonAdapter;
import com.eseo.tvshowtracker.UI.adapter.SearchResultAdapter;
import com.eseo.tvshowtracker.managers.GetTvShowThread;
import com.eseo.tvshowtracker.managers.RESTService;
import com.eseo.tvshowtracker.managers.SQLiteManager;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.SearchResultsPage;
import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Damien on 05/02/2015.
 */
public class SeasonListFragment extends ListFragment{

    private TvShow tvshow;
    private ListSeasonAdapter mAdapter ;

    public static SeasonListFragment newInstance(int sectionNumber, TvShow tvshow) {
        SeasonListFragment fragment = new SeasonListFragment();
        fragment.tvshow = tvshow;
        return fragment;
    }

    public SeasonListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ListSeasonAdapter(getActivity(),tvshow);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_episodes_tvshow,container,false);
        return view ;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO: get episodes
    }
}
