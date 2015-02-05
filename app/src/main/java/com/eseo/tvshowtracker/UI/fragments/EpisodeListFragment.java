package com.eseo.tvshowtracker.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.ListEpisodeAdapter;
import com.eseo.tvshowtracker.UI.adapter.ListSeasonAdapter;
import com.eseo.tvshowtracker.model.Season;

/**
 * Created by Damien on 05/02/2015.
 */
public class EpisodeListFragment extends ListFragment{

    private Season season;
    private ListEpisodeAdapter mAdapter ;

    public static EpisodeListFragment newInstance(int sectionNumber, Season season) {
        EpisodeListFragment fragment = new EpisodeListFragment();
        fragment.season = season;
        return fragment;
    }

    public EpisodeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ListEpisodeAdapter(getActivity(),season);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_episodes_tvshow,container,false);
        return view ;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO: context menu of this episode
    }
}
