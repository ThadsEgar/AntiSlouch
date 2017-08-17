package com.example.michael.antislouch;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.Queue;

//DB Instructions
//1. Instantiate DB
//2.Open DB
//3. Use commands
//4. Close db



public class graph_menu extends AppCompatActivity {
    DBPoints dbadapter;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        openDB();
        Log.v("graph_menu", "openDB");
//        insertPoints(1,55);
        Log.v("graph_menu", "POINT1");

//        insertPoints(2, 75);
        Log.v("graph_menu", "POINT2");


//        insertPoints(3, 90);
//        insertPoints(4, 85);
        makePoints();
        Log.v("graph_menu", "Make points");

        closeDB();
        Log.v("graph_menu", "close DB");



        addListenerOnButton();


    }



    @Override
    //Closes DB
    protected void onDestroy(){
        super.onDestroy();
        closeDB();


    }

    private void closeDB() {
        dbadapter.close();

    }

    private void openDB() {
        dbadapter = new DBPoints(this);
        dbadapter.open();



    }
//    public void insertPoints(int time, int degrees){
//        int healthy_posture = 0;
//        //Hard coded in case we want customization
//        if(degrees >= 85){
//            healthy_posture = 1;
//        }
//
//
//        long id = dbadapter.insertRow(time, degrees, healthy_posture);
//
//    }


    public void makePoints(){
        Cursor cursor  = dbadapter.getAllRows();
        displayPoints(cursor);
    }

    private void displayPoints(Cursor cursor) {

        GraphView graph = (GraphView) findViewById(R.id.graph);

        LinkedList<DataPoint> points = new LinkedList<DataPoint>();


        //Reset cursor to start to see data
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(DBPoints.COL_ROWID);
                int degrees = cursor.getInt(DBPoints.COL_DEGREES);
                int time = cursor.getInt(DBPoints.COL_TIME);

                Log.v("graph_menu", DBPoints.COL_ROWID + " " + DBPoints.COL_DEGREES + " " + DBPoints.COL_TIME);
                Log.v("graph_menu", time + " " + degrees + " " + id);
                points.add(new DataPoint(time, degrees));

            }

            while(cursor.moveToNext());
            //Resource leak to prevent
            cursor.close();

        }
        DataPoint [] pointsArray = points.toArray(new DataPoint[points.size()]);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointsArray);



        graph.addSeries(series);
        //date time format
        try {
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        }catch (Exception e){
            Log.e("graph_menu", "something wrong with date time");
        }

    }




    public void addListenerOnButton(){


    }

}
