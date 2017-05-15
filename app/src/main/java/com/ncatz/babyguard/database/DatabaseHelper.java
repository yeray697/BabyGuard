package com.ncatz.babyguard.database;

import android.content.Context;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.repository.Repository;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by yeray697 on 14/05/17.
 */

public class DatabaseHelper  extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private SQLiteDatabase database;

    public static final int DATABASE_VERSION = 2;
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

    static public synchronized DatabaseHelper getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("DB not initialized");
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

    public void addMessage(ChatMessage message) {
        String sql = "INSERT INTO "+DatabaseContract.Messages.TABLE_NAME +
                "("+DatabaseContract.Messages.MESSAGE_COL+","+DatabaseContract.Messages.SENDER_COL+","+DatabaseContract.Messages.RECEIVER_COL+","+DatabaseContract.Messages.DATETIME_COL+")" +
                " VALUES ('"+message.getMessage()+"','"+message.getSender()+"','"+message.getReceiver()+"','"+message.getDatetime()+"')";
        database.rawExecSQL(sql);
    }

    public void loadChatMessages(String kidId) {
        ArrayList<Chat> chats = Repository.getInstance().getChats();
        Cursor c;
        String sql;
        ChatMessage aux;
        for (Chat chat : chats) {
            sql = "SELECT * FROM " + DatabaseContract.Messages.TABLE_NAME +
                    " where (" + DatabaseContract.Messages.SENDER_COL + " = '" + kidId +"' AND " + DatabaseContract.Messages.RECEIVER_COL +" = '" + chat.getId() + "') OR "+
                    "(" + DatabaseContract.Messages.SENDER_COL + " = '" + chat.getId() +"' AND " + DatabaseContract.Messages.RECEIVER_COL +" = '" + kidId + "')";
            /*sql = "SELECT * FROM " + DatabaseContract.Messages.TABLE_NAME +
                    " where (" + DatabaseContract.Messages.SENDER_COL + " = " + userId + ") OR "+
                    "(" + DatabaseContract.Messages.SENDER_COL + " = " + chat.getId() + ")";*/
            c = database.rawQuery(sql,null);

            if (c.moveToFirst()) {
                do {
                    aux = new ChatMessage();
                    aux.setMessage(c.getString(1));
                    aux.setDatetime(c.getString(2));
                    aux.setSender(c.getString(3));
                    aux.setReceiver(c.getString(4));
                    chat.addMessage(aux);
                } while (c.moveToNext());
            }
            c.close();
        }
    }
}