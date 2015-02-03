package com.eseo.tvshowtracker.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eseo.tvshowtracker.model.Episode;
import com.eseo.tvshowtracker.model.Season;
import com.eseo.tvshowtracker.model.TvShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damien on 2/3/2015.
 */
public class SQLiteManager extends SQLiteOpenHelper {

    /* Database */
    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;

    /* All tables */
    private static final String COL_PK_ID = "ID";

    /* Table TVSHOW */
    private static final String TABLE_TVSHOW = "TVSHOW";

    private static final String COL_NAME_TVSHOW = "ORIGINAL_NAME";
    private static final String COL_POSTER_TVSHOW = "POSTER_PATH";

    /* Table SEASON */
    private static final String TABLE_SEASON = "SEASON";

    private static final String COL_SEASON_NUMBER = "SEASON_NUMBER";
    private static final String COL_EPISODE_COUNT = "EPISODE_COUNT";
    private static final String COL_FK_ID_TVSHOW = "ID_TVSHOW";

    /* Table EPISODE */
    private static final String TABLE_EPISODE = "EPISODE";

    private static final String COL_NAME_EPISODE = "NAME";
    private static final String COL_AIR_DATE = "AIR_DATE";
    private static final String COL_EPISODE_SEEN = "EPISODE_SEEN";
    private static final String COL_FK_ID_SEASON = "ID_SEASON";

    /* Table Create Statements */
    //NOTE: INTEGER PRIMARY KEY will autoincrement
    private static final String CREATE_TABLE_TVSHOW = "CREATE TABLE "
            + TABLE_TVSHOW + "(" + COL_PK_ID + " INTEGER PRIMARY KEY," + COL_NAME_TVSHOW
            + " TEXT," + COL_POSTER_TVSHOW + " TEXT)";

    private static final String CREATE_TABLE_SEASON = "CREATE TABLE "
            + TABLE_SEASON + "(" + COL_PK_ID + " INTEGER PRIMARY KEY," + COL_SEASON_NUMBER
            + " INTEGER," + COL_EPISODE_COUNT + " INTEGER," + COL_FK_ID_TVSHOW + " INTEGER)";

