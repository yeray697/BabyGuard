package com.phile.babyguard.database;

import android.provider.BaseColumns;

/**
 * Created by yeray697 on 2/03/17.
 */

public class DatabaseContract {
    static class KidTable implements BaseColumns {
        static final String TABLE_NAME = "kid";
        static final String COLUMN_ID = _ID;
        static final String COLUMN_ID_KID = "kid_id_kid";
        static final String COLUMN_ID_NURSERY = "kid_id_nursery";
        static final String COLUMN_NAME = "kid_name";
        static final String COLUMN_PHOTO = "kid_photo";
        static final String COLUMN_INFO = "kid_info";

        static final String SQL_CREATE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER,"
                        + " %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL)",
                TABLE_NAME, COLUMN_ID, COLUMN_ID_KID, COLUMN_ID_NURSERY,
                COLUMN_NAME, COLUMN_PHOTO,COLUMN_INFO
        );

        static final String SQL_DELETE = String.format(
                "DROP TABLE IF EXISTS %s",
                TABLE_NAME
        );

        static final String[] ALL_COLUMNS = new String[]{
                COLUMN_ID, COLUMN_ID_KID, COLUMN_ID_NURSERY, COLUMN_NAME, COLUMN_PHOTO, COLUMN_INFO
        };
        public static final String SQL_INSERT_ENTRIES = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s')",
                TABLE_NAME,
                COLUMN_ID_KID,COLUMN_ID_NURSERY, COLUMN_NAME, COLUMN_PHOTO, COLUMN_INFO,
                "1","1","Joselito","https://pbs.twimg.com/profile_images/450103729383956480/Tiys3m4x.jpeg","Alérgico a los  cacahuetes",
                "2","1","Lola","http://rackcdn.elephantjournal.com/wp-content/uploads/2012/01/bad-kid.jpg","Alérgica a la lactosa");
    }

    static class InfoKidTable implements BaseColumns {
        static final String TABLE_NAME = "infokid";
        static final String COLUMN_ID = _ID;
        static final String COLUMN_ID_KID= "infokid_id_kid";
        static final String COLUMN_TITLE = "infokid_title";
        static final String COLUMN_DATE = "infokid_date";
        static final String COLUMN_TIME = "infokid_time";
        static final String COLUMN_DESCRIPTION = "infokid_description";
        static final String COLUMN_TYPE = "infokid_type";

        static final String SQL_CREATE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER, %s TEXT NOT NULL, %s TEXT," //TODO Poner al último NOT NULL, porque ahora está de pruebas y no hace falta
                        + " %s TEXT NOT NULL,  %s TEXT NOT NULL, %s INTEGER NOT NULL)",
                TABLE_NAME, COLUMN_ID, COLUMN_ID_KID, COLUMN_TITLE, COLUMN_DATE,
                COLUMN_TIME, COLUMN_DESCRIPTION,COLUMN_TYPE
        );

        static final String SQL_DELETE = String.format(
                "DROP TABLE IF EXISTS %s",
                TABLE_NAME
        );

        static final String[] ALL_COLUMNS = new String[]{
                COLUMN_ID,COLUMN_ID_KID, COLUMN_TITLE, COLUMN_DATE, COLUMN_TIME, COLUMN_DESCRIPTION, COLUMN_TYPE
        };
        public static final String SQL_INSERT_ENTRIES = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s) VALUES (%s,'%s','%s','%s','%s',%s), (%s,'%s','%s','%s','%s',%s),(%s,'%s','%s','%s','%s',%s)," +
                "(%s,'%s','%s','%s','%s',%s),(%s,'%s','%s','%s','%s',%s),(%s,'%s','%s','%s','%s',%s),(%s,'%s','%s','%s','%s',%s),(%s,'%s','%s','%s','%s',%s),(%s,'%s','%s','%s','%s',%s)",
                TABLE_NAME,
                COLUMN_ID_KID,COLUMN_TITLE, COLUMN_DATE, COLUMN_TIME, COLUMN_DESCRIPTION,COLUMN_TYPE,
                "2","Siesta","","11:30","Ha tenido una pesadilla","3",
                "1","Llegada","","08:30","","4",
                "2","Llegada","","08:30","","4",
                "1","Jugando con instrumentos","","12:45","Se le da muy bien el piano","4",
                "2","Jugando con instrumentos","","12:45","Se le da muy bien la flauta","4",
                "1","Desayuno","","11:15","Se lo ha comido todo","2",
                "2","Almuerzo","","14:15","Se lo ha comido todo","2",
                "1","Deposición","","09:50","Regular","1",
                "1","Deposición","","13:50","Ahora la hizo bien","1");
    }

    static class CalendarKidTable implements BaseColumns {
        static final String TABLE_NAME = "calendarkid";
        static final String COLUMN_ID = _ID;
        static final String COLUMN_ID_NURSERY= "calendarkid_id_nursery";
        static final String COLUMN_ID_KID= "calendarkid_id_kid";
        static final String COLUMN_TITLE = "calendarkid_title";
        static final String COLUMN_DESCRIPTION = "calendarkid_description";
        static final String COLUMN_DATE = "calendarkid_date";

        static final String SQL_CREATE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER,%s INTEGER, %s TEXT NOT NULL," +
                        " %s TEXT NOT NULL, %s TEXT NOT NULL)",
                TABLE_NAME, COLUMN_ID, COLUMN_ID_NURSERY, COLUMN_ID_KID, COLUMN_TITLE,
                COLUMN_DESCRIPTION, COLUMN_DATE
        );

        static final String SQL_DELETE = String.format(
                "DROP TABLE IF EXISTS %s",
                TABLE_NAME
        );

        static final String[] ALL_COLUMNS = new String[]{
                COLUMN_ID, COLUMN_ID_NURSERY, COLUMN_ID_KID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_DATE
        };
        public static final String SQL_INSERT_ENTRIES = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (%s, %s,'%s','%s','%s')," +
                        "(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s')" +
                ",(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s'),(%s, %s,'%s','%s','%s')",
                TABLE_NAME,
                COLUMN_ID_KID, COLUMN_ID_NURSERY, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_DATE,
                "-1","1","Cumpleaños de Juan","Se celebrará a primera hora. Comerán tarta","2017-03-15",
                "1","1","Tutoría","Quedamos a las 11:00","2017-03-12",
                "-1","1","Febrero","Empieza febrero","2017-02-01",
                "-1","1","Cumple Raquel","Cumpleaños de Raquel","2017-03-05,",
                "-1","1","Fiesta","Fiesta en tu casa","2017-03-06",
                "-1","1","Cumple Fernando","Fiesta en la bolera","2017-03-07",
                "-1","1","Año nuevo","Feliz año a todos","2017-01-01",
                "-1","1","Cumple Raquel","Cumpleaños de Raquel","2017-03-08,",
                "-1","1","Cumple Fernando","Fiesta en la bolera","2017-03-09",
                "-1","1","Año nuevo","Feliz año a todos","2017-01-10",
                "-1","1","Cumple Raquel","Cumpleaños de Raquel","2017-03-11");
    }

    static class NurseryTable implements BaseColumns {
        static final String TABLE_NAME = "nursery";
        static final String COLUMN_ID = _ID;
        static final String COLUMN_NURSERY_ID= "nursery_id";
        static final String COLUMN_NAME= "nursery_name";
        static final String COLUMN_ADDRESS= "nursery_address";
        static final String COLUMN_EMAIL = "nursery_email";
        static final String COLUMN_WEB = "nursery_web";

        static final String SQL_CREATE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER,%s TEXT NOT NULL,%s TEXT NOT NULL, %s TEXT NOT NULL," +
                        " %s TEXT NOT NULL)",
                TABLE_NAME, COLUMN_ID, COLUMN_NURSERY_ID, COLUMN_NAME, COLUMN_ADDRESS, COLUMN_EMAIL,
                COLUMN_WEB
        );

        static final String SQL_DELETE = String.format(
                "DROP TABLE IF EXISTS %s",
                TABLE_NAME
        );

        static final String[] ALL_COLUMNS = new String[]{
                COLUMN_ID, COLUMN_NURSERY_ID, COLUMN_NAME, COLUMN_ADDRESS, COLUMN_EMAIL, COLUMN_EMAIL
        };
        public static final String SQL_INSERT_ENTRIES = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (%s,'%s','%s','%s','%s')",
                TABLE_NAME,
                COLUMN_NURSERY_ID,COLUMN_NAME, COLUMN_ADDRESS, COLUMN_EMAIL, COLUMN_WEB,
                "1","BestGuard","Calle falsa 123","bestguard@gmail.com","www.bestguard.com");
    }

    static class NurseryTelephoneTable implements BaseColumns {
        static final String TABLE_NAME = "nursery_telephone";
        static final String COLUMN_ID = _ID;
        static final String COLUMN_ID_NURSERY= "nursery_telephone_name";
        static final String COLUMN_PHONE= "nursery_telephone_address";

        static final String SQL_CREATE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER,%s TEXT NOT NULL)",
                TABLE_NAME, COLUMN_ID, COLUMN_ID_NURSERY, COLUMN_PHONE
        );

        static final String SQL_DELETE = String.format(
                "DROP TABLE IF EXISTS %s",
                TABLE_NAME
        );

        static final String[] ALL_COLUMNS = new String[]{
                COLUMN_ID, COLUMN_ID_NURSERY, COLUMN_PHONE
        };
        public static final String SQL_INSERT_ENTRIES = String.format("INSERT INTO %s (%s,%s) VALUES (%s,'%s'),(%s,'%s')",
                TABLE_NAME,
                COLUMN_ID_NURSERY,COLUMN_PHONE,
                "1","985988596",
                "1","698598723");
    }
}
