package com.example.michael.antislouch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by abhi on 8/10/17.
 */


//SQL Database for storing user information

public class DBPoints {
    //Constants
    private static final String TAG = "DBPoints";

    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    //Fields
    public static final String KEY_TIME = "Time";
    public static final String DEGREES = "Degrees";
    public static final String HEALTHY_POSTURE = "Healthy Posture";

    //Field numbers
    public static final int COL_TIME = 1;
    public static final int COL_DEGREES = 2;
    public static final int COL_HEALTHY_POSTURE = 3;

    public static final String[] COL_NAMES = {KEY_TIME, DEGREES, HEALTHY_POSTURE};

    //version
    public static final int VERSION = 1;
    //db info
    public static final String DATABASE_NAME = "DBPoints";
    public static final String DATABASE_TABLE = "tablePoints";

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "+KEY_TIME +
                    "integer not null " + DEGREES +
                    " integer not null, "+ HEALTHY_POSTURE + " integer not null);";





    // Context of application 
    private final Context context;

    private DatabaseHelper DBHelp;
    private SQLiteDatabase DB;


    //constructor for db
    public DBPoints(Context ctx) {
        this.context = ctx;
        DBHelp = new DatabaseHelper(context);
    }

    //opens db
    public DBPoints open() {
        DB = DBHelp.getWritableDatabase();
        return this;
    }

    //closes db
    public void close() {
        DBHelp.close();
    }


    //Inserts data
    public long insertRow(int degrees, int time, int healthyposture) {
        ContentValues values = new ContentValues();
        values.put(DEGREES, degrees);
        values.put(KEY_TIME, time);
        values.put(HEALTHY_POSTURE, healthyposture);
        return DB.insert(DATABASE_TABLE, null, values);


    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	DB.query(true, DATABASE_TABLE, COL_NAMES,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get row (by Id)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	DB.query(true, DATABASE_TABLE, COL_NAMES,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }





    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV)  {
            Log.w(TAG, "Upgrading application's database from version " + oldV
                    + " to " + newV + ", which will destroy all old data!");

            // Destroy old database:
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(sqLiteDatabase);
        }

    }


    //Columns





}
