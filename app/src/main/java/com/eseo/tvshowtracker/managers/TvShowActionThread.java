package com.eseo.tvshowtracker.managers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


/**
 * Created by Damien on 2/4/2015.
 */
public class TvShowActionThread extends Thread {

    public static final int TV_SHOW_ANSWER = 1 ;
    public static final int TV_SHOW_CREATE_ACTION = 2 ;
    public static final int TV_SHOW_UPDATE_ACTION = 3 ;
    public static final int TV_SHOW_DELETE_ACTION = 4 ;


    private long mTvShowId ;
    private Context context;
    private Handler mResultHandler ;
    private SQLiteManager db;

    public TvShowActionThread(Context c, long tvShowId, Handler resultHandler){
        this.mTvShowId = tvShowId ;
        this.mResultHandler = resultHandler ;
        this.context = c;
        this.db = new SQLiteManager(context);
    }

    @Override
    public void run() {
        //TODO(thread): database actions
        /*
        int choix = mResultHandler.obtainMessage();
        mResultHandler.handleMessage();
        switch(choix){
            case(TV_SHOW_CREATE_ACTION):
                break;
            case(TV_SHOW_UPDATE_ACTION):
                break;
            case(TV_SHOW_DELETE_ACTION):
                break;
            default:
                break;
        }
        Message message;
        message.toString();
        mResultHandler.obtainMessage("");
                db.createTvShow(db.getTvShow(mTvShowId));
                */

    }
}