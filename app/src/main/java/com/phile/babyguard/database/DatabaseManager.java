package com.phile.babyguard.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.phile.babyguard.model.InfoKid;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.model.NurserySchool;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeray697 on 2/03/17.
 */

public class DatabaseManager {
    private static DatabaseManager databaseManager;

    private DatabaseManager(){
    }
    public static DatabaseManager getInstance(){
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public NurserySchool getNursery(String id){
        NurserySchool nurserySchool = null;

        SQLiteDatabase database = DatabaseHelper.getInstance().getDatabase();
        Cursor cursor = database.query(DatabaseContract.NurseryTable.TABLE_NAME, DatabaseContract.NurseryTable.ALL_COLUMNS,
                DatabaseContract.NurseryTable.COLUMN_NURSERY_ID+"=?",new String[]{id},null,null,null);

        if(cursor.moveToFirst()){
            String name, address, email, web;
            ArrayList<String> telephones;
            do{
                name = cursor.getString(2);
                address = cursor.getString(3);
                email = cursor.getString(4);
                web = cursor.getString(5);
            }while (cursor.moveToNext());
            cursor = database.query(DatabaseContract.NurseryTelephoneTable.TABLE_NAME, DatabaseContract.NurseryTelephoneTable.ALL_COLUMNS,
                    null,null,null,null,null);
            if(cursor.moveToFirst()){
                telephones = new ArrayList<>();
                do{
                    telephones.add(cursor.getString(2));
                }while (cursor.moveToNext());
                nurserySchool = new NurserySchool(name,address,email,web,telephones);
            }
        }
        return nurserySchool;
    }
    public Kid getKid(String id, String nursery){
        Kid kid = null;

        SQLiteDatabase database = DatabaseHelper.getInstance().getDatabase();
        Cursor cursor = database.query(DatabaseContract.KidTable.TABLE_NAME, DatabaseContract.KidTable.ALL_COLUMNS,
                DatabaseContract.KidTable.COLUMN_ID_KID+"=?",new String[]{id},null,null,null);

        if(cursor.moveToFirst()){
            String name,photo, info;
            ArrayList<InfoKid> infoKids;
            ArrayList<DiaryCalendarEvent> calendarEvents;
            do{
                name = cursor.getString(3);
                photo = cursor.getString(4);
                info = cursor.getString(5);
            }while (cursor.moveToNext());
            cursor = database.query(DatabaseContract.InfoKidTable.TABLE_NAME, DatabaseContract.InfoKidTable.ALL_COLUMNS,
                    DatabaseContract.InfoKidTable.COLUMN_ID_KID+"=?",new String[]{id},null,null,null);
            infoKids = new ArrayList<>();
            calendarEvents = new ArrayList<>();
            if(cursor.moveToFirst()){
                do{
                    infoKids.add(new InfoKid("",cursor.getString(2),cursor.getString(4),InfoKid.parseIntToType(cursor.getInt(6)),cursor.getString(5)));
                }while (cursor.moveToNext());
            }
            cursor = database.query(DatabaseContract.CalendarKidTable.TABLE_NAME, DatabaseContract.CalendarKidTable.ALL_COLUMNS,
                    DatabaseContract.CalendarKidTable.COLUMN_ID_KID+"=? or "+
                            "("+DatabaseContract.CalendarKidTable.COLUMN_ID_NURSERY+"=? and "+DatabaseContract.CalendarKidTable.COLUMN_ID_KID+"=?)",new String[]{id,nursery,"-1"},null,null,null);
            if(cursor.moveToFirst()){
                String date;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                do{
                    date = cursor.getString(5);
                    try {
                        cal.setTime(sdf.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calendarEvents.add(new DiaryCalendarEvent(cursor.getString(3),cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),cursor.getString(4)));
                }while (cursor.moveToNext());
            }
            kid = new Kid(id,name,photo,info,infoKids,calendarEvents);
        }
        return kid;
    }
}
