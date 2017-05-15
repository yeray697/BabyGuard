package com.ncatz.babyguard.database;

import android.provider.BaseColumns;

/**
 * Created by yeray697 on 14/05/17.
 */

public class DatabaseContract {

    private static final String TEXT_TYPE = " TEXT";

    public static abstract class Messages implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String SENDER_COL = "sender";
        public static final String RECEIVER_COL = "receiver";
        public static final String MESSAGE_COL = "message";
        public static final String DATETIME_COL= "datetime";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Messages.TABLE_NAME + " (" +
                        Messages._ID + " INTEGER PRIMARY KEY," +
                        Messages.MESSAGE_COL + TEXT_TYPE + "," +
                        Messages.DATETIME_COL + TEXT_TYPE + "," +
                        Messages.SENDER_COL + TEXT_TYPE + "," +
                        Messages.RECEIVER_COL + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Messages.TABLE_NAME;
    }
}