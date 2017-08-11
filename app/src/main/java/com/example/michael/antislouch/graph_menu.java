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
    public void insertPoints(int degrees, int time){
        long id = dbadapter.insertRow(degrees, time);

    }


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
                int id = cursor.getInt(0);
                int degrees = cursor.getInt(0);
                int time = cursor.getInt(0);
                points.add(new DataPoint(degrees, time));

            }

            while(cursor.moveToNext());
            //Resource leak to prevent
            cursor.close();

        }
        DataPoint [] pointsArray = points.toArray(new DataPoint[points.size()]);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointsArray);



        graph.addSeries(series);


    }




    public void addListenerOnButton(){


    }

}
