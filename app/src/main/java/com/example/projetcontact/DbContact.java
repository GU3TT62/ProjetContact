/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.projetcontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DbContact {

    public static final String KEY_NOM = "nom";
    public static final String KEY_PRENOM = "prenom";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ADRESS = "adresse";
    public static final String KEY_TEL = "tel";
    public static final String Key_FAV = "fav";
    public static final String KEY_MAIL = "mail";




    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table Contact (_id integer primary key autoincrement, "
        + "nom text not null, prenom text not null, adresse text not null, tel text not null,mail text not null, fav boolean);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "Contact";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Contact");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DbContact(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbContact open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param nom the name of the contact
     * @param prenom the name of the contact
     * @param tel the phone of the contact
     * @param adresse the address of the ncontactote
     * @return rowId or -1 if failed
     */
    public long createContact(String nom, String prenom, String adresse,String tel,String mail,boolean fav) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM, nom);
        initialValues.put(KEY_PRENOM, prenom);
        initialValues.put(KEY_ADRESS, adresse);
        initialValues.put(KEY_TEL, tel);
        initialValues.put(KEY_MAIL, mail);
        initialValues.put(Key_FAV, fav);



        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteContact(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean deleteAllContact(){
        return mDb.delete(DATABASE_TABLE, null,null)>0;
    }

    public void addFav(long rowId){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOM, mDb.query(DATABASE_TABLE, new String[] {KEY_NOM},KEY_ROWID+"="+rowId,null,null,null,null).getString(1));
        cv.put(KEY_PRENOM, mDb.query(DATABASE_TABLE, new String[] {KEY_PRENOM},KEY_ROWID+"="+rowId,null,null,null,null).getString(2));
        cv.put(KEY_ADRESS, mDb.query(DATABASE_TABLE, new String[] {KEY_ADRESS},KEY_ROWID+"="+rowId,null,null,null,null).getString(3));
        cv.put(KEY_TEL, mDb.query(DATABASE_TABLE, new String[] {KEY_TEL},KEY_ROWID+"="+rowId,null,null,null,null).getString(4));
        cv.put(KEY_MAIL, mDb.query(DATABASE_TABLE, new String[] {KEY_MAIL},KEY_ROWID+"="+rowId,null,null,null,null).getString(5));
        cv.put(Key_FAV,true);
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllContact() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NOM,
                KEY_PRENOM,KEY_ADRESS,KEY_TEL,KEY_MAIL,Key_FAV}, null, null, null, null, KEY_NOM);
    }
    //up
    public Cursor fetchFavs(){
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NOM,
                KEY_PRENOM,KEY_ADRESS,KEY_TEL,KEY_MAIL,Key_FAV}, Key_FAV+"="+1, null, null, null, KEY_NOM);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchContact(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_NOM, KEY_PRENOM,KEY_ADRESS,KEY_TEL,KEY_MAIL,Key_FAV}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param nom value to set note title to
     * @param prenom value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateContact(long rowId, String nom, String prenom, String adresse, String tel,String mail,boolean fav) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOM, nom);
        args.put(KEY_PRENOM, prenom);
        args.put(KEY_ADRESS, adresse);
        args.put(KEY_TEL, tel);
        args.put(KEY_MAIL, mail);
        args.put(KEY_MAIL, mail);

        args.put(Key_FAV, fav);



        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
