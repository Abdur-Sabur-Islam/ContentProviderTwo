package com.me.save.com.contentprovidertwo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.URI;

/**
 * Created by acer on 8/13/2017.
 */

public class MyContentProvider extends ContentProvider {

    private static final int ALL_COUNTRIES = 1;
    private static final int SINGLE_COUNTRIES = 2;
    private static final String AUTHORITY = "com.me.save.com.contentprovidertwo";


    // authority name is the symbolic name of your provider

    // to avoid  conflict with other provider

    //you should use domain name
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/countries");


    //create content uri using authority by appending patth database
    private static final UriMatcher uriMatcher;

    // ------------------------------------------------------------ coment -------------------------

    // a content uri pattern matches content uri using wildcard characters
    // *: matches a string of any valid characteristic  of any length
    // # : matches a numeric characteristic of any lenth;

    // =0-------------------------- end comment ----------------------------------------------------

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "countries", ALL_COUNTRIES);
        uriMatcher.addURI(AUTHORITY, "countries/#", SINGLE_COUNTRIES);
    }

    private MyDatabaseHelper dbhelper;


// system start oncreate when the provider start up

    @Override
    public boolean onCreate() {
        // get access to the database helper
        dbhelper = new MyDatabaseHelper(getContext());
        return false;
    }

    // ------------------------------------------------ comment -------------------------

    // the query() method requrn a cursor oibjet ,or if it fails
    // throuws an exception
    //  // The query() method must return a Cursor object, or if it fails,
    // throw an Exception. If you are using an SQLite database as your data storage,
    // you can simply return the Cursor returned by one of the query() methods of the
    // SQLiteDatabase class. If the query does not match any rows, you should return a
    // Cursor instance whose getCount() method returns 0. You should return null only
    // if an internal error occurred during the query process.

    // ---------------------------------------------end comment ------------------


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbhelper.getWritableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(CountriesDb.SQLITE_TABLE);

        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                //  do nothing
                break;
            case SINGLE_COUNTRIES:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(CountriesDb.KEY_ROWID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported Uri:" + uri);

        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {

            case ALL_COUNTRIES:

                return "vnd.android.cursor.dir/vnd.com.me.save.com.contentprovidertwo.countries";

            case SINGLE_COUNTRIES:
                return "vnd.android.cursor.item/vnd.com.me.save.com.contentprovidertwo.countries";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }


    //   -------------------------------coment ----------------


    // the insert() method app row on appropriate table, using content values
    //  in the content values arguments , if a column name is not in agument
    // you can provide a default values


    // --------------------------------------end comment -----------------
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:

                // do nothing
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(CountriesDb.SQLITE_TABLE, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }


    // The delete() method deletes rows based on the seletion or if an id is
    // provided then it deleted a single row. The methods returns the numbers
    // of records delete from the database. If you choose not to delete the data
    // physically then just update a flag here.


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbhelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ALL_COUNTRIES:
                // don nothing
                break;
            case SINGLE_COUNTRIES:

                String id = uri.getPathSegments().get(1);
                selection = CountriesDb.KEY_ROWID + "= " + id + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : "");

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int deleteCount = db.delete(CountriesDb.SQLITE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);


        return deleteCount;

    }


    // the update method same as the delete which update multiple rows
    // base on the selection or single row if the row id is provide
    // the upodate method return the update row


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_COUNTRIES:
                // do nothing
                break;
            case SINGLE_COUNTRIES:

                String id = uri.getPathSegments().get(1);
                selection = CountriesDb.KEY_ROWID+" = "+id+(!TextUtils.isEmpty(selection)? "AND ("+selection+')': "");
                break;
            default:
                throw  new IllegalArgumentException("Unsppported uri:"+uri);

        }
        int updateCount = db.update(CountriesDb.SQLITE_TABLE,values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return updateCount;
    }
}
