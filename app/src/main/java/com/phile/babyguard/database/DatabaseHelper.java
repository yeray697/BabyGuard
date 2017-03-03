package com.phile.babyguard.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.phile.babyguard.Babyguard_Application;

/**
 * Created by yeray697 on 2/03/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "babyguard.db";
    private static final int DATABASE_VERSION = 2;

    private static volatile DatabaseHelper instance;
    private SQLiteDatabase database;

    public synchronized static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private DatabaseHelper() {
        super(Babyguard_Application.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL(DatabaseContract.KidTable.SQL_CREATE);
            sqLiteDatabase.execSQL(DatabaseContract.InfoKidTable.SQL_CREATE);
            sqLiteDatabase.execSQL(DatabaseContract.CalendarKidTable.SQL_CREATE);
            sqLiteDatabase.execSQL(DatabaseContract.NurseryTable.SQL_CREATE);
            sqLiteDatabase.execSQL(DatabaseContract.NurseryTelephoneTable.SQL_CREATE);
            sqLiteDatabase.execSQL(DatabaseContract.NurseryTable.SQL_INSERT_ENTRIES);
            sqLiteDatabase.execSQL(DatabaseContract.NurseryTelephoneTable.SQL_INSERT_ENTRIES);
            sqLiteDatabase.execSQL(DatabaseContract.KidTable.SQL_INSERT_ENTRIES);
            sqLiteDatabase.execSQL(DatabaseContract.InfoKidTable.SQL_INSERT_ENTRIES);
            sqLiteDatabase.execSQL(DatabaseContract.CalendarKidTable.SQL_INSERT_ENTRIES);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception ex){
            ex.getMessage();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL(DatabaseContract.KidTable.SQL_DELETE);
            sqLiteDatabase.execSQL(DatabaseContract.InfoKidTable.SQL_DELETE);
            sqLiteDatabase.execSQL(DatabaseContract.CalendarKidTable.SQL_DELETE);
            sqLiteDatabase.execSQL(DatabaseContract.NurseryTable.SQL_DELETE);
            sqLiteDatabase.execSQL(DatabaseContract.NurseryTelephoneTable.SQL_DELETE);
            onCreate(sqLiteDatabase);
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,newVersion,oldVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys = ON");
            }
        }
    }

    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    void closeDatabase() {
        database.close();
    }
}
