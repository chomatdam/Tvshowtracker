package com.eseo.tvshowtracker.UI.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eseo.tvshowtracker.R;
import com.eseo.tvshowtracker.UI.activities.DetailedTvShowActivity;
import com.eseo.tvshowtracker.managers.SQLiteManager;
import com.eseo.tvshowtracker.managers.TVShowManager;
import com.eseo.tvshowtracker.model.TvShow;
import com.squareup.picasso.Picasso;

/**
 * Created by Fat on 03/02/2015.
 */

public class MyShowsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TVSHOW_LIST_LOADER = 0x01;


    private SimpleCursorAdapter mAdapter ;
    private TvShow selectedTvShow ;

    private ProgressDialog mProgressDialog ;

    SQLiteManager mSqLiteManager ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSqLiteManager = new SQLiteManager(getActivity());

        String[] from =  new String[]{"poster_path","original_name","next_episode"};
        int[] to = new int[]{R.id.tvshow_poster_image_view,R.id.tvshow_name_text_view,R.id.tv_show_next_episode};

        getLoaderManager().initLoader(TVSHOW_LIST_LOADER, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.row_my_tv_show,
                null, from, to, Adapter.IGNORE_ITEM_VIEW_TYPE);


        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int column) {
                //POSTER_PATH
                if( column == 2 ){
                    ImageView posterView = (ImageView) view;
                    String posterPath = cursor.getString(column);
                    Picasso.with(getActivity())
                            .load(TVShowManager.IMAGE_URL+posterPath)
                            .error(R.drawable.tv_icon)
                            .placeholder(R.drawable.tv_icon)
                            .into(posterView);

                    return true;
                }
                return false;
            }
        });

        setListAdapter(mAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shows,container,false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        registerForContextMenu(listView);
        return view ;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()== android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            RelativeLayout rowLayout = (RelativeLayout) mAdapter.getView(info.position, info.targetView, (ViewGroup) v);
            TextView name = (TextView) rowLayout.findViewById(R.id.tvshow_name_text_view);

            selectedTvShow = mSqLiteManager.getTvShow((String)name.getText());

            menu.setHeaderTitle((String)name.getText());
            String[] menuItems = getResources().getStringArray(R.array.options);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.options);
        String menuItemName = menuItems[menuItemIndex];

        switch(menuItemIndex){
            //UPDATE
            case 0 :
                new DataBaseAsyncTask().execute(SQLiteManager.DELETE_CODE);
                break;
            //DELETE
            case 1 :
                new DataBaseAsyncTask().execute(SQLiteManager.DELETE_CODE);
                break;
        }


        return true;
    }


    @Override
    public void onResume() {
       super.onResume();
       update();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

        return new CursorLoader(getActivity(),null, null, null, null,null){
            @Override
            protected Cursor onLoadInBackground() {
                return mSqLiteManager.queryAllDataShow();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), DetailedTvShowActivity.class);
        Bundle bundle = new Bundle();

        TextView name = (TextView) v.findViewById(R.id.tvshow_name_text_view);
        selectedTvShow = mSqLiteManager.getTvShow((String)name.getText());

        bundle.putSerializable("tvshow",selectedTvShow);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void update() {
        getLoaderManager().restartLoader(TVSHOW_LIST_LOADER,null,this);
    }

    private class  DataBaseAsyncTask extends AsyncTask<Integer,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyShowsFragment.this.getActivity());
            mProgressDialog.setMessage("TV show being removed ... ");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            int ddbAction = params[0] ;
            long id_tvshow = selectedTvShow.getId() ;

            switch(ddbAction){
                case SQLiteManager.UPDATE_CODE:
                    break ;

                case SQLiteManager.DELETE_CODE:
                    mSqLiteManager.deleteTvShow(id_tvshow);
                    break ;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            update();
        }
    }


}
