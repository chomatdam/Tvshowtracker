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

/**
 * Created by Damien on 2/3/2015.
 */
public class SQLiteManager extends SQLiteOpenHelper {


    public static final int DELETE_CODE = 1 ;
    public static final int UPDATE_CODE = 2 ;

    /* Database */
    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;

    /* All tables */
    private static final String COL_PK_ID = "_id";
    private static final String COL_POSTER = "poster_path";
    private static final String COL_AIR_DATE = "air_date";

    /* Table TVSHOW */
    private static final String TABLE_TVSHOW = "TVSHOW";

    private static final String COL_NAME_TVSHOW = "original_name";
    private static final String COL_ID_MOVIEDB = "id_moviedb";
    private static final String COL_NEXT_EPISODE_TVSHOW = "next_episode";
    private static final String COL_OVERVIEW_TVSHOW = "overview";

    /* Table SEASON */
    private static final String TABLE_SEASON = "SEASON";

    private static final String COL_SEASON_NUMBER = "season_number";
    private static final String COL_EPISODE_COUNT = "episode_count";
    private static final String COL_FK_ID_TVSHOW = "id_tvshow";

    /* Table EPISODE */
    private static final String TABLE_EPISODE = "EPISODE";

    private static final String COL_NAME_EPISODE = "name";
    private static final String COL_EPISODE_SEEN = "episode_seen";
    private static final String COL_FK_ID_SEASON = "id_season";

    /* Table Create Statements */
    //NOTE: INTEGER PRIMARY KEY will autoincrement
    private static final String CREATE_TABLE_TVSHOW = "CREATE TABLE "
            + TABLE_TVSHOW + "(" + COL_PK_ID + " INTEGER PRIMARY KEY," + COL_NAME_TVSHOW
            + " TEXT," + COL_POSTER + " TEXT,"+ COL_OVERVIEW_TVSHOW + " TEXT,"
            +COL_ID_MOVIEDB+" INTEGER,"+ COL_NEXT_EPISODE_TVSHOW +" TEXT)";

    private static final String CREATE_TABLE_SEASON = "CREATE TABLE "
            + TABLE_SEASON + "(" + COL_PK_ID + " INTEGER PRIMARY KEY," + COL_SEASON_NUMBER
            + " INTEGER," + COL_EPISODE_COUNT + " INTEGER,"+COL_AIR_DATE+" TEXT,"
            +COL_POSTER+" TEXT,"+ COL_FK_ID_TVSHOW + " INTEGER)";

    private static final String CREATE_TABLE_EPISODE = "CREATE TABLE "
            + TABLE_EPISODE + "(" + COL_PK_ID + " INTEGER PRIMARY KEY,"
            + COL_NAME_EPISODE + " TEXT," + COL_EPISODE_SEEN + " INTEGER," +
            COL_AIR_DATE + " TEXT," + COL_FK_ID_SEASON + " INTEGER)";


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
    DB REQUESTS  TODO(reorganization): DAO structure OR Framework (ORMDroid/DroidPersistence/Cupboard)
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
        values.put(COL_POSTER, show.getPoster_url());
        values.put(COL_NEXT_EPISODE_TVSHOW, show.getNextEpisode());
        values.put(COL_OVERVIEW_TVSHOW, show.getOverview());
        values.put(COL_ID_MOVIEDB, show.getId_moviedb());

        // insert row
        long tvshow_id = db.insert(TABLE_TVSHOW, null, values);
        show.setId(tvshow_id);

        for(Season season : show.getSeasons()){
            createSeason(show.getId(),season);
        }

