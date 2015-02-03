package com.eseo.tvshowtracker.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.adapter.SearchResultAdapter;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;

/**
 * Created by Fat on 03/02/2015.
 */
public class MyShowsFragment extends ListFragment {

    private ArrayList<TvShow> myShows = new ArrayList<TvShow>();
    private SearchResultAdapter mAdapter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myShows.add(new TvShow(1,"Test",null));
        mAdapter = new SearchResultAdapter(this.getActivity(),myShows);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shows,container,false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        return view ;
    }



}
