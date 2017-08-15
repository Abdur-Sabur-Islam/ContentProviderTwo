package com.me.save.com.contentprovidertwo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by acer on 8/13/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "holeWorld";
    private static final int DATABASE_VERSION = 2;


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        CountriesDb.onCreate(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        CountriesDb.onUpgrade(sqLiteDatabase,i,i1);
    }
}
