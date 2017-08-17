package com.example.michael.antislouch;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class menu extends AppCompatActivity{

    DBPoints dbadapter;
    public Button settingsButton;
    public Sensor mSensor;
    private SensorManager mSensorManager;

    private final int DEG_MAX = 105;
    private final int DEG_MIN = 85;
    private Handler handler;

    private Sensor gyroscopeSensor;
    private DBPoints dbAdapter;
    Button graph_menu;

    public void initButtons()
    {
        settingsButton = (Button)findViewById(R.id.Settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent toy = new Intent(menu.this, settings.class);
                startActivity(toy);
            }
        });



    }

    //Nested Orientation class to implement Runnable to make background task for orientation
    public class Orientation implements SensorEventListener, Runnable {


        private SensorManager mSensorManager;


        float[] inR;
        float[] I;
        float[] axisAccel;
        float[] axisMag;
        float[] orientationDegrees;

        double azimuthDegrees;
        double pitchDegrees;
        double rollDegrees;

        Calendar calendar;



        //Contstructor
        public Orientation() {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            inR = new float[16];
            I = new float[16];
            axisAccel = new float[3];
            axisMag = new float[3];
            orientationDegrees = new float[3];
            azimuthDegrees = 0;
            pitchDegrees = 0;
            rollDegrees = 0;
            calendar = Calendar.getInstance();
        }

        //Initiate thread to check orientation
        public void run() {
            //Registers class for accelerometer and magnetic sensor
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_NORMAL);


        }

        //Do not need to check accuracy as it will only
        //be one point, so it would be very insignificant
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

        //Checks if there are new orientation values,
        //and if there is, then it updates the user
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            //Checks if the accuracy of the sensor event data
            //is reliable or not
            if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
                return;

            //Checks the sensor that generated this event and see
            //which had changed
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                axisAccel = sensorEvent.values.clone();
            }
            else if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                axisMag = sensorEvent.values.clone();

            }

            //Checks if neither of hte values are null,
            //which means that somme of them have a
            //new rotation matrix
            if(!(axisMag == null || axisAccel == null)){
                boolean matrixExist = SensorManager.getRotationMatrix(inR, I, axisAccel, axisMag);
                //Gets the new orientational values from
                //change in factor
                if(matrixExist){
                    SensorManager.getOrientation(inR, orientationDegrees);
                    azimuthDegrees = Math.toDegrees(orientationDegrees[0]);
                    pitchDegrees = Math.toDegrees(orientationDegrees[1]);
                    rollDegrees = Math.toDegrees(orientationDegrees[2]);
                    //Checks if the phone is asllep or not
                    PowerManager pmanager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    boolean sleepMode = pmanager.isInteractive();
                    if(sleepMode){
                        int healthy_posture = 1;
                        if(rollDegrees < DEG_MIN){
                            healthy_posture = 0;
                            Toast.makeText(getApplicationContext(),
                                    "Your head is too low.",Toast.LENGTH_SHORT).show();
                        }
                        else if(rollDegrees > DEG_MAX){
                            healthy_posture = 0;
                            Toast.makeText(getApplicationContext(),
                                    "Your head is too high.",Toast.LENGTH_SHORT).show();
                        }
                        insertPoints(calendar.getTime(), (int) rollDegrees, healthy_posture);
                    }
                }
            }
        }

        public void insertPoints(Date time, int degrees, int healthy_posture){
            long id = dbadapter.insertRow(time, degrees, healthy_posture);

        }
    }





    private void closeDB() {
        dbadapter.close();

    }

    private void openDB() {
        dbadapter = new DBPoints(this);
        dbadapter.open();



    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_menu);
        buttonListener();

        initButtons();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }







    public void initiateTime(){

    }






    public void buttonListener(){
        final Context context = this;
        graph_menu = (Button) findViewById(R.id.Graph);
        graph_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent graphIntent =  new Intent(context, graph_menu.class);
                startActivity(graphIntent);
            }
        });

    }

    //This returns the angle orientation by getting x,y,z
    private int angleOrientation(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        return 0;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
