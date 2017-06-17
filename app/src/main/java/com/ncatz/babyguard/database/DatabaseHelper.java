package com.ncatz.babyguard.database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatKeyMap;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.services.CalendarService;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that instantiate a database ciphered, with methods to add and read data and
 * change between databases (user log in and sign off trigger it)
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private SQLiteDatabase database;
    private Context context;

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Babyguard";
    public static final String DATABASE_EXTENSION = ".db";


    public DatabaseHelper(Context context, String userId) {
        super(context, DATABASE_NAME + "_" + userId + DATABASE_EXTENSION, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context);
        ((Babyguard_Application) context.getApplicationContext()).setDatabaseLoaded(false);
        this.context = context;
    }

    static public synchronized DatabaseHelper getInstance(String userId) {
        if (instance == null) {
            instance = new DatabaseHelper(Babyguard_Application.getContext(), userId);
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

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void addMessage(ChatMessage message) {
        String sql = "INSERT INTO " + DatabaseContract.Messages.TABLE_NAME +
                "(" + DatabaseContract.Messages.SENDER_COL + "," + DatabaseContract.Messages.MESSAGE_COL + "," + DatabaseContract.Messages.KID_COL + "," +
                DatabaseContract.Messages.TEACHER_COL + "," + DatabaseContract.Messages.DATETIME_COL + ")" +
                " VALUES ('" + message.getSender() + "','" + message.getMessage() + "','" + message.getKid() + "','" + message.getTeacher() + "','" + message.getDatetime() + "')";
        database.rawExecSQL(sql);
    }

    public void loadChatMessages() {
        if (!((Babyguard_Application) context.getApplicationContext()).isDatabaseLoaded() && (Babyguard_Application.isTeacher() || Repository.getInstance().decreaseParentChats() == 0)) { //Avoid duplicate chats
            ((Babyguard_Application) context.getApplicationContext()).setDatabaseLoaded(true);
            AsyncTask<Void, Void, Void> thread = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    HashMap<ChatKeyMap, Chat> chats = Repository.getInstance().getChats();
                    Cursor c;
                    String sql;
                    ChatMessage aux;
                    for (Map.Entry<ChatKeyMap, Chat> chat : chats.entrySet()) {
                        String teacherId = chat.getKey().getTeacherId(),
                                kidId = chat.getKey().getKidId();
                        sql = "SELECT * FROM " + DatabaseContract.Messages.TABLE_NAME +
                                " WHERE (" + DatabaseContract.Messages.TEACHER_COL + " = '" + teacherId + "' AND " + DatabaseContract.Messages.KID_COL + " = '" + kidId + "') " +
                                "ORDER BY " + DatabaseContract.Messages.DATETIME_COL + " ASC";
                        c = database.rawQuery(sql, null);

                        if (c.moveToFirst()) {
                            do {
                                aux = new ChatMessage();
                                aux.setSender(Integer.parseInt(c.getString(1)));
                                aux.setMessage(c.getString(2));
                                aux.setDatetime(c.getString(3));
                                aux.setTeacher(c.getString(4));
                                aux.setKid(c.getString(5));
                                chat.getValue().addMessage(aux);
                            } while (c.moveToNext());
                        }
                        c.close();
                    }
                    FirebaseManager.getInstance().setDeviceId();
                    context.startService(new Intent(context, CalendarService.class));
                    return null;
                }
            };
            thread.execute();
        }
    }

    public void closeDb() {
        database.close();
        this.close();
        instance = null;
    }
}
