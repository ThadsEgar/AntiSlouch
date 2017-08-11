package com.example.michael.antislouch;

import android.database.Cursor;

/**
 * Created by abhi on 8/10/17.
 */


//SQL Database for storing user information

public class DBPoints {
    //Constants
    private static final String TAG = "DBPoints";
    //Fields
    public static final String KEY_TIME = "Time";
    public static final String DEGREES = "Degrees";
    public static final String HEALTHY_POSTURE = "Healthy Posture";

    //Field numbers
    public static final int COL_TIME = 1;
    public static final int COL_DEGREES = 2;
    public static final int COL_HEALTHY_POSTURE = 3;

    public static final String[] COL_NAMES = {KEY_TIME, DEGREES, HEALTHY_POSTURE};



    public DBPoints(graph_menu graph_menu) {
    }

    public void open() {
    }

    public void close() {
    }

    public long insertRow(int degrees, int time) {

        return 0;
    }

    public Cursor getAllRows() {
        return null;
    }


    //Columns





}