    private static final String CREATE_TABLE_EPISODE = "CREATE TABLE "
            + TABLE_EPISODE + "(" + COL_PK_ID + " INTEGER PRIMARY KEY,"
            + COL_NAME_EPISODE + " TEXT," + COL_EPISODE_SEEN + " INTEGER," +
            COL_AIR_DATE + "TEXT," + COL_FK_ID_SEASON + " INTEGER)";


    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TVSHOW);
        db.execSQL(CREATE_TABLE_SEASON);
        db.execSQL(CREATE_TABLE_EPISODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteManager.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSHOW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODE);
        onCreate(db);
    }
    /*
    DB REQUESTS (CREATE - DELETE - GET)  TODO: ->DAO
     */

    public void clear(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSHOW);

        onCreate(db);
    }


    public long createTvShow(TvShow show) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME_TVSHOW, show.getName());
        values.put(COL_POSTER_TVSHOW, show.getPoster_url());

        // insert row
        long tvshow_id = db.insert(TABLE_TVSHOW, null, values);
        show.setId(tvshow_id);

        return tvshow_id;
    }

    public long createSeason(TvShow show, Season season) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id_tvshow = show.getId();

        ContentValues values = new ContentValues();
        values.put(COL_SEASON_NUMBER,season.getSeason_number());
        values.put(COL_EPISODE_COUNT, season.getEpisode_count());
        values.put(COL_FK_ID_TVSHOW, id_tvshow);

        show.getSeasons().add(season);
        season.setId_tvshow(id_tvshow);

        // insert row
        long season_id = db.insert(TABLE_SEASON, null, values);

        return season_id;
    }

    public long createEpisode(Season season, Episode episode){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME_EPISODE,episode.getName());
        values.put(COL_AIR_DATE, episode.getAir_date());
        values.put(COL_EPISODE_SEEN, episode.isSeen());
        values.put(COL_FK_ID_SEASON, season.getId());

        season.getEpisodes().add(episode);
        episode.setId_season(season.getId());

        // insert row
        long episode_id = db.insert(TABLE_EPISODE, null, values);

        return episode_id;
    }


    public void deleteTvShow(long tvshow_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Season> seasons = getAllSeasons(tvshow_id);
        for(Season season : seasons){
            ArrayList<Episode> episodes = getAllEpisodes(season.getId());
            for(Episode episode : episodes){
                db.delete(TABLE_EPISODE,COL_PK_ID,
                        new String[] { String.valueOf(episode.getId()) });
            }
            db.delete(TABLE_SEASON,COL_PK_ID,
                    new String[] { String.valueOf(season.getId()) });
        }
        db.delete(TABLE_TVSHOW, COL_PK_ID + " = ?",
                new String[] { String.valueOf(tvshow_id) });
    }
    //TODO: ISSUE
    public void deleteSeason(long tvshow_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SEASON, COL_PK_ID + " = ?",
                new String[] { String.valueOf(tvshow_id) });
    }

    public void deleteEpisode(long season_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EPISODE, COL_PK_ID + " = ?",
                new String[] { String.valueOf(season_id) });
    }

    public TvShow getTvShow(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TVSHOW + " WHERE "
                + COL_NAME_TVSHOW + " = '" + name +"';";

        Cursor c = db.rawQuery(selectQuery, null);

        TvShow show;
        if (c != null) {
            c.moveToFirst();
            show = getTvShow(c.getInt(c.getColumnIndex(COL_PK_ID)));
        }
        else
            show = null;

        return show;
    }

    public TvShow getTvShow(long tvshow_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TVSHOW + " WHERE "
                + COL_PK_ID + " = " + tvshow_id;

        Log.e("DB_SELECT", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TvShow show = new TvShow();
        show.setId(c.getInt(c.getColumnIndex(COL_PK_ID)));
        show.setName((c.getString(c.getColumnIndex(COL_NAME_TVSHOW))));
        show.setPoster_url(c.getString(c.getColumnIndex(COL_POSTER_TVSHOW)));
        show.setSeasons(getAllSeasons(c.getInt(c.getColumnIndex(COL_PK_ID))));

        return show;
    }

    public ArrayList<Season> getAllSeasons(long idShow){
        ArrayList<Season> seasons = new ArrayList<Season>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SEASON + " WHERE "
                + COL_PK_ID + " = " + idShow;

        Log.e("DB_SELECT", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Season season = new Season();
                season.setId(c.getLong(c.getColumnIndex(COL_PK_ID)));
                season.setSeason_number(c.getInt(c.getColumnIndex(COL_SEASON_NUMBER)));
                season.setEpisode_count(c.getInt(c.getColumnIndex(COL_EPISODE_COUNT)));
                //season.setAir_date();
                //season.setPoster_path();
                season.setEpisodes(getAllEpisodes(c.getInt(c.getColumnIndex(COL_PK_ID))));
            } while (c.moveToNext());
        }
        return seasons;
    }

    public ArrayList<Episode> getAllEpisodes(long idSeason){
        ArrayList<Episode> episodes = new ArrayList<Episode>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EPISODE + " WHERE "
                + COL_PK_ID + " = " + idSeason;

        Log.e("DB_SELECT", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Episode episode = new Episode();
                episode.setId(c.getInt(c.getColumnIndex(COL_PK_ID)));
                episode.setName(c.getString(c.getColumnIndex(COL_NAME_EPISODE)));
                episode.setSeen((c.getInt(c.getColumnIndex(COL_EPISODE_SEEN))));
                episode.setAir_date(c.getString(c.getColumnIndex(COL_AIR_DATE)));
                episodes.add(episode);
            } while (c.moveToNext());
        }
        return episodes;
    }

}
