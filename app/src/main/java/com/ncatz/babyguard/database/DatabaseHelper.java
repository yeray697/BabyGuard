package com.ncatz.babyguard.database;

import android.content.Context;

import com.ncatz.babyguard.Babyguard_Application;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by yeray697 on 14/05/17.
 */

public class DatabaseHelper  extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private SQLiteDatabase database;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Babyguard";
    public static final String DATABASE_EXTENSION = ".db";

    public DatabaseHelper(Context context, String userId) {
        super(context, DATABASE_NAME + "_" + userId + DATABASE_EXTENSION, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context);
    }

    static public synchronized DatabaseHelper getInstance(String userId) {
        if (instance == null) {
            instance = new DatabaseHelper(Babyguard_Application.getContext(),userId);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Messages.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Messages.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void setPassword(String password) {
        database = getWritableDatabase(password);
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }
}