        return tvshow_id;
    }

    public long createSeason(long tvshow_id, Season season) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SEASON_NUMBER,season.getSeason_number());
        values.put(COL_EPISODE_COUNT, season.getEpisode_count());
        values.put(COL_AIR_DATE, season.getAir_date());
        values.put(COL_POSTER, season.getPoster_path());
        values.put(COL_FK_ID_TVSHOW, tvshow_id);

        season.setId_tvshow(tvshow_id);

        // insert row
        long season_id = db.insert(TABLE_SEASON, null, values);
        season.setId(season_id);

        for(Episode episode : season.getEpisodes()){
           createEpisode(season.getId(),episode);
        }

        return season_id;
    }

    public long createEpisode(long season_id, Episode episode){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME_EPISODE,episode.getName());
        values.put(COL_EPISODE_SEEN, episode.isSeen());
        values.put(COL_AIR_DATE, episode.getAir_date());
        values.put(COL_FK_ID_SEASON, season_id);

        episode.setId_season(season_id);

        // insert row
        long episode_id = db.insert(TABLE_EPISODE, null, values);
        episode.setId(episode_id);

        return episode_id;
    }


    public void deleteTvShow(long tvshow_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Season> seasons = getAllSeasons(tvshow_id);
        for(Season season : seasons){
            ArrayList<Episode> episodes = getAllEpisodes(season.getId());
            for(Episode episode : episodes){
               deleteEpisode(episode.getId());
            }
           deleteSeason(season.getId());
        }
        db.delete(TABLE_TVSHOW, COL_PK_ID + " = ?",
                new String[] { String.valueOf(tvshow_id) });
    }

    public void deleteSeason(long season_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SEASON, COL_PK_ID + " = ?",
                new String[] { String.valueOf(season_id) });
    }

    public void deleteEpisode(long episode_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EPISODE, COL_PK_ID + " = ?",
                new String[] { String.valueOf(episode_id) });
    }

    public TvShow getTvShow(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        name = name.replace("'","''");

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
        show.setId_moviedb(c.getInt(c.getColumnIndex(COL_ID_MOVIEDB)));
        show.setName((c.getString(c.getColumnIndex(COL_NAME_TVSHOW))));
        show.setPoster_url(c.getString(c.getColumnIndex(COL_POSTER)));
        show.setNextEpisode(c.getString(c.getColumnIndex(COL_NEXT_EPISODE_TVSHOW)));
        show.setOverview(c.getString(c.getColumnIndex(COL_OVERVIEW_TVSHOW)));
        show.setSeasons(getAllSeasons(c.getInt(c.getColumnIndex(COL_PK_ID))));


        return show;
    }

    public ArrayList<Season> getAllSeasons(long idShow){
        ArrayList<Season> seasons = new ArrayList<Season>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SEASON + " WHERE "
                + COL_FK_ID_TVSHOW + " = " + idShow;

        Log.e("DB_SELECT", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Season season = new Season();
                season.setId(c.getLong(c.getColumnIndex(COL_PK_ID)));
                season.setId_tvshow(c.getLong(c.getColumnIndex(COL_FK_ID_TVSHOW)));
                season.setSeason_number(c.getInt(c.getColumnIndex(COL_SEASON_NUMBER)));
                season.setEpisode_count(c.getInt(c.getColumnIndex(COL_EPISODE_COUNT)));
                season.setAir_date(c.getString(c.getColumnIndex(COL_AIR_DATE)));
                season.setPoster_path(c.getString(c.getColumnIndex(COL_POSTER)));
                season.setEpisodes(getAllEpisodes(c.getInt(c.getColumnIndex(COL_PK_ID))));
                seasons.add(season);
            } while (c.moveToNext());
        }
        return seasons;
    }

    public ArrayList<Episode> getAllEpisodes(long idSeason){
        ArrayList<Episode> episodes = new ArrayList<Episode>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EPISODE + " WHERE "
                + COL_FK_ID_SEASON + " = " + idSeason;

        Log.e("DB_SELECT", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Episode episode = new Episode();
                episode.setId(c.getInt(c.getColumnIndex(COL_PK_ID)));
                episode.setId_season(c.getInt(c.getColumnIndex(COL_FK_ID_SEASON)));
                episode.setName(c.getString(c.getColumnIndex(COL_NAME_EPISODE)));
                episode.setSeen((c.getInt(c.getColumnIndex(COL_EPISODE_SEEN))));
                episode.setAir_date(c.getString(c.getColumnIndex(COL_AIR_DATE)));
                episodes.add(episode);
            } while (c.moveToNext());
        }
        return episodes;
    }

    public Cursor queryAllDataShow(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TVSHOW ;
        return db.rawQuery(selectQuery, null);

    }


}
