package com.me.save.com.contentprovidertwo;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by acer on 8/13/2017.
 */

public class CountriesDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_CONTINENT = "continent";


    private static final String LOG_TAG = "countriesDb";
    public static final String SQLITE_TABLE = "country";


    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    KEY_CONTINENT + "," +
                    " UNIQUE (" + KEY_CODE +"));";

    public static void onCreate(SQLiteDatabase sqLiteDatabase){
        Log.w(LOG_TAG, DATABASE_CREATE );
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }

}
