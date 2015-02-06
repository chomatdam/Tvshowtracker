package com.eseo.tvshowtracker.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.EpisodeListAdapter;
import com.eseo.tvshowtracker.model.Season;

/**
 * Created by Damien on 05/02/2015.
 */
public class EpisodeListFragment extends ListFragment{

    private Season season;
    private EpisodeListAdapter mAdapter ;

    public static EpisodeListFragment newInstance(Season season) {
        EpisodeListFragment fragment = new EpisodeListFragment();
        fragment.season = season;
        return fragment;
    }

    public EpisodeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new EpisodeListAdapter(getActivity(),season);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_list,container,false);
        return view ;
    }
}
